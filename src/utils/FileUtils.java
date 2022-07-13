package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
	public static String removeExtension(File file) {
		if(file.isFile()) {
			return file.getName().substring(0, file.getName().lastIndexOf("."));
		}
		return "";
	}
	
	public static String getExtension(File file) {
		if(file.isFile()) {
			return file.getName().substring(file.getName().lastIndexOf(".") + 1);
		}
		return "";
	}

	public static ArrayList<String> getFileContent(File file) throws IOException {
		ArrayList<String> content = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(file);
		String contentString = new String(fis.readAllBytes());
		for (String string : contentString.split("\n")) {
			content.add(string);
		}
		return content;
	}
	public static String getFileContentAsString(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String contentString = new String(fis.readAllBytes());
		return contentString;
	}
	
}
