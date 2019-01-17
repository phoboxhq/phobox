package de.milchreis.phobox.core.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemTagRatio {

    private long allItems;
    private long taggedItems;
    private long untaggedItems;
    private float taggingRatio;


    public ItemTagRatio(long countAll, long countTaggedItems) {
        allItems = countAll;
        taggedItems = countTaggedItems;

        untaggedItems = allItems - taggedItems;
        taggingRatio = (float)taggedItems / (float)allItems;
    }
}
