package de.milchreis.phobox.core.model.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.milchreis.phobox.db.entities.Item;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemsInPeriod {

    @NonNull private LocalDate startPoint;
    @NonNull private LocalDate endPoint;

    private long numberOfItems;
    private List<Item> items;

    public ItemsInPeriod(LocalDate startPoint, LocalDate endPoint, long numberOfItems) {
        this(startPoint, endPoint);
        this.numberOfItems = numberOfItems;
    }

    public ItemsInPeriod(LocalDate startPoint, LocalDate endPoint, List<Item> items) {
        this(startPoint, endPoint);
        this.items = items;
    }

}
