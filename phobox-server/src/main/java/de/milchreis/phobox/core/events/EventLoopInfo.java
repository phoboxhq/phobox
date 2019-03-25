package de.milchreis.phobox.core.events;

import de.milchreis.phobox.db.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventLoopInfo {

    private Item item;

    public EventLoopInfo(EventLoopInfo loopInfo) {
        item =  loopInfo != null ? loopInfo.getItem() : null;
    }
}
