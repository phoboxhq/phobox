package de.milchreis.phobox.utils.image;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CameraNameFormatter {

    private String maker;
    private String model;

    public String getFormattedCameraName() {
        return getFormattedCameraName(new String[]{maker, model});
    }


    public static String getFormattedCameraName(String[] cameraMakerAndModelArray) {

        String maker = cameraMakerAndModelArray[0] == null ? "" : cameraMakerAndModelArray[0];
        String model = cameraMakerAndModelArray[1] == null ? "" : cameraMakerAndModelArray[1];

        maker = maker.replace("CORPORATION", "");

        if (maker.isEmpty() && model.isEmpty()) {
            return "Unknown";
        }

        if (maker.isEmpty() && !model.isEmpty()) {
            return model;
        }

        if (!maker.isEmpty() && model.isEmpty()) {
            return maker;
        }

        if(model.contains(maker)) {
            return model;
        } else {
            return maker + " " + model;
        }
    }
}
