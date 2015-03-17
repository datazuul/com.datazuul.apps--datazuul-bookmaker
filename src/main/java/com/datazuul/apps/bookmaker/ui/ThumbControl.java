package com.datazuul.apps.bookmaker.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ThumbControl extends JPanel {

	private static final long serialVersionUID = 3042227346683364488L;
	protected final File file;
	protected Image image = null;
	private static Image noImage = null;
	private LoadThread loadThread;
	protected boolean selected = false;

	private MouseAdapter mouseHandler = new MouseAdapter() {
		public void mousePressed(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON1) {
				selected = !selected;
				repaint();
			}
		}
	};

	public ThumbControl(File file, LoadThread loadThread) {
		this.file = file;
		this.loadThread = loadThread;
		setPreferredSize(new Dimension(120, 125));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.addMouseListener(mouseHandler);
	}

	public synchronized void paint(Graphics g) {
		if (selected) {
			this.setBackground(Color.BLUE);
		} else {
			this.setBackground(Color.WHITE);
		}
		super.paint(g);
		if (image == null) {
			loadThread.add(this);
			if (noImage == null) {
				BufferedImage bi = new BufferedImage(100, 100,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = bi.createGraphics();
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, 100, 100);
				g2.setColor(Color.RED);
				g2.drawLine(0, 0, 100, 100);
				g2.drawLine(100, 0, 0, 100);
				g2.dispose();
				noImage = bi;
			}
			g.drawImage(noImage, 10, 10, this);
		} else {
			g.drawImage(image, 10, 10, this);
		}
	}
}
