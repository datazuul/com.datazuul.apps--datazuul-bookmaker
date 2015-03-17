package com.datazuul.apps.bookmaker;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ShowImage extends Component {
	BufferedImage img;
	BufferedImage destinationBI;

	public void paint(Graphics g) {
		AffineTransform at = AffineTransform.getRotateInstance(Math
				.toRadians(90), img.getWidth() / 2, img.getHeight() / 2);

		/*
		 * translate to make sure the rotation doesn't cut off any image data
		 */
		AffineTransform translationTransform;
		translationTransform = findTranslation(at, img);
		double translateX = Math.ceil(translationTransform.getTranslateX());
		double translateY = Math.ceil(translationTransform.getTranslateY());
		at.preConcatenate(translationTransform);

		// instantiate and apply affine transformation filter
		BufferedImageOp bio;
		bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

		// the following is ok for a rotation of multiple of 90° only?
		BufferedImage destinationBI = new BufferedImage(img.getWidth()
				+ (2 * (int) translateX), img.getHeight()
				+ (2 * (int) translateY), img.getType());
		bio.filter(img, destinationBI);

		System.out.println("old size: " + img.getWidth() + " x "
				+ img.getHeight());
		System.out.println("new size: " + destinationBI.getWidth() + " x "
				+ destinationBI.getHeight());

		String[] readerFormatNames = ImageIO.getReaderFormatNames();
		for (int i = 0; i < readerFormatNames.length; i++) {
			String format = readerFormatNames[i];
			System.out.println(format);
		}

		try {
			ImageIO
					.write(
							destinationBI,
							"jpeg",
							new File(
									"/home/ralf/Desktop/WORKSPACES/Workspace-JavaPro/com.datazuul.apps--bookmaker/src/main/java/com/datazuul/apps/bookmaker/DSCF0038-rotated.jpeg"));
		} catch (IOException ex) {
			System.out.println(ex);
		}

		g.drawImage(destinationBI, 0, 0, null);
	}

	/*
	 * find proper translations to keep rotated image correctly displayed
	 */
	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
		Point2D p2din, p2dout;

		p2din = new Point2D.Double(0.0, 0.0);
		p2dout = at.transform(p2din, null);
		double ytrans = p2dout.getY();

		p2din = new Point2D.Double(0, bi.getHeight());
		p2dout = at.transform(p2din, null);
		double xtrans = p2dout.getX();

		AffineTransform tat = new AffineTransform();
		tat.translate(-xtrans, -ytrans);
		return tat;
	}

	public ShowImage() {
		try {
			img = ImageIO
					.read(new File(
							"/home/ralf/Desktop/WORKSPACES/Workspace-JavaPro/com.datazuul.apps--bookmaker/src/main/java/com/datazuul/apps/bookmaker/DSCF0038.JPG"));
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100, 100);
		} else {
			return new Dimension(img.getWidth(null), img.getHeight(null));
		}
	}

	public static void main(String[] args) {

		JFrame f = new JFrame("Load Image Sample");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		f.add(new ShowImage());
		f.pack();
		f.setVisible(true);
	}

}