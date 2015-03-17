package com.datazuul.apps.bookmaker.main;

import com.datazuul.apps.bookmaker.ui.MainWindow;

public class ImageOrganizer {

	public MainWindow mainWindow;

	private ImageOrganizer() {
		this.mainWindow = new MainWindow(this);
	}

	public static void main(String[] args) {
		new ImageOrganizer();
	}

}
