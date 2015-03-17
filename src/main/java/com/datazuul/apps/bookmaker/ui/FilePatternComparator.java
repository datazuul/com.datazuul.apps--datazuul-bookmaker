package com.datazuul.apps.bookmaker.ui;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;

import com.datazuul.apps.bookmaker.pattern.Pattern;

public class FilePatternComparator implements Comparator<File> {

	private final Pattern pattern;
	private HashMap<File, Double> pMap = new HashMap<File, Double>();

	public FilePatternComparator(Pattern p) {
		this.pattern = p;
	}

	@Override
	public int compare(File o1, File o2) {
		try {
			Double d1 = 0.0;
			Double d2 = 0.0;
			if ((d1 = pMap.get(o1)) == null) {
				pMap.put(o1, pattern.checkImage(o1));
				d1 = pMap.get(o1);
			}
			if ((d2 = pMap.get(o2)) == null) {
				pMap.put(o2, pattern.checkImage(o2));
				d2 = pMap.get(o2);
			}
			if (d1 < d2) {
				return 1;
			} else if (d1 > d2) {
				return -1;
			}
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}

	public double getMatchValue(File file) {
		Double d = pMap.get(file);
		if (d == null)
			return 0.0;
		return d;
	}

}
