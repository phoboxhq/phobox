package de.milchreis.phobox.utils.storage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathConverter {

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
