package com.github.leventarican.utility;

import com.github.leventarican.Launcher;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class should provide some global methods in order to use it in more projects.
 */
public class Utility {

    /**
     * A copy routine for binary files. Copy a file to a given destination directory.
     *
     * @param sourceFile           the file to copy
     * @param destinationDirectory the directory to copy
     * @throws IOException
     */
    public static void copyFileToDirectory(File sourceFile, File destinationDirectory) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), 4096);
        File targetFile = new File(destinationDirectory + "/" + sourceFile.getName());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile), 4096);

        int data;
        while ((data = bis.read()) != -1) {
            bos.write(data);
        }
        
        bos.close();
        bis.close();
        
        targetFile.setLastModified(sourceFile.lastModified());
    }

    /**
     * Retrieves the current time.
     *
     * @return a String in HH:mm:ss format
     */
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Set the location of a window to center.
     *
     * @param frame the Frame to set.
     */
    public static void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = frame.getSize();

        int windowX = Math.max(0, (screenSize.width - windowSize.width) / 2);
        int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2);

        frame.setLocation(windowX, windowY);
    }
    
    /**
     * Print some OS information to console.
     */
    public static void systemInformation() {
    	String osName = ManagementFactory.getOperatingSystemMXBean().getName();
    	String osVersion = ManagementFactory.getOperatingSystemMXBean().getVersion();
    	
    	System.out.println("OS name: " + osName);
    	System.out.println("OS version: " + osVersion);
    }
    
    /**
     * Open a directory in explorer (Windows).
     * This function use the Desktop class introduced in Java 6.
     * 
     * @param file the directory to open.
     * @throws IOException if some errors occurs.
     */
    public static void openFolder(File file) throws IOException {
    	Desktop.getDesktop().open(file);
    }
    
    /**
     * Just create an {@link ImageIcon} for a simple {@link String} path.
     * 
     * @param path the location of the icon
     * @return an {@link ImageIcon} 
     */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Launcher.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	/**
	 * Writes the {@link String}s in a {@link List} to a file.
	 * 
	 * @param filename the filename to write
	 * @param list the list of lines.
	 */
	public static void writeLinesToFile(String filename, List<String> list) {
		Writer writer = null;
		try {
			writer = new FileWriter(filename);
			for (String str : list) writer.write(String.format("%s\n", str));
		}
		catch (IOException e) {e.printStackTrace();}
		finally {if (writer != null) try {writer.close();} catch (IOException e) {}}
	}
}
