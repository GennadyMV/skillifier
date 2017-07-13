package rage.services.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import fi.helsinki.cs.tmc.langs.util.TaskExecutor;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.stereotype.Service;

@Service
public final class FileUtils {

    private TaskExecutor taskExecutor;

    private final Path tmcrun = Paths.get(System.getProperty("server.local.references"), "tmc-run");
    private final Path langsJar = Paths.get(System.getProperty("server.local.references"), "tmc-langs.jar");

    public FileUtils() {
        this.taskExecutor = new TaskExecutorImpl();
        checkAndCopyLangsJar();
    }

    public void checkAndCopyLangsJar() {
        Path parent = Paths.get(System.getProperty("server.local.langs"));
        try {
            Files.copy(parent, langsJar);
        } catch (IOException e) {
            System.out.println("File already exists ? " + e.toString());
        }
    }

    private void extractZip(Path file, Path path) throws ZipException, IOException {
        ZipFile zip = new ZipFile(file.toFile());
        zip.extractAll(path.toString());
    }

    private void moveSubFolderFilesToFolderAndDeleteSubFolder(Path path) throws IOException {
        for (Path entry : Files.newDirectoryStream(path)) {
            if (!Files.isDirectory(entry)) {
                continue;
            }
            for (Path subFile : Files.newDirectoryStream(entry)) {
                Files.move(subFile, path.resolve(subFile.getFileName()));
            }
            Files.delete(entry);
        }
    }
    
    // TODO: Currently works, but not for any directory setup
    // Therefore it needs fixing unless every directory matches criteria
    public Path decompressProjectAndCreateTar(Path project, Path exercisePath) throws IOException, ZipException, ArchiveException {
        Path path = Files.createTempDirectory(project.getParent(), "");
        Path original = Files.createTempDirectory(path, "");
        extractZip(exercisePath, original);
        moveSubFolderFilesToFolderAndDeleteSubFolder(original);
        recursiveDelete(original.resolve("src"));

        extractZip(project, path);
        moveSubFolderFilesToFolderAndDeleteSubFolder(path);
        taskExecutor.compressTarForSubmitting(path, langsJar, tmcrun,
                Paths.get(path.toString() + ".tar"));
        if (path.toString() != null) {
            recursiveDelete(path);
        }
        return Paths.get(path + ".tar");
    }


    public void recursiveDelete(Path path)  {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(path);
                    return FileVisitResult.CONTINUE;
                }
            });
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
