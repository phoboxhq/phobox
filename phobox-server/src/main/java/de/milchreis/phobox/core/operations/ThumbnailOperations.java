package de.milchreis.phobox.core.operations;

import de.milchreis.phobox.core.model.PhoboxModel;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

@AllArgsConstructor
public class ThumbnailOperations {

    private PhoboxModel model;

    public File getPhysicalThumbnail(File physicalFile) {
        String imgPath = physicalFile.getAbsolutePath().replace(model.getStoragePath(), "");
        String fileExtension = FilenameUtils.getExtension(physicalFile.getName());
        imgPath = imgPath.substring(0, imgPath.length() - fileExtension.length());
        return new File(model.getThumbPath(), imgPath + "jpg");
    }

    public File getPhysicalThumbnailByWebpath(String webpath) {
        return getPhysicalThumbnail(new File(model.getStoragePath(), webpath));
    }
}
