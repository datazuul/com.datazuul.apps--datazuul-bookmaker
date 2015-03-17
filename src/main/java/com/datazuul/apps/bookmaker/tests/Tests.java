package com.datazuul.apps.bookmaker.tests;

import com.datazuul.apps.bookmaker.pattern.Pattern;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.xml.sax.SAXException;

public class Tests extends TestCase {

	public void testPatternFromImage() throws IOException, InterruptedException {
		Pattern pat = new Pattern("Foo", new File(
				"testData/images/1189521390761.jpg"), null);
		assertTrue(pat
				.checkImage(new File("testData/images/1190405635262.jpg")) < pat
				.checkImage(new File("testData/images/1189521390761.jpg")));
		assertTrue(pat.checkImage(new File("testData/images/123_hehe.jpg")) < pat
				.checkImage(new File("testData/images/1189521390761.jpg")));
		assertTrue(pat
				.checkImage(new File("testData/images/1190405635262.jpg")) > pat
				.checkImage(new File("testData/images/123_hehe.jpg")));
	}

	public void testPatternFromImage2() throws IOException,
			InterruptedException, SAXException, ParserConfigurationException {
		Pattern pat = new Pattern("Foo2", new File("testData/images/1.jpg"),
				new File("/tmp"));
		assertTrue(pat.checkImage(new File("testData/images/1.jpg")) > pat
				.checkImage(new File("testData/images/0.jpg")));
		pat.save("/tmp/testPat.xml");
		Pattern pat2 = new Pattern("/tmp/testPat.xml");
		assertTrue(pat2.checkImage(new File("testData/images/1.jpg")) > pat2
				.checkImage(new File("testData/images/0.jpg")));
	}

	public void testPatternFromImage3() throws IOException,
			InterruptedException, SAXException, ParserConfigurationException {
		Pattern pat = new Pattern("Foo3", new File("testData/images/1.jpg"),
				new File("/tmp"));
		pat.addImage(new File("testData/images/0.jpg"));
		double d = pat.checkImage(new File("testData/images/1.jpg"));
		pat.save("/tmp/testPat.xml");
		Pattern pat2 = new Pattern("/tmp/testPat.xml");
		assertTrue(pat2.checkImage(new File("testData/images/1.jpg")) == d);
	}

}
