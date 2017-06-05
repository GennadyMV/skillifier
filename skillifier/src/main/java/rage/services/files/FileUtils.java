package rage.services.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fi.helsinki.cs.tmc.langs.util.TaskExecutor;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("nullness")
public final class FileUtils {

    private static TaskExecutor taskExecutor;

    private final Path tmcrun = Paths.get(System.getProperty("server.local.references") + "/tmc-run");
    private final Path langsJar = Paths.get(System.getProperty("server.local.references") + "/tmc-langs.jar");

    public FileUtils() {
        checkAndCopyLangsJar();
        taskExecutor = new TaskExecutorImpl();
    }

    public void checkAndCopyLangsJar() {
        Path parent = Paths.get(System.getProperty("server.local.langs"));
        try {
            Files.copy(parent, langsJar);
        } catch (IOException e) {
            System.out.println("File already exists ? " + e.toString());
        }
    }

    private void extractZip(File file, Path path) throws ZipException, IOException {
        ZipFile zip = new ZipFile(file);
        zip.extractAll(path.toString());
    }

    private void moveSubFolderFilesToFolderAndDeleteSubFolder(Path path) throws IOException {
        for (File file : path.toFile().listFiles()) {
            if (file.isDirectory()) {
                for (File subfile : file.listFiles()) {
                    Files.move(subfile.toPath(), path.resolve(subfile.getName()));
                }
                file.delete();
            }
        }
    }
    
    // TODO: Currently works, but not for any directory setup
    // Therefore it needs fixing unless every directory matches criteria
    public Path decompressProjectAndCreateTar(File project, Path exercisePath) throws IOException, ZipException, ArchiveException {
        Path path = Files.createTempDirectory(Paths.get(project.getParent()), "");
        Path original = Files.createTempDirectory(path, "");
        extractZip(exercisePath.toFile(), original);
        moveSubFolderFilesToFolderAndDeleteSubFolder(original);
        moveSubFolderFilesToFolderAndDeleteSubFolder(original);
        deleteFolderAndItsContent(original.resolve("src").toFile());

        extractZip(project, path);
        moveSubFolderFilesToFolderAndDeleteSubFolder(path);
        taskExecutor.compressTarForSubmitting(path, langsJar, tmcrun,
                Paths.get(path.toString() + ".tar"));
        if (path.toString() != null) {
            deleteFolderAndItsContent(path.toFile());
        }
        return Paths.get(path + ".tar");
    }

    public void deleteFolderAndItsContent(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolderAndItsContent(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
