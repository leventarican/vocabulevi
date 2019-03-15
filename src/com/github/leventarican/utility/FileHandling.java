package com.github.leventarican.utility;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class just holds a {@link List} of the lines in a {@link File}. It reads
 * a file and acts as a container for the lines of a file.
 */
public class FileHandling {

	private List<String> lineHolder;
	private File file;

	public FileHandling(File file) throws Exception {
		super();
		this.lineHolder = new ArrayList<String>();
		this.file = file;
		start(file);
	}

	/*
	 * Start processing the file. First look whether the file is readable and
	 * the then start process.
	 */
	private void start(File file) throws Exception {
		if (FileHandling.getFileInformation(file)) {	// is file available ?
			initializeFile(file);						// start processing
		}
	}

	private void initializeFile(File file) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		Reader fileReader = new FileReader(file);
		for (int c; (c = fileReader.read()) != -1;) {
			if (c == '\n') {							// extract the new line
				packageLine(stringBuilder);
				stringBuilder.delete(0, stringBuilder.length());
			} else {
				stringBuilder.append((char) c);			// line is not finished
			}
		}
	}

	/*
	 * Package every line in a holder.
	 */
	private void packageLine(StringBuilder stringBuilder) {
		lineHolder.add(stringBuilder.toString());
	}

	public static boolean getFileInformation(File file) {
		boolean result = false;

		if (file.exists()) {
			System.out.println(String.format("File: %s exists", file.toString()));
			result = true;
			if (file.canRead()) {
				System.out.println(String.format("File: %s is ready for read\n",file.toString()));
			} else {
				System.err.println(String.format("File: %s cannot opened", file.toString()));
				result = false;
			}
		} else {
			System.err.println(String.format("File: %s does not exists", file.toString()));
		}
		return result;
	}

	public List<String> getLineHolder() {
		return lineHolder;
	}

	public void setLineHolder(List<String> lineHolder) {
		this.lineHolder = lineHolder;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
