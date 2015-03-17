package com.datazuul.apps.bookmaker.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class LoadThread extends Thread {

	private Vector<ThumbControl> tcs = new Vector<ThumbControl>();

	public LoadThread() {
		start();
	}

	public synchronized void add(ThumbControl tc) {
		tcs.add(tc);
		notifyAll();
	}

	public synchronized ThumbControl remove() {
		while (tcs.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tcs.remove(0);
	}

	public void run() {
		ThumbControl tc;
		while ((tc = remove()) != null) {
			try {
				BufferedImage read = ImageIO.read(tc.file);
				if (read != null) {
					int width = 100;
					int height = 100;
					if (read.getWidth() >= read.getHeight()) {
						height = -1;
					} else {
						width = -1;
					}
					tc.setOriginalImage(read);
					tc.setThumbnail(read.getScaledInstance(width, height,
							BufferedImage.SCALE_FAST));
					tc.repaint();
				}
			} catch (IOException e) {
				// ignore
			}
		}
	}
}
