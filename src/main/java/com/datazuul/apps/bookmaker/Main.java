package com.datazuul.apps.bookmaker;

import java.io.File;

import com.datazuul.apps.bookmaker.gui.MainWindow;

/**
 * BookMaker's main class.
 */
public class Main {
	public static final String PROGRAM_NAME = "datazuul BookMaker";
	public static final String VERSION = "1.0";

	public static void main(String args[]) {
		MainWindow mainWindow = null;

		if (args.length > 0) {
			mainWindow = new MainWindow(new File(args[0]));
		} else {
			mainWindow = new MainWindow();
		}

		mainWindow.setVisible(true);
	}
}
