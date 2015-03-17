package com.datazuul.apps.bookmaker.tests;

import java.awt.Rectangle;
import java.util.EventListener;

public interface RubberBandListener extends EventListener {

	public abstract void notifyBounds(Rectangle boundingBox);
}
