package de.milchreis.phobox.core.events.model;

import de.milchreis.phobox.db.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventLoopInfo {

    private Item item;
    private boolean stopLoop;
    private String stopLoopReason;

    public EventLoopInfo(EventLoopInfo loopInfo) {
        item =  loopInfo != null ? loopInfo.getItem() : null;
    }

    public void stopLoop(String stopReason) {
        stopLoop = true;
        stopLoopReason = stopReason;
    }
}
