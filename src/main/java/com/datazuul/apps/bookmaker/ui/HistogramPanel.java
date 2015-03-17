package com.datazuul.apps.bookmaker.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class HistogramPanel extends JPanel {

	private static final long serialVersionUID = 2017354337088366162L;
	public JProgressBar[] bars = new JProgressBar[255];

	public HistogramPanel(double[] levels, Color color) {
		super();
		RealLayout rl = new RealLayout();
		rl.setHorizontal();
		setLayout(rl);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		double max = 0.0;
		for (int i = 0; i < 255; i++) {
			max = Math.max(max, levels[i]);
		}
		for (int i = 0; i < 255; i++) {
			bars[i] = new JProgressBar();
			bars[i].setBorder(null);
			bars[i].setPreferredSize(new Dimension(2, 100));
			bars[i].setOrientation(SwingConstants.VERTICAL);
			bars[i].setMaximum((int) ((10000 * max) + 0.5));
			bars[i].setMinimum(0);
			bars[i].setValue((int) ((levels[i] * 10000.0) + 0.5));
			bars[i].setForeground(color);
			add(bars[i]);
		}
	}

}
