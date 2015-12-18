package com.twitterassistant.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileUtils
 */

public final class FileUtils {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	public static final int SORT_BY_NAME = 1;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	public class FileNameComparator implements Comparator<File> {
		public int compare(File a, File b) {
			return a.getName().compareTo(b.getName());
		}
	}

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	private FileUtils() {
		// not publicly instantiable
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public static File[] listFiles(File dir, int sort) {
		File[] files = dir.listFiles();		
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File a, File b) {
				return a.getName().compareTo(b.getName());
			}
		});
		return files;
	}

	public static String getExt(String file) {
		int i = file.lastIndexOf('.');
		if (i > 0 && i < file.length() - 1)
			return file.substring(i + 1).toLowerCase();
		else
			return null;
	}

	public static void write(String path, byte[] image) throws IOException {

		FileOutputStream fos = null;
		if (image != null) {
			// new File(f.getParent()).mkdirs(); //folders should be there already
			try {
				fos = new FileOutputStream(path);
				fos.write(image);
				fos.close();
			} finally {
				if (fos != null)
					fos.close();
			}
		}
	}

	public static void zip(String file, String dest) throws IOException {

		byte[] buffer = new byte[1024];

		FileOutputStream fos = new FileOutputStream(dest);
		ZipOutputStream zos = new ZipOutputStream(fos);
		ZipEntry ze = new ZipEntry(new File(file).getName());
		zos.putNextEntry(ze);
		FileInputStream in = new FileInputStream(file);

		int len;
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}

		in.close();
		zos.closeEntry();
		zos.close();
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class

