package de.milchreis.phobox.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

public class PathConverter {
	private static Logger log = Logger.getLogger(PathConverter.class);

	public static String encode(String imagepath) {
		try {
			imagepath = URLEncoder.encode(imagepath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Imagepath could not decoded: " + imagepath);
		}
		return imagepath;
	}
	
	public static String decode(String imagepath) {
		try {
			imagepath = URLDecoder.decode(imagepath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Imagepath could not decoded: " + imagepath);
		}
		return imagepath;
	}
}
