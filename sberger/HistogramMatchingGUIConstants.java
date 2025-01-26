package sberger;

import java.awt.*;

public class HistogramMatchingGUIConstants {
    public static final int MARGIN = 10;
    public static final int HISTOGRAM_WIDTH = 256;
    public static final int HISTOGRAM_HEIGHT = 80;
    public static final int LINE_WIDTH = 1;
    public static final int HISTOGRAM_BASELINE = MARGIN + HISTOGRAM_HEIGHT;
    public static final int HISTOGRAM_X_AXIS_Y = HISTOGRAM_BASELINE + LINE_WIDTH;
    public static final int BUTTON_WIDTH = 180;
    public static final int BUTTON_HEIGHT = 25;
    public static final int WIDTH = MARGIN + HISTOGRAM_WIDTH + MARGIN;
    public static final int HEIGHT = MARGIN + HISTOGRAM_HEIGHT + MARGIN;
    public static final int BORDER_X = (WIDTH - BUTTON_WIDTH) / 2;
    public static final int BORDER_Y = (HEIGHT - BUTTON_HEIGHT) / 2;
    public static final int FONT_SIZE = 16;
    public static final int SUBSCRIPT_FONT_SIZE = 10;
    public static final int TEXT_MARGIN_X = WIDTH / 6;
    public static final int TEXT_MARGIN_Y = MARGIN + FONT_SIZE;
    public static final int SUBSCRIPT_MARGIN_Y = TEXT_MARGIN_Y + 2;
    public static final int UNDERLINE_Y = SUBSCRIPT_MARGIN_Y + 1;
    public static final Font FONT = new Font("SansSerif", Font.BOLD, FONT_SIZE);
    public static final Font SUBSCRIPT_FONT = new Font("SansSerif", Font.BOLD, SUBSCRIPT_FONT_SIZE);
}
