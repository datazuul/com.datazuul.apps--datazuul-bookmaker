/**
 *
 */
package com.datazuul.apps.bookmaker;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;

import com.pdfjet.A4;
import com.pdfjet.Font;
import com.pdfjet.Image;
import com.pdfjet.ImageType;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.TextLine;

/**
 * @author ralf
 *
 */
public class PDFJetExample {
	public PDFJetExample() throws Exception {

		FileOutputStream fos = new FileOutputStream("PDFJetExample.pdf");

		PDF pdf = new PDF(fos);

		Font f1 = new Font(pdf, "Helvetica");

		String fileName = "DSCF0038.JPG";
		BufferedInputStream bis1 = new BufferedInputStream(getClass()
				.getResourceAsStream(fileName));
		Image image1 = new Image(pdf, bis1, ImageType.JPEG);

		fileName = "DSCF0057.JPG";
		BufferedInputStream bis2 = new BufferedInputStream(getClass()
				.getResourceAsStream(fileName));
		Image image2 = new Image(pdf, bis2, ImageType.JPEG);

		Page page = new Page(pdf, A4.PORTRAIT);

//		TextLine text = new TextLine(f1,
//				"The map below is an embedded PNG image");
//		text.setPosition(90, 30);
//		text.drawOn(page);

		double width = image1.getWidth();
		if (width > A4.PORTRAIT[0]) {
			image1.scaleBy(A4.PORTRAIT[0] / width);
		}
		image1.setPosition(0, 0);
		image1.drawOn(page);

//		text.setText("JPG image file embedded once and drawn 3 times");
//		text.setPosition(90, 550);
//		text.drawOn(page);

		page = new Page(pdf, A4.PORTRAIT);
		width = image2.getWidth();
		if (width > A4.PORTRAIT[0]) {
			image2.scaleBy(A4.PORTRAIT[0] / width);
		}
		image2.setPosition(0, 0);
		image2.drawOn(page);

//		text.setText("The map on the right is an embedded BMP image");
//		text.setUnderline(true);
//		text.setStrikeLine(true);
//		text.setTextDirection(15);
//		text.setPosition(90, 800);
//		text.drawOn(page);
//
//		image3.setPosition(390, 630);
//		image3.scaleBy(0.5);
//		image3.drawOn(page);

		pdf.flush();
		fos.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new PDFJetExample();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
