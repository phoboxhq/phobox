package de.milchreis.phobox.utils.image;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.exif.ExifIFD0Directory;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.model.exif.ExifContainer;
import de.milchreis.phobox.utils.exif.ExifHelper;
import de.milchreis.phobox.utils.phobox.ListHelper;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static de.milchreis.phobox.utils.phobox.ListHelper.contains;

public class ImportFormatter {

    private static final SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
    private static final SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");

    private String importFormat;

    public ImportFormatter(String format) {
        importFormat = format;
    }

    public File createPath(File imageFile) throws ImageProcessingException, IOException {

        String subpath = "";
        ExifContainer exifData = ExifContainer.load(imageFile);

        for (String element : importFormat.split("/")) {

            if (contains(element, Arrays.asList("%Y", "%M", "%D"))) {
                Timestamp creationDate = exifData.getCreation();
                element = element.replace("%Y", yearFormatter.format(creationDate));
                element = element.replace("%M", monthFormatter.format(creationDate));
                element = element.replace("%D", dayFormatter.format(creationDate));
            }

            if (contains(element, Arrays.asList("%CAM"))) {

                CameraNameFormatter formatter = new CameraNameFormatter(
                        exifData.getValueByTagId(ExifIFD0Directory.TAG_MAKE),
                        exifData.getValueByTagId(ExifIFD0Directory.TAG_MODEL));

                element = element.replace("%CAM", formatter.getFormattedCameraName());
            }

            if (contains(element, Arrays.asList("%FILE_TYPE"))) {
                element = element.replace("%FILE_TYPE", FilenameUtils.getExtension(imageFile.getName()).toUpperCase());
            }

            if (contains(element, Arrays.asList("%IS_RAW"))) {

                if(ListHelper.endsWith(imageFile.getName(), PhoboxDefinitions.SUPPORTED_RAW_FORMATS)) {
                    element = element.replace("%IS_RAW", "RAW");
                } else {
                    element = element.replace("%IS_RAW", "");
                }
            }

            if (contains(element, Arrays.asList("%ARTIST"))) {

                String artist = exifData.getValueByTagId(ExifIFD0Directory.TAG_ARTIST);

                if(artist != null) {
                    artist = artist.replace("/", " ")
                            .replace("\\", " ")
                            .replace(":", " ");

                    element = element.replace("%ARTIST", artist);
                }
            }

            subpath += element + "/";
        }

        return new File(subpath);
    }

    public boolean isValid() {
        if (importFormat == null || importFormat.isEmpty())
            return false;
        else
            return true;
    }

}
