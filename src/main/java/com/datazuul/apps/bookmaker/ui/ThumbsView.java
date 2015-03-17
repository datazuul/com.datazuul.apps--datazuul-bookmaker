package com.datazuul.apps.bookmaker.ui;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;

public class ThumbsView extends ScrollableFlowPanel {

	private static final long serialVersionUID = -8384126263798751312L;

	private Vector<ThumbControl> thumbnails = new Vector<ThumbControl>();

	private final MouseAdapter mo;

	protected File directory;

	private final Comparator<File> comp;

	private LoadThread loadThread;

	public ThumbsView(File directory, MouseAdapter mo,
			FilePatternComparator comp) {
		super();
		this.directory = directory;
		this.mo = mo;
		this.comp = comp;
		this.loadThread = new LoadThread();
		FlowLayout fl = (FlowLayout) this.getLayout();
		fl.setAlignment(FlowLayout.LEFT);
		System.out.println("Pattern recognition. Loading files");
		List<File> files = Arrays.asList(directory.listFiles());
		if (comp != null) {
			System.out
					.println("Pattern recognition. Recognizing and sorting, this may take a *VERY LONG* time");
			Collections.sort(files, comp);
		}
		if (mo != null) {
			this.addMouseListener(mo);
		}
		System.out.println("Pattern recognition. Building UI");
		for (File file : files) {
			ThumbControl tc = new ThumbControl(file, loadThread);
			if (comp != null) {
				double mv = comp.getMatchValue(file);
				tc.setToolTipText(new Double(mv).toString());
			}
			if (mo != null) {
				add(tc).addMouseListener(mo);
			} else {
				add(tc);
			}
			thumbnails.add(tc);
		}
	}

	public Vector<File> getSelectedFiles() {
		Vector<File> files = new Vector<File>();
		for (ThumbControl tc : thumbnails) {
			if (tc.selected) {
				files.add(tc.file);
			}
		}
		return files;
	}

	public void refresh() {
		thumbnails.removeAllElements();
		removeAll();
		updateUI();
		List<File> files = Arrays.asList(directory.listFiles());
		if (comp != null) {
			Collections.sort(files, comp);
		}
		for (File file : files) {
			ThumbControl tc = new ThumbControl(file, loadThread);
			if (mo != null) {
				add(tc).addMouseListener(mo);
			}
			thumbnails.add(tc);
		}
	}

	public void selectAll() {
		for (ThumbControl tc : thumbnails) {
			tc.selected = true;
		}
		updateUI();
	}

	public void deSelectAll() {
		for (ThumbControl tc : thumbnails) {
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

}
