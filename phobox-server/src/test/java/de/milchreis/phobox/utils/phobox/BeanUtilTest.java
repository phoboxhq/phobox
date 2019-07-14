package de.milchreis.phobox.utils.phobox;

import de.milchreis.phobox.server.api.PhotosController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class BeanUtilTest {

    @Test
    public void test_getBean() {

        // Act
        PhotosController controller = BeanUtil.getBean(PhotosController.class);

        // Assert
        assertNotNull(controller);
    }

}