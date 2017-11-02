package de.milchreis.phobox.utils;

public class VersionComparator {

	public static int compare(String seperator, String version1, String version2) 
	throws NumberFormatException {
		
		String[] v1 = version1.split(seperator);
		String[] v2 = version2.split(seperator);
		
		int maxsize = v1.length >= v2.length ? v1.length : v2.length;
		
		for(int i=0; i<maxsize; i++) {
			
			if(i == v2.length) {
				return 1;
			}
			
			if(i == v1.length) {
				return -1;
			}
			
			Integer iv1 = Integer.parseInt(v1[i]);
			Integer iv2 = Integer.parseInt(v2[i]);
		
			int cmp = iv1.compareTo(iv2);
			if(cmp != 0)	return cmp;
		}
		
		return 0;
	}
	
}
