package de.milchreis.phobox.core.model.statistics;

import lombok.Data;

import java.util.List;

@Data
public class CountedByCameraAndYear {

    private Integer year;
    private String camera;
    private List<Integer> itemsByMonth;

}
