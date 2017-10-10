package de.milchreis.phobox.utils;

import java.io.File;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.actions.ReThumbFileAction;
import de.milchreis.phobox.core.actions.ThumbFileAction;

public class ThumbHelper {

	public static int LOW_THUMB_WIDTH = 300;
	public static int LOW_THUMB_HEIGHT = 300;

	public static int HIGH_THUMB_WIDTH = 1920;
	public static int HIGH_THUMB_HEIGHT = 1080;

	public static ThumbFileAction createThumbActionForLow() {
		return new ThumbFileAction(
				LOW_THUMB_WIDTH,
				LOW_THUMB_HEIGHT, 
				new File(Phobox.getModel().getThumbPath(), "low"));
	}
	
	public static ThumbFileAction createThumbActionForHigh() {
		return new ThumbFileAction(
				HIGH_THUMB_WIDTH,
				HIGH_THUMB_HEIGHT, 
				new File(Phobox.getModel().getThumbPath(), "high"));
	}
	
	public static ReThumbFileAction createReThumbActionForLow() {
		return new ReThumbFileAction(
				LOW_THUMB_WIDTH,
				LOW_THUMB_HEIGHT, 
				new File(Phobox.getModel().getThumbPath(), "low"));
	}
	
	public static ReThumbFileAction createReThumbActionForHigh() {
		return new ReThumbFileAction(
				HIGH_THUMB_WIDTH,
				HIGH_THUMB_HEIGHT, 
				new File(Phobox.getModel().getThumbPath(), "high"));
	}
}
