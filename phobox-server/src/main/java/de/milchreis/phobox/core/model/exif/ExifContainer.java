package de.milchreis.phobox.core.model.exif;

import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

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
        return getByTagId(ExifSubIFDDirectory.TAG_FOCAL_LENGTH).getValue();
    }

    public ExifItem getByTagId(String tagId) {
        return items.get(tagId);
    }

    public ExifItem getByTagId(int tagId) {
        String hex = "0x" + Integer.toHexString(tagId);
        return items.get(hex);
    }

    public void add(Tag tag) {
        items.put(tag.getTagTypeHex(), new ExifItem(tag.getTagTypeHex(), tag.getTagName(), tag.getDescription()));
    }
}
