package sberger;

import ij.IJ;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

// dialog that acts as a view + controller for a specific ImageJ plugin
public class HistogramMatchingGUI
		extends JDialog
		implements ChangeListener, MouseListener, ActionListener, MouseMotionListener {

	// the model for this controller
	final HistogramMatchingPlugin_ model;

	// dialog UI elements
	private JButton btn_openReferenceImage;
	private JButton btn_matchHistograms;

	// histogram panels
	private final HistogramPanel pnl_referenceHistogram;
	private final HistogramPanel pnl_matchedHistogram;

	// constructor puts together the elements of the dialog
	public HistogramMatchingGUI(final HistogramMatchingPlugin_ model) {

		// init dialog window
		super(IJ.getInstance(), "GUI Histogram Matching");

		// remember the model
		this.model = model;

		// dialog containing UI elements to control plugin
		setSize(200, 200);
		setResizable(false);
		Container panel = getContentPane();

		// layout: multiple horizontal boxes nexted in one vertical box
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// original image histogram
		HistogramPanel pnl_originalHistogram = new HistogramPanel(model.getOriginalHistogram(), "h_A");

		// create GUI elements
		btn_openReferenceImage = new JButton("Open Reference Image");
		btn_openReferenceImage.addActionListener(this);
		pnl_referenceHistogram = new HistogramPanel(btn_openReferenceImage);

		btn_matchHistograms = new JButton("Match Histograms");
		btn_matchHistograms.setEnabled(false);
		btn_matchHistograms.addActionListener(this);
		pnl_matchedHistogram = new HistogramPanel(btn_matchHistograms);

		panel.add(pnl_originalHistogram);
		panel.add(pnl_referenceHistogram);
		panel.add(pnl_matchedHistogram);

		// move all components up, keep free space at bottom (if at all)
		panel.add(Box.createVerticalGlue());
		pack();
	}

	// update dialog
	public void update() {
		if (model.isValidated) {
			pnl_referenceHistogram.setHistogram(model.getReferenceHistogram(), "H_R");
			btn_matchHistograms.setEnabled(true);
		} else {
			btn_matchHistograms.setEnabled(false);
		}
	}

	// update values in model, and refresh display of the model
	public void updateModel() {

		// update model due to changes of GUI elements
		model.update();

	}

	// called whenever one of the UI elements changes...
	@Override
	public void stateChanged(ChangeEvent e) {
		updateModel();
	}

	// react to button presses
	@Override
	public void actionPerformed(ActionEvent ev) {

		if (ev.getSource() == this.btn_openReferenceImage) {
			model.loadReferenceImage();
			if(!model.isValidated) {
				JOptionPane.showMessageDialog(this, "Invalid image format");
			}
		} else if (ev.getSource() == this.btn_matchHistograms) {
			if (model.matchHistograms()) {
				pnl_matchedHistogram.setHistogram(model.getMatchedHistogram(), "h_A'");
			}
		}

		updateModel();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}