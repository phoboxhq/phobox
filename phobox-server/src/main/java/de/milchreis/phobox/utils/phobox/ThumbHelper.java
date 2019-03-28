package de.milchreis.phobox.utils.phobox;

import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.actions.ReThumbFileAction;

public class ThumbHelper {

	public static ReThumbFileAction createReThumbAction() {
		return new ReThumbFileAction(
				PhoboxDefinitions.THUMB_WIDTH,
				PhoboxDefinitions.THUMB_HEIGHT);
	}
}
