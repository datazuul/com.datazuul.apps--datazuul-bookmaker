package com.datazuul.apps.bookmaker.pattern;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.datazuul.apps.bookmaker.ui.MainWindow;

public class PatternList implements Iterable<Pattern> {

	private static final long serialVersionUID = 4602763988346715333L;

	private Vector<Pattern> patterns = new Vector<Pattern>();

	public PatternList(MainWindow mw) {
		super();
		File settingsDir = new File(System.getProperty("user.home"),
				".imageOrganizer");
		if (!settingsDir.exists() && !settingsDir.mkdir()) {
			System.err.println("Could not create settings directory");
			System.exit(-1);
		}
		for (File configFile : settingsDir.listFiles()) {
			if (configFile.getName().endsWith(".pat")) {
				try {
					add(new Pattern(configFile.getCanonicalPath()));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
			} else if (configFile.getName().equals("system.conf")) {
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(
									configFile.getCanonicalFile())));
					mw.directory = new File(br.readLine());
					br.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void writeDirectory(File directory) {
		File settingsDir = new File(System.getProperty("user.home"),
				".imageOrganizer");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					settingsDir, "system.conf")));
			bw.write(directory.getCanonicalPath() + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<Pattern> iterator() {
		return patterns.iterator();
	}

	public void remove(Pattern p) {
		new File(System.getProperty("user.home"), ".imageOrganizer/" + p.name
				+ ".pat").delete();
		patterns.remove(p);
	}

	public void add(Pattern pattern) {
		patterns.add(pattern);
		try {
			pattern.save(new File(System.getProperty("user.home"),
					".imageOrganizer/" + pattern.name + ".pat")
					.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
