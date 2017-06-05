package rage.services.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import rage.Skillifier;


@SpringBootTest
@SuppressWarnings("nullness")
@RunWith(SpringJUnit4ClassRunner.class)
public class FileUtilsTest {
    
    static {
        Skillifier.setLocalTestProperties();
    }

    @Autowired private FileUtils utils;

    @Test
    public void testDir() throws IOException, ZipException, ArchiveException {
        Path dataFile = Paths.get(System.getProperty("user.dir") + "/src/test/testResources/data.zip");
        File srcFile = Paths.get(System.getProperty("user.dir") + "/src/test/testResources/exe.zip").toFile();
        File createdTarFile = utils.decompressProjectAndCreateTar(srcFile,dataFile).toFile();
        assertNotNull(createdTarFile);
        assertTrue(createdTarFile.exists());
        createdTarFile.delete();
    }

}
