package sberger;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import java.util.Arrays;

public class HistogramMatchingPlugin_ implements PlugInFilter {

    // Member variables
    private ImagePlus inputImage;
    private ImagePlus outputImage;
    private ImagePlus referenceImage;

    private ByteProcessor inputIP;
    private ByteProcessor outputIP;

    private HistogramMatchingGUI dialog;
    public boolean isValidated;

    //for easy debugging
    public static void main(String[] args) {
        final ImagePlus originalImage = new Opener().openImage("C:\\Users\\sberg\\Documents\\bht\\ESA2\\trees.png");
        final HistogramMatchingPlugin_ app = new HistogramMatchingPlugin_();
        app.setup("", originalImage);
        app.run(originalImage.getProcessor());
    }

    // plugin is setup here
    public int setup(String arg, ImagePlus imp) {

        // set member variables
        inputImage = imp;

        // returns a flag word indicating the type of images the filter can process
        return DOES_8G + NO_CHANGES;
    }

    // this is the actual main function of PlugInFilter
    public void run(ImageProcessor ip) {

        inputIP = (ByteProcessor) ip;

        isValidated = false;

        // create GUI for this plugin
        dialog = new HistogramMatchingGUI(this);
        dialog.setVisible(true);
    }

    // update model due to changes in the dialog
    public void update() {

        // Here the model can react to changes in the dialog
    }

    // returns the histogram of the input image
    public int[] getOriginalHistogram() {
        return inputIP.getHistogram();
    }

    // returns the histogram of the reference image
    public int[] getReferenceHistogram() {
        return referenceImage.getProcessor().getHistogram();
    }

    // returns the matched histogram
    public int[] getMatchedHistogram() {
        return outputImage.getProcessor().getHistogram();
    }

    // read reference image and validate current state
    public void loadReferenceImage() {

        // open file dialog
        OpenDialog openDialog = new OpenDialog("Reference Image");
        String fileName = openDialog.getPath();

        // read image
        Opener opener = new Opener();
        referenceImage = opener.openImage(fileName);

        // check whether reference image's depth matches with those of the input image
        if (referenceImage.getBitDepth() == 8) {
            isValidated = true;
            dialog.update();
            referenceImage.show();
        }
    }

    private int[] getReferenceCumulativeHistogram() {
        int[] histogram = referenceImage.getProcessor().getHistogram();
        int[] cumulative = new int[histogram.length];
        cumulative[0] = histogram[0];
        for (int i = 1; i < histogram.length; i++) {
            cumulative[i] = cumulative[i - 1] + histogram[i];
        }
        return cumulative;
    }

    // the actual image processing stuff is done here
    public boolean matchHistograms() {

        // get pixel array of input image
        byte[] pixels_input = (byte[]) inputIP.getPixels();

        // get input image's dimensions
        int width = inputIP.getWidth();
        int height = inputIP.getHeight();
        int numpixel = width * height;

        double[] original = histogramProbabilityDistribution(getOriginalHistogram(), false);
        double[] referenceCumulative = histogramProbabilityDistribution(getReferenceCumulativeHistogram(), true);
        int[] lut = new int[original.length];

        double matchedCumulated = 0.0;
        for (int cumulativeIndex = 0, originalIndex = 0; cumulativeIndex < referenceCumulative.length; cumulativeIndex++) {
            while (Math.abs(matchedCumulated - 1) > 0.0000001 && matchedCumulated + original[originalIndex] <= referenceCumulative[cumulativeIndex]) {
                lut[originalIndex] = cumulativeIndex;
                matchedCumulated += original[originalIndex++];
            }
        }

        // create output image
        outputImage = NewImage.createByteImage("Titel", inputIP.getWidth(), inputIP.getHeight(), 1, NewImage.FILL_WHITE);
        outputIP = (ByteProcessor) outputImage.getProcessor();

        // get pixel array of output image
        byte[] pixels_output = (byte[]) outputIP.getPixels();

        // perform gray-value transformation
        // keep in mind that in java byte is an 8-bit signed datatype!!!
        for (
                int i = 0;
                i < numpixel; i++) {
            pixels_output[i] = (byte) (lut[0xff & pixels_input[i]] & 0xff);
        }

        // show output image
        dialog.update();
        outputImage.show();


        return true;

    }

    private double[] histogramProbabilityDistribution(int[] histogram, boolean isCumulative) {
        double pixelCount;
        if (isCumulative) {
            pixelCount = histogram[histogram.length - 1];
        } else {
            pixelCount = Arrays.stream(histogram).reduce(0, Integer::sum);
        }
        return Arrays.stream(histogram).mapToDouble(a -> a / pixelCount).toArray();
    }
}