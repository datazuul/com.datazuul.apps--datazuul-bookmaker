package com.datazuul.apps.bookmaker.gui;

import java.awt.FlowLayout;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;

public class ThumbnailsPanel extends ScrollableFlowPanel {
	private static final long serialVersionUID = 358966964861098342L;
	private File directory;
	private LoadThread loadThread;
	private Vector<ThumbControl> thumbnails = new Vector<ThumbControl>();

	public ThumbnailsPanel(File directory) {
		super();
		this.directory = directory;
		this.loadThread = new LoadThread();

		FlowLayout fl = (FlowLayout) this.getLayout();
		fl.setAlignment(FlowLayout.LEFT);

		List<File> files = Arrays.asList(directory.listFiles());
		for (File file : files) {
			ThumbControl tc = new ThumbControl(file, loadThread);
			tc.setToolTipText(file.getName());
			add(tc);
			getThumbnails().add(tc);
		}
	}

	public Vector<File> getSelectedFiles() {
		Vector<File> files = new Vector<File>();
		for (ThumbControl tc : getThumbnails()) {
			if (tc.selected) {
				files.add(tc.file);
			}
		}
		return files;
	}

	public void refresh() {
		getThumbnails().removeAllElements();
		removeAll();
		updateUI();
		List<File> files = Arrays.asList(directory.listFiles());
		for (File file : files) {
			ThumbControl tc = new ThumbControl(file, loadThread);
			getThumbnails().add(tc);
		}
	}

	public void selectAll() {
		for (ThumbControl tc : getThumbnails()) {
			tc.selected = true;
		}
		updateUI();
	}

	public void deSelectAll() {
		for (ThumbControl tc : getThumbnails()) {
			tc.selected = false;
		}
		updateUI();
	}

	public void setDirectory(File directory) {
		this.directory = directory;
		refresh();
	}

	public void changeDirectory() {
		JFileChooser jfc = new JFileChooser(directory);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			directory = jfc.getSelectedFile();
			refresh();
		}
	}

	/**
	 * @return the thumbnails
	 */
	public Vector<ThumbControl> getThumbnails() {
		return thumbnails;
	}
}
