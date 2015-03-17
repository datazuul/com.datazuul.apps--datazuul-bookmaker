package com.datazuul.apps.bookmaker.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;

/**
 * A simple layout tool that adds components in a row or column. Check the
 * TestRealGUI class for example of usage
 *
 * @author monk
 */
public class RealLayout implements LayoutManager {

	private HashMap<Component, Integer> layout = new HashMap<Component, Integer>();

	private boolean horizontal = false;

	public void setHorizontal() {
		horizontal = true;
	}

	public void setVertical() {
		horizontal = false;
	}

	public void addLayoutComponent(String size, Component comp) {
		layout.put(comp, Integer.parseInt(size));
	}

	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int maxWidth = parent.getWidth() - (insets.left + insets.right);
		int maxHeight = parent.getHeight() - (insets.top + insets.bottom);

		int nextY = 0;
		int nextX = 0;
		int minSize = 0;
		int splitCount = 0;
		Component last = null;
		int size = 0;
		Integer integ = null;
		for (Component c : parent.getComponents()) {
			integ = layout.get(c);
			if (integ == null) {
				if (horizontal) {
					minSize += c.getPreferredSize().width;
				} else {
					minSize += c.getPreferredSize().height;
				}
			} else {
				size = integ;
				if ((size = integ.intValue()) >= 0) {
					minSize += size;
				} else {
					splitCount += -size;
				}
			}
		}
		for (Component c : parent.getComponents()) {
			integ = layout.get(c);
			if (integ == null) {
				if (horizontal) {
					size = c.getPreferredSize().width;
				} else {
					size = c.getPreferredSize().height;
				}
			} else {
				size = integ;
				if (size < 0) {
					if (horizontal) {
						size = (int) ((-(double) size / (double) splitCount) * ((double) maxWidth - minSize));
					} else {
						size = (int) ((-(double) size / (double) splitCount) * ((double) maxHeight - minSize));
					}
				}
			}
			if (horizontal) {
				c.setBounds(nextX, nextY, size, maxHeight);
				nextX += size;
			} else {
				c.setBounds(nextX, nextY, maxWidth, size);
				nextY += size;
			}
			last = c;
		}
		if (last != null && splitCount > 0) {
			if (horizontal) {
				last.setBounds(nextX - size, nextY, maxWidth - nextX + size,
						maxHeight);
			} else {
				last.setBounds(nextX, nextY - size, maxWidth, maxHeight - nextY
						+ size);
			}
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	public Dimension preferredLayoutSize(Container parent) {
		int minSize = 0;
		int size;
		int minWidth = 0;
		int minHeight = 0;
		Integer integ = null;
		for (Component c : parent.getComponents()) {
			minHeight = (int) Math.max(minHeight, c.getPreferredSize()
					.getHeight());
			minWidth = (int) Math
					.max(minWidth, c.getPreferredSize().getWidth());
			integ = layout.get(c);
			if (integ == null) {
				if (horizontal) {
					minSize += c.getPreferredSize().width;
				} else {
					minSize += c.getPreferredSize().height;
				}
			} else {
				size = integ;
				if ((size = integ.intValue()) >= 0) {
					minSize += size;
				}
			}
		}
		if (horizontal)
			return new Dimension(minSize, minHeight);
		return new Dimension(minWidth, minSize);

	}

	public void removeLayoutComponent(Component comp) {
		layout.remove(comp);
	}

}
