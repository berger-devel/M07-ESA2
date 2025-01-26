package sberger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static sberger.HistogramMatchingGUIConstants.*;

public class HistogramPanel extends JPanel {

    private int[] histogram;
    private String label;

    public HistogramPanel(int[] histogram, String label) {
        this();
        this.histogram = normalize(histogram);
        this.label = label;
    }

    public HistogramPanel(JButton button) {
        this();
        add(button);
    }

    private HistogramPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER_Y, BORDER_X, BORDER_Y, BORDER_X));
        setPreferredSize(new Dimension(HistogramMatchingGUIConstants.WIDTH, HistogramMatchingGUIConstants.HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.drawLine(MARGIN, HISTOGRAM_BASELINE, MARGIN, MARGIN);
        g.drawLine(MARGIN, HISTOGRAM_X_AXIS_Y, MARGIN + HISTOGRAM_WIDTH, HISTOGRAM_X_AXIS_Y);

        if (histogram != null) {
            g.setColor(Color.darkGray);
            int x = MARGIN + LINE_WIDTH;
            for (int i = 0; i < HISTOGRAM_WIDTH; i++) {
                g.drawLine(x, HISTOGRAM_BASELINE, x++, HISTOGRAM_BASELINE - histogram[i]);
            }

            String[] denotations = label.split("_");
            g.setFont(FONT);
            g.drawString(denotations[0], TEXT_MARGIN_X, TEXT_MARGIN_Y);
            int underlineWidth = g.getFontMetrics().stringWidth(denotations[0]);
            g.setFont(SUBSCRIPT_FONT);
            g.drawString(denotations[1], TEXT_MARGIN_X + underlineWidth, SUBSCRIPT_MARGIN_Y);
            underlineWidth += g.getFontMetrics().stringWidth(denotations[1]);
            g.drawLine(TEXT_MARGIN_X, UNDERLINE_Y, TEXT_MARGIN_X + underlineWidth, UNDERLINE_Y);
        }
    }

    public void setHistogram(int[] histogram, String label) {
        removeAll();
        this.histogram = normalize(histogram);
        this.label = label;
        validate();
        repaint();
    }

    private int[] normalize(int[] histogram) {
        int maximum = Arrays.stream(histogram).reduce(0, Math::max);
        return Arrays.stream(histogram).map(a -> (int) ((double) a / (double) maximum * HISTOGRAM_HEIGHT)).toArray();
    }
}
