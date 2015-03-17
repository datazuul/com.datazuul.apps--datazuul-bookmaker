package com.datazuul.apps.bookmaker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuBar;

public class MainMenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 3767131778279209374L;
	private final MainWindow mainWindow;

	public MainMenu(MainWindow mainWindow) {
		super();
		this.mainWindow = mainWindow;
		addButton("Change directory");
		addButton("Refresh");
		addButton("Select all");
		addButton("Deselect all");
	}

	public void addButton(String title) {
		JButton mi = new JButton(title);
		mi.addActionListener(this);
		this.add(mi);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = ((JButton) e.getSource()).getText();
		if(command.equals("Change directory")) {
			mainWindow.thumbsView.changeDirectory();
			mainWindow.setDirectory(mainWindow.thumbsView.directory);
		} else if(command.equals("Refresh")) {
			mainWindow.thumbsView.refresh();
		} else if(command.equals("Select all")) {
			mainWindow.thumbsView.selectAll();
		} else if(command.equals("Deselect all")) {
			mainWindow.thumbsView.deSelectAll();
		}
	}

}
