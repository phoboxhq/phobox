package de.milchreis.phobox.core.events;

import de.milchreis.phobox.core.events.model.EventLoopInfo;
import de.milchreis.phobox.db.entities.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MetaExtractEventTest {

    @Autowired
    private MetaExtractEvent metaExtractEvent;

    @Test
    public void test_getMetaData() {

        File exampleFilesDirectory = new File("src/test/resources/example-images/");

        for(File image : exampleFilesDirectory.listFiles()) {
            // Arrange
            EventLoopInfo eventLoopInfo = new EventLoopInfo();
            Item item = new Item();
            eventLoopInfo.setItem(item);
            item.setFullPath(image.getAbsolutePath());

            // Act
            metaExtractEvent.onImportFile(image, eventLoopInfo);

            // Assert
            item = eventLoopInfo.getItem();
            assertNotNull(item);
            assertNotNull("Is null for " + image.getName(), item.getLens());
            assertNotNull("Is null for " + image.getName(), item.getFocalLength());
            assertNotNull("Is null for " + image.getName(), item.getRotation());
            assertNotNull("Is null for " + image.getName(), item.getHeight());
            assertNotNull("Is null for " + image.getName(), item.getWidth());
            assertNotNull("Is null for " + image.getName(), item.getCreation());
            assertNotNull("Is null for " + image.getName(), item.getCamera());

            System.out.println(item.toString());
        }
    }

}