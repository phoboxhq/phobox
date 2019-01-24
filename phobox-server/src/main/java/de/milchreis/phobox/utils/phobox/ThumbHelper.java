package de.milchreis.phobox.utils.phobox;

import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.actions.ReThumbFileAction;
import de.milchreis.phobox.core.actions.ThumbFileAction;

public class ThumbHelper {

	public static ThumbFileAction createThumbAction() {
		return new ThumbFileAction(
				PhoboxDefinitions.THUMB_WIDTH,
				PhoboxDefinitions.THUMB_HEIGHT);
	}
	
	public static ReThumbFileAction createReThumbAction() {
		return new ReThumbFileAction(
				PhoboxDefinitions.THUMB_WIDTH,
				PhoboxDefinitions.THUMB_HEIGHT);
	}
}
