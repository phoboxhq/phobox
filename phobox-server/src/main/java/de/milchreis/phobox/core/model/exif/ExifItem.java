package de.milchreis.phobox.core.model.exif;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExifItem {

    private String tag;
    private String description;
    private String value;

}
