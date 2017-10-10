package de.milchreis.phobox.utils;

import java.util.Arrays;

public class ListHelper {
	
	public static boolean endsWith(String needle, String[] heap) {
		String needleLow = needle.toLowerCase();
		
		if(heap == null)
			return true;
		
		return Arrays.asList(heap).stream()
			.filter(s -> needleLow.endsWith(s.toLowerCase()))
			.map(s -> true)
			.findFirst()
			.orElse(false);
	}

}
