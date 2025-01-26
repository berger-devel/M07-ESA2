import ij.IJ;
import ij.gui.NewImage;
import ij.plugin.filter.PlugInFilter;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import java.lang.*;
import ij.ImagePlus;



public class HistogramMatchingPlugin_ implements PlugInFilter {

	// Member variables
	private ImagePlus inputImage;
	private ImagePlus outputImage;
	private ImagePlus referenceImage;
	
	private ByteProcessor inputIP;
	private ByteProcessor outputIP;
	
	private HistogramMatchingGUI dialog;
	public boolean isValidated;
	
	// plugin is setup here
	public int setup(String arg, ImagePlus imp) {
		
		// set member variables
		inputImage = imp;
		
		// returns a flag word indicating the type of images the filter can process
		return DOES_8G+NO_CHANGES;
	}
	
	// this is the actual main function of PlugInFilter
	public void run(ImageProcessor ip) {
	
		inputIP = (ByteProcessor)ip;
		
		isValidated = false;
		
		// create GUI for this plugin
		dialog = new HistogramMatchingGUI(this);
		dialog.setVisible(true);
	}
	
	// update model due to changes in the dialog
	public void update() {
		
		// Here the model can react to changes in the dialog
	}
	
	// read reference image and validate current state
	public void getReferenceImage() {
	
		// open file dialog
		OpenDialog openDialog = new OpenDialog("Reference Image");
		String fileName = openDialog.getPath();
		
		// read image
		Opener opener = new Opener();
		referenceImage = opener.openImage(fileName);
		
		// check whether reference image's depth matches with those of the input image
		// to be continued ...
		
		// if everything is ok set isValidated true
		// to be continued ...
		isValidated = true;
		dialog.update();
	}
	
	
	// the actual image processing stuff is done here 
	public boolean matchHistograms() {	
	
		// get pixel array of input image
		byte[] pixels_input = (byte[]) inputIP.getPixels();
		
		// get input image's dimensions
		int width    = inputIP.getWidth();
		int height   = inputIP.getHeight(); 
		int numpixel = width*height;
		
		
		// create LUT (a simple inversion just for demonstration)
		// THIS HAS TO BE REPLACED BY THE ACTUAL HISTOGRAM-MATCHING!!!
		int[] lut = new int[256];
		for (int i=0;i<256;i++) {
			lut[i]=255-i;
		}
		
		// create output image
		outputImage = NewImage.createByteImage("Titel", inputIP.getWidth(),inputIP.getHeight(),1,NewImage.FILL_WHITE);
		outputIP = (ByteProcessor) outputImage.getProcessor();
		
		// get pixel array of output image
		byte[] pixels_output = (byte[]) outputIP.getPixels();
		
		// perform gray-value transformation
		// keep in mind that in java byte is an 8-bit signed datatype!!!
		for (int i = 0; i<numpixel;i++) {
			pixels_output[i]= (byte) (lut[0xff & pixels_input[i]] & 0xff);
		}
		
		// show output image
		outputImage.show();
				
		
		return true;
	
	}
}