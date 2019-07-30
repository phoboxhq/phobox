package de.milchreis.phobox.utils.image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.drew.imaging.ImageProcessingException;

import de.milchreis.phobox.utils.exif.ExifHelper;

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
        Date creationDate = null;

        for (String element : importFormat.split("/")) {

            if (contains(importFormat, Arrays.asList("%Y", "%M", "%D"))) {

                if(creationDate == null)
                    creationDate = ExifHelper.getCreationDate(imageFile);

                element = element.replace("%Y", yearFormatter.format(creationDate));
                element = element.replace("%M", monthFormatter.format(creationDate));
                element = element.replace("%D", dayFormatter.format(creationDate));
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
