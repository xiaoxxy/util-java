package tools.common;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;


public final class ConfigFileParseHelper {

	
	public static Document getRootElement(){
		Document root = getRootElement("config.xml");
		return root;	
	}
	
	public static Document getRootElement(String xmlPath) {
		Document docRoot = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			docRoot = db.parse(xmlPath);
			return docRoot;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

