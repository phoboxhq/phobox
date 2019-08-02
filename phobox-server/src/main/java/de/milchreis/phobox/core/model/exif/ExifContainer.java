package de.milchreis.phobox.core.model.exif;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import de.milchreis.phobox.utils.image.CameraNameFormatter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Data
public class ExifContainer {

    private Map<String, ExifItem> items = new TreeMap<>();

    public String getFNumber() {
        return getByTagId(ExifSubIFDDirectory.TAG_FNUMBER).getValue();
    }

    public String getExposureTime() {
        return getByTagId(ExifSubIFDDirectory.TAG_EXPOSURE_TIME).getValue();
    }

    public String getISO() {
        return getByTagId(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT).getValue();
    }

    public String getFocalLength() {
        String value = getByTagId(ExifSubIFDDirectory.TAG_FOCAL_LENGTH).getValue();

        if ("0 mm".equals(value))
            return null;

        return value;
    }

    public String getCamera() {
        return CameraNameFormatter.getFormattedCameraName(
                new String[]{
                        getValueByTagId(ExifIFD0Directory.TAG_MAKE),
                        getValueByTagId(ExifIFD0Directory.TAG_MODEL)
                }
        );
    }

    public ExifItem getByTagId(String tagId) {
        return items.get(tagId);
    }

    public Dimension getDimension() {

        String width = getValueByTagId(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
        String height = getValueByTagId(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);

        if (width == null) {
            width = items.entrySet().stream()
                    .filter(e -> e.getValue().getDescription().toLowerCase().contains("width"))
                    .map(e -> e.getValue().getValue().toString())
                    .findFirst()
                    .orElse(null);
        }

        if (height == null) {
            height = items.entrySet().stream()
                    .filter(e -> e.getValue().getDescription().toLowerCase().contains("height"))
                    .map(e -> e.getValue().getValue().toString())
                    .findFirst()
                    .orElse(null);
        }

        try {
            if (width != null) {
                width = width.replace("pixels", "").replace(" ", "");
            }

            if (height != null) {
                height = height.replace("pixels", "").replace(" ", "");
            }

            return new Dimension(Integer.parseInt(width), Integer.parseInt(height));

        } catch (Exception e) {
            return null;
        }
    }

    public Timestamp getCreation() {

        String creation = getValueByTagId(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

        if (creation != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                return new Timestamp(sdf.parse(creation).getTime());
            } catch (ParseException e) {
                log.error("Error while parsing creation: " + creation);
            }
        }

        return null;
    }

    public String getLens() {
        String lens = getValueByTagId(ExifIFD0Directory.TAG_LENS_MODEL);

        if (lens == null)
            lens = items.entrySet().stream()
                    .filter(e -> e.getValue().getDescription().toLowerCase().equals("lens"))
                    .map(e -> e.getValue().getValue().toString())
                    .findFirst()
                    .orElse(null);

        if (lens == null)
            lens = items.entrySet().stream()
                    .filter(e -> e.getValue().getDescription().toLowerCase().contains("lensmodel"))
                    .map(e -> e.getValue().getValue().toString())
                    .findFirst()
                    .orElse(null);

        if (lens == null)
            lens = items.entrySet().stream()
                    .filter(e -> e.getValue().getDescription().toLowerCase().contains("lens type"))
                    .map(e -> e.getValue().getValue().toString())
                    .findFirst()
                    .orElse(null);

        // For Sony cameras with unknown lens
        if (lens != null && lens.startsWith("--"))
            lens = null;

        return lens;
    }

    public ExifItem getByTagId(int tagId) {
        String hex = "0x" + Integer.toHexString(tagId);
        return items.get(hex);
    }

    public String getValueByTagId(int tagId) {
        String hex = "0x" + StringUtils.leftPad(Integer.toHexString(tagId), 4, '0');
        ExifItem item = items.get(hex);
        return item == null ? null : item.getValue();
    }

    public void add(Tag tag) {
        items.put(tag.getTagTypeHex(), new ExifItem(tag.getTagTypeHex(), tag.getTagName(), tag.getDescription()));
    }

    public static ExifContainer load(File file) throws ImageProcessingException, IOException {
        if (file == null)
            throw new IllegalArgumentException("The given file is null");

        ExifContainer container = new ExifContainer();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        metadata.getDirectories().forEach(directory -> {
            directory.getTags().forEach(container::add);
        });

        return container;
    }
}
