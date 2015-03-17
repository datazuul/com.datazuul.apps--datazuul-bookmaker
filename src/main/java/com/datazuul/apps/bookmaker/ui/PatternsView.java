package com.datazuul.apps.bookmaker.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.datazuul.apps.bookmaker.pattern.Pattern;

public class PatternsView extends JList implements ActionListener {

	private static final long serialVersionUID = -7412110539061499953L;
	private final MainWindow mw;

	public PatternsView(MainWindow mw) {
		super(new DefaultListModel());
		this.mw = mw;
		setLayout(new RealLayout());
		setMinimumSize(new Dimension(140, 300));
		updateList();
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3
						&& PatternsView.this.getSelectedIndex() != -1) {
					JPopupMenu pm = new JPopupMenu();
					JMenuItem mi;
					mi = new JMenuItem("Use(Whole directory)");
					mi.addActionListener(PatternsView.this);
					pm.add(mi);
					mi = new JMenuItem("View");
					mi.addActionListener(PatternsView.this);
					pm.add(mi);
					mi = new JMenuItem("Remove");
					mi.addActionListener(PatternsView.this);
					pm.add(mi);
					pm.show((Component) event.getSource(), event.getX(), event
							.getY());
				}
			}
		});
	}

	public void updateList() {
		DefaultListModel dlm = (DefaultListModel) this.getModel();
		dlm.removeAllElements();
		for (Pattern p : mw.patternList) {
			dlm.addElement(p);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = ((JMenuItem) e.getSource()).getText();
		DefaultListModel dlm = (DefaultListModel) this.getModel();
		Pattern p = (Pattern) dlm.get(this.getSelectedIndex());
		if (text.equals("Use(Whole directory)")) {
			ThumbsView tv = new ThumbsView(mw.thumbsView.directory, null,
					new FilePatternComparator(p));
			JScrollPane scroll = new JScrollPane(tv);
			scroll.setMaximumSize(new Dimension(600, 500));
			scroll.setPreferredSize(new Dimension(600, 500));
			scroll.setMinimumSize(new Dimension(600, 500));
			if (JOptionPane.showConfirmDialog(this, scroll,
					"Select desired files", JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION) {
				Vector<File> files = tv.getSelectedFiles();
				System.out
						.println("Moving files according to pattern. Should take less then a minute for resonable amounts");
				for (File file : files) {
					p.move(file);
				}
			}
			mw.thumbsView.refresh();
		} else if (text.equals("View")) {
			JPanel ip = new JPanel(new RealLayout());
			try {
				ip.add(new JLabel(p.destination.getCanonicalPath()));
			} catch (IOException e1) {
				ip.add(new JLabel("ERROR! BROKEN DESTINATION"));
			}
			JPanel panel = new JPanel();
			GridLayout gl = new GridLayout(5, 3);
			panel.setLayout(gl);
			panel.add(new JLabel(new ImageIcon(p.getImage(Pattern.MIN))));
			panel.add(new JLabel(new ImageIcon(p.getImage(Pattern.AVG))));
			panel.add(new JLabel(new ImageIcon(p.getImage(Pattern.MAX))));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MIN,
					Pattern.RED), new Color(100, 0, 0)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.AVG,
					Pattern.RED), new Color(175, 0, 0)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MAX,
					Pattern.RED), new Color(250, 0, 0)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MIN,
					Pattern.GREEN), new Color(0, 100, 0)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.AVG,
					Pattern.GREEN), new Color(0, 175, 0)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MAX,
					Pattern.GREEN), new Color(0, 250, 0)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MIN,
					Pattern.BLUE), new Color(0, 0, 100)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.AVG,
					Pattern.BLUE), new Color(0, 0, 175)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MAX,
					Pattern.BLUE), new Color(0, 0, 250)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MIN,
					Pattern.ALL), new Color(100, 100, 100)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.AVG,
					Pattern.ALL), new Color(175, 175, 175)));
			panel.add(new HistogramPanel(p.getHistogram(Pattern.MAX,
					Pattern.ALL), new Color(250, 250, 250)));
			ip.add(panel);
			JOptionPane.showMessageDialog(this, ip);
		} else if (text.equals("Remove")) {
			mw.patternList.remove(p);
			updateList();
		}
	}

}
