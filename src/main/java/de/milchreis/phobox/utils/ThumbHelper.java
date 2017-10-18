package de.milchreis.phobox.utils;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.actions.ReThumbFileAction;
import de.milchreis.phobox.core.actions.ThumbFileAction;

public class ThumbHelper {

	public static int THUMB_WIDTH = 800;
	public static int THUMB_HEIGHT = 800;

	public static ThumbFileAction createThumbAction() {
		return new ThumbFileAction(
				THUMB_WIDTH,
				THUMB_HEIGHT, 
				Phobox.getModel().getThumbPath());
	}
	
	public static ReThumbFileAction createReThumbAction() {
		return new ReThumbFileAction(
				THUMB_WIDTH,
				THUMB_HEIGHT, 
				Phobox.getModel().getThumbPath());
	}
}
