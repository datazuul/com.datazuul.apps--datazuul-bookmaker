package com.datazuul.apps.bookmaker.tests;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RubberBand {

	private Rectangle rectangle;
	private final List rubberBandListeners = new ArrayList();
	private final Component component;
	private int maxWidth;
	private int maxHeight;

	public RubberBand(Component component) {
		this.component = component;
		addListeners();
	}

	private void addListeners() {
		component.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent event) {
				reactOnMouseDragged(event);
			}
		});
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				reactOnMousePressed(event);
			}

			public void mouseClicked(MouseEvent event) {
				reactOnMouseClicked();
			}
		});
	}

	public int getX() {
		return rectangle.width >= 0 ? rectangle.x
				: (rectangle.x + rectangle.width);
	}

	public int getY() {
		return rectangle.height >= 0 ? rectangle.y
				: (rectangle.y + rectangle.height);
	}

	public int getWidth() {
		return Math.abs(rectangle.width);
	}

	public int getHeight() {
		return Math.abs(rectangle.height);
	}

	public void addRubberBandListener(Runnable rubberBandListener) {
		rubberBandListeners.add(rubberBandListener);
	}

	public void notifyRubberBandListeners() {
		Iterator iterator = rubberBandListeners.iterator();
		while (iterator.hasNext()) {
			((Runnable) iterator.next()).run();
		}
	}

	public void kill() {
		rectangle = null;
		notifyRubberBandListeners();
	}

	private void reactOnMouseDragged(MouseEvent event) {
		if (rectangle == null)
			createRubberBand(0, 0);
		int x = Math.min(event.getX(), maxWidth - 1);
		x = Math.max(0, x);
		int y = Math.min(event.getY(), maxHeight - 1);
		y = Math.max(0, y);
		rectangle.width = x - rectangle.x;
		rectangle.height = y - rectangle.y;
		notifyRubberBandListeners();
	}

	private void createRubberBand(int x, int y) {
		rectangle = new Rectangle(x, y, 0, 0);
	}

	private void reactOnMousePressed(MouseEvent event) {
		createRubberBand(event.getX(), event.getY());
		notifyRubberBandListeners();
	}

	private void reactOnMouseClicked() {
		kill();
	}

	public boolean isActive() {
		return rectangle != null;
	}

	public void setMaxBounds(int width, int height) {
		this.maxWidth = width;
		this.maxHeight = height;
	}

}
