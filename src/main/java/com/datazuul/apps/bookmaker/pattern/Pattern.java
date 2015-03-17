package com.datazuul.apps.bookmaker.pattern;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Pattern {

	public static final int MIN = 0;
	public static final int MAX = 1;
	public static final int AVG = 2;

	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int ALL = 3;

	private static final double PATTERN_PIXEL_COUNT = 1.0 / 30000.0;

	private Vector<PImage> pimages = new Vector<PImage>();

	public String name;
	public File destination;

	public Pattern(String xmlFilePath) throws IOException, SAXException,
			ParserConfigurationException {
		PImage pi = new PImage();
		pimages.add(pi);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = factory.newDocumentBuilder()
				.parse(new File(xmlFilePath));
		Node node = doc.getChildNodes().item(0);
		name = node.getAttributes().getNamedItem("name").getTextContent();
		destination = new File(node.getAttributes().getNamedItem("target")
				.getTextContent());
		NodeList list = node.getChildNodes();
		String color, x, y, intensity, freq;
		int sColor, sX, sY, sIntIntensity;
		double sFreq, sIntensity;
		for (int i = 0; i < list.getLength(); i++) {
			NodeList list2 = list.item(i).getChildNodes();
			for (int j = 0; j < list2.getLength(); j++) {
				Node childNode = list2.item(j);
				if (childNode.getNodeName().equals("pixel")) {
					NamedNodeMap attributes = childNode.getAttributes();
					color = attributes.getNamedItem("color").getTextContent();
					intensity = attributes.getNamedItem("intensity")
							.getTextContent();
					x = attributes.getNamedItem("x").getTextContent();
					y = attributes.getNamedItem("y").getTextContent();
					sColor = Integer.parseInt(color);
					sX = Integer.parseInt(x);
					sY = Integer.parseInt(y);
					sIntensity = Double.parseDouble(intensity);
					pi.areaIntensity[sColor][sX][sY] = sIntensity;
				} else if (childNode.getNodeName().equals("histogram")) {
					NamedNodeMap attributes = childNode.getAttributes();
					color = attributes.getNamedItem("color").getTextContent();
					intensity = attributes.getNamedItem("intensity")
							.getTextContent();
					freq = attributes.getNamedItem("freq").getTextContent();
					sColor = Integer.parseInt(color);
					sIntIntensity = Integer.parseInt(intensity);
					sFreq = Double.parseDouble(freq);
					pi.histogram[sColor][sIntIntensity] = sFreq;
				}
			}
		}
	}

	public void save(String xmlFilePath) throws IOException {
		FileWriter fw = new FileWriter(new File(xmlFilePath));
		fw.write("<pattern name=\"" + name + "\" target=\""
				+ destination.getAbsolutePath() + "\">\n");
		for (PImage pi : pimages) {
			fw.write("<image>\n");
			for (int x = 0; x < 100; x++) {
				for (int y = 0; y < 100; y++) {
					fw.write("<pixel color=\"0\" x=\"" + x + "\" y=\"" + y
							+ "\" intensity=\"" + pi.areaIntensity[0][x][y]
							+ "\" />\n");
					fw.write("<pixel color=\"1\" x=\"" + x + "\" y=\"" + y
							+ "\" intensity=\"" + pi.areaIntensity[1][x][y]
							+ "\" />\n");
					fw.write("<pixel color=\"2\" x=\"" + x + "\" y=\"" + y
							+ "\" intensity=\"" + pi.areaIntensity[2][x][y]
							+ "\" />\n");
				}
			}
			for (int intensity = 0; intensity < 256; intensity++) {
				fw
						.write("<histogram color=\"0\" intensity=\""
								+ intensity + "\" freq=\""
								+ pi.histogram[0][intensity] + "\" />\n");
				fw
						.write("<histogram color=\"1\" intensity=\""
								+ intensity + "\" freq=\""
								+ pi.histogram[1][intensity] + "\" />\n");
				fw
						.write("<histogram color=\"2\" intensity=\""
								+ intensity + "\" freq=\""
								+ pi.histogram[2][intensity] + "\" />\n");
			}
			fw.write("</image>\n");
		}
		fw.write("</pattern>\n");
		fw.close();
	}

	public Pattern(String name, File file, File destination) throws IOException {
		PImage pi = new PImage();
		pimages.add(pi);
		this.name = name;
		this.destination = destination;
		Color countColor;
		Image im2 = ImageIO.read(file).getScaledInstance(100, 100,
				BufferedImage.SCALE_AREA_AVERAGING);
		BufferedImage bi = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gfx = bi.createGraphics();
		gfx.drawImage(im2, 0, 0, null);
		gfx.dispose();
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				countColor = new Color(bi.getRGB(x, y));
				pi.histogram[Pattern.RED][countColor.getRed()] = PATTERN_PIXEL_COUNT;
				pi.histogram[Pattern.GREEN][countColor.getGreen()] = PATTERN_PIXEL_COUNT;
				pi.histogram[Pattern.BLUE][countColor.getBlue()] = PATTERN_PIXEL_COUNT;
			}
		}
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				Color col = new Color(bi.getRGB(x, y));
				pi.areaIntensity[Pattern.RED][x][y] = col.getRed();
				pi.areaIntensity[Pattern.GREEN][x][y] = col.getGreen();
				pi.areaIntensity[Pattern.BLUE][x][y] = col.getBlue();
			}
		}
	}

	public void addImage(File file) throws IOException {
		PImage pi = new PImage();
		pimages.add(pi);
		Image image = ImageIO.read(file).getScaledInstance(100, 100,
				BufferedImage.SCALE_AREA_AVERAGING);
		BufferedImage bi = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gfx = bi.createGraphics();
		gfx.drawImage(image, 0, 0, null);
		gfx.dispose();
		Color countColor;
		double[][] rgbCount = new double[3][256];
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				countColor = new Color(bi.getRGB(x, y));
				rgbCount[Pattern.RED][countColor.getRed()] += PATTERN_PIXEL_COUNT;
				rgbCount[Pattern.GREEN][countColor.getGreen()] += PATTERN_PIXEL_COUNT;
				rgbCount[Pattern.BLUE][countColor.getBlue()] += PATTERN_PIXEL_COUNT;
			}
		}
		for (int rgb = 0; rgb < 3; rgb++) {
			for (int intensity = 0; intensity < 256; intensity++) {
				pi.histogram[rgb][intensity] = rgbCount[rgb][intensity];
			}
		}
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				Color col = new Color(bi.getRGB(x, y));
				pi.areaIntensity[Pattern.RED][x][y] = col.getRed();
				pi.areaIntensity[Pattern.GREEN][x][y] = col.getGreen();
				pi.areaIntensity[Pattern.BLUE][x][y] = col.getBlue();
			}
		}
	}

	private PImage virtualPImage(int type) {
		PImage pi = new PImage();
		boolean first = true;
		for (PImage bi : pimages) {
			for (int x = 0; x < 100; x++) {
				for (int y = 0; y < 100; y++) {
					for (int c = 0; c < 3; c++) {
						if (type == MIN) {
							if (first
									|| bi.areaIntensity[c][x][y] < pi.areaIntensity[c][x][y]) {
								pi.areaIntensity[c][x][y] = bi.areaIntensity[c][x][y];
							}
						} else if (type == AVG) {
							pi.areaIntensity[c][x][y] += bi.areaIntensity[c][x][y];
						} else { // MAX
							if (first
									|| bi.areaIntensity[c][x][y] > pi.areaIntensity[c][x][y]) {
								pi.areaIntensity[c][x][y] = bi.areaIntensity[c][x][y];
							}
						}
					}
				}
			}
			for (int i = 0; i < 256; i++) {
				for (int c = 0; c < 3; c++) {
					if (type == MIN) {
						if (first || pi.histogram[c][i] > bi.histogram[c][i]) {
							pi.histogram[c][i] = bi.histogram[c][i];
						}
					} else if (type == AVG) {
						pi.histogram[c][i] += bi.histogram[c][i];
					} else { // MAX
						if (first || pi.histogram[c][i] < bi.histogram[c][i]) {
							pi.histogram[c][i] = bi.histogram[c][i];
						}
					}
				}
			}
			first = false;
		}
		if (type == AVG) {
			for (int x = 0; x < 100; x++) {
				for (int y = 0; y < 100; y++) {
					for (int c = 0; c < 3; c++) {
						pi.areaIntensity[c][x][y] = pi.areaIntensity[c][x][y]
								/ ((double) pimages.size());
					}
				}
			}
			for (int i = 0; i < 256; i++) {
				for (int c = 0; c < 3; c++) {
					pi.histogram[c][i] = pi.histogram[c][i]
							/ ((double) pimages.size());
				}
			}
		}
		return pi;
	}

	public double checkImage(File file) throws IOException {
		PImage pi = virtualPImage(AVG);
		Image im2 = ImageIO.read(file).getScaledInstance(100, 100,
				BufferedImage.SCALE_AREA_AVERAGING);
		BufferedImage bi = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gfx = bi.createGraphics();
		gfx.drawImage(im2, 0, 0, null);
		gfx.dispose();
		double[][] cRgbCount = new double[3][256];
		Color countColor;
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				countColor = new Color(bi.getRGB(x, y));
				cRgbCount[Pattern.RED][(countColor.getRed())] += PATTERN_PIXEL_COUNT;
				cRgbCount[Pattern.GREEN][(countColor.getGreen())] += PATTERN_PIXEL_COUNT;
				cRgbCount[Pattern.BLUE][(countColor.getBlue())] += PATTERN_PIXEL_COUNT;
			}
		}
		double colorMatch = 1.0;
		double[] rgbMatch = new double[3];
		for (int rgb = 0; rgb < 3; rgb++) {
			rgbMatch[rgb] = 0.0;
			for (int color = 0; color < 256; color++) {
				for (int offset = -5; offset <= 5; offset++) {
					int tc = (color + offset > 255) ? Math
							.abs(255 - (color + offset)) : Math.abs(color
							+ offset);
					rgbMatch[rgb] += ((1.0 - Math.abs(cRgbCount[rgb][color]
							- pi.histogram[rgb][tc])) / 255.0);
				}
			}
			colorMatch *= rgbMatch[rgb];
		}
		double maxMinMatch = 0.0;
		double partValue = 1.0 / (100.0 * 100.0 * 3.0);
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				Color col = new Color(bi.getRGB(x, y));
				int red = (col.getRed());
				int green = (col.getGreen());
				int blue = (col.getBlue());
				double pred = pi.areaIntensity[Pattern.RED][x][y];
				double pgreen = pi.areaIntensity[Pattern.GREEN][x][y];
				double pblue = pi.areaIntensity[Pattern.BLUE][x][y];
				if (red == pred) {
					maxMinMatch += partValue;
				} else if (red < pred) {
					maxMinMatch += partValue * (1.0 - ((pred - red) / pred));
				} else if (red > pred) {
					maxMinMatch += partValue
							* (1.0 - ((red - pred) / (255 - pred)));
				}
				if (green == pgreen) {
					maxMinMatch += partValue;
				} else if (green < pgreen) {
					maxMinMatch += partValue
							* (1.0 - ((pgreen - green) / pgreen));
				} else if (green > pgreen) {
					maxMinMatch += partValue
							* (1.0 - ((green - pgreen) / (255 - pgreen)));
				}
				if (blue == pblue) {
					maxMinMatch += partValue;
				} else if (blue < pblue) {
					maxMinMatch += partValue * (1.0 - ((pblue - blue) / pblue));
				} else if (blue > pblue) {
					maxMinMatch += partValue
							* (1.0 - ((blue - pblue) / (255 - pblue)));
				}
			}
		}
		// handle drop in prec.
		maxMinMatch = ((int) ((maxMinMatch * 100000000.0) + 0.5)) / 100000000.0;
		return colorMatch * maxMinMatch;
	}

	public Image getImage(int type) {
		BufferedImage image = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_RGB);
		PImage himage = virtualPImage(type);
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				image
						.setRGB(
								x,
								y,
								new Color(
										(int) (himage.areaIntensity[Pattern.RED][x][y] + 0.5),
										(int) (himage.areaIntensity[Pattern.GREEN][x][y] + 0.5),
										(int) (himage.areaIntensity[Pattern.BLUE][x][y] + 0.5))
										.getRGB());
			}
		}
		return image;
	}

	public String toString() {
		return name;
	}

	public void move(File file) {
		file.renameTo(new File(destination.getAbsolutePath() + File.separator
				+ file.getName()));
	}

	public double[] getHistogram(int type, int color) {
		double[] levels = new double[256];
		PImage himage = virtualPImage(type);
		for (int i = 0; i < 256; i++) {
			if (color != ALL) {
				levels[i] = himage.histogram[color][i];
			} else {
				for (int c = RED; c <= BLUE; c++) {
					levels[i] += himage.histogram[c][i];
				}
				levels[i] /= 3;
			}
		}
		return levels;
	}

}
