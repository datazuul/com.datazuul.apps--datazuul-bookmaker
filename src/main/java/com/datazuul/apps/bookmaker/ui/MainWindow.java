package com.datazuul.apps.bookmaker.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.datazuul.apps.bookmaker.main.ImageOrganizer;
import com.datazuul.apps.bookmaker.pattern.Pattern;
import com.datazuul.apps.bookmaker.pattern.PatternList;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = -2133534539333334278L;
	protected JSplitPane hpane;
	private PatternsView patternsView;
	public ThumbsView thumbsView;
	public File directory = null;
	private JPopupMenu pm = new JPopupMenu();
	protected final PatternList patternList;

	public MainWindow(ImageOrganizer imageOrganizer) {
		super("ImageOrganizer - 1.0");
		this.patternList = new PatternList(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setJMenuBar(new MainMenu(this));
		patternsView = new PatternsView(this);
		MouseAdapter mo = new MouseAdapter() {

			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) {
					pm.show((Component) event.getSource(), event.getX(), event
							.getY());
				}
			}

		};
		if (directory == null) {
			directory = new File(".");
		}
		thumbsView = new ThumbsView(directory, mo, null);
		pm.add(new JMenuItem("Create pattern from selected"))
				.addActionListener(this);
		JScrollPane scroll = new JScrollPane(thumbsView);
		hpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, patternsView,
				scroll);
		add(hpane);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = ((JMenuItem) e.getSource()).getText();
		if (text.equals("Create pattern from selected")) {
			Vector<File> selectedFiles = thumbsView.getSelectedFiles();
			if (!selectedFiles.isEmpty()) {
				String name = JOptionPane.showInputDialog(this,
						"Enter pattern name");
				if (name != null && !name.equals("")) {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					jfc.setDialogTitle("Select target directory");
					if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
						System.out
								.println("Pattern creation. Takes a while depending on picture amount");
						Pattern pattern = null;
						for (File file : thumbsView.getSelectedFiles()) {
							if (pattern == null) {
								try {
									pattern = new Pattern(name, file, jfc
											.getSelectedFile());
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							} else {
								try {
									pattern.addImage(file);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
						patternList.add(pattern);
						patternsView.updateList();
					}
				}
			}
		}
	}

	public void setDirectory(File directory) {
		this.directory = directory;
		patternList.writeDirectory(directory);
	}

}
