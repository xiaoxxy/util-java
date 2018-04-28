package tools.common;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FileHelper {
	
	public static boolean writeContentToFile(String content, String filePath) {
		boolean flag = true;
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
			out.write("\n" + content);
			out.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return flag;
	}

}
