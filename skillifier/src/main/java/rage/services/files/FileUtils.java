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

    public FileUtils() {
        this.taskExecutor = new TaskExecutorImpl();
        checkAndCopyLangsJar();
    }

    public void checkAndCopyLangsJar() {
        String localLangs = System.getProperty("server.local.langs");
        if (localLangs == null) {
            throw new RuntimeException("server.local.langs is not defined");
        }
        Path parent = Paths.get(localLangs);
        try {
            Files.copy(parent, tmcLangsPath());
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
        taskExecutor.compressTarForSubmitting(path, tmcLangsPath(), tmcRunPath(),
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

    private static Path tmcRunPath() {
        String property = System.getProperty("server.local.references");
        if (property == null) {
            throw new RuntimeException("server.local.references is not defined");
        }
        return Paths.get(property, "tmc-run");

    }

    private static Path tmcLangsPath() {
        String property = System.getProperty("server.local.references");
        if (property == null) {
            throw new RuntimeException("server.local.references is not defined");
        }
        return Paths.get(property, "tmc-langs.jar");

    }



}
