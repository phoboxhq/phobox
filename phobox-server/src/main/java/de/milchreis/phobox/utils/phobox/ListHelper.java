package de.milchreis.phobox.utils.phobox;

import java.util.Arrays;
import java.util.List;

public class ListHelper {
	
	public static boolean endsWith(String word, String[] possibleEndings) {
		if(word == null)
			throw new IllegalArgumentException("The given word is null");

		String needleLow = word.toLowerCase();

		if(possibleEndings == null)
			return true;

		return Arrays.asList(possibleEndings).stream()
			.filter(s -> needleLow.endsWith(s.toLowerCase()))
			.map(s -> true)
			.findFirst()
			.orElse(false);
	}

	public static boolean contains(String text, List<String> searchStrings) {
		if(text == null)
			throw new IllegalArgumentException("The given text is null");

		if(searchStrings == null || searchStrings.size() == 0)
			return false;

		return searchStrings.stream()
				.filter(s -> text.contains(s))
				.count() > 0;
	}

}
