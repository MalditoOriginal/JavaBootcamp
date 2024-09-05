import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private Path currentPath;

    public FileManager(String initialPath) {
        this.currentPath = Paths.get(initialPath);
    }

    public void listFiles() {
        File folder = currentPath.toFile();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName() + " " + (file.length() / 1024) + " KB");
            }
        } else {
            System.out.println("Cannot list files: Directory does not exist.");
        }
    }

    public void changeDirectory(String folderName) {
        Path newPath = currentPath.resolve(folderName).normalize();
        if (Files.isDirectory(newPath)) {
            currentPath = newPath;
        } else {
            System.out.println("Directory does not exist: " + folderName);
        }
    }

    public void moveOrRename(String source, String target) {
        Path sourcePath = currentPath.resolve(source);
        Path targetPath = currentPath.resolve(target);

        try {
            Files.move(sourcePath, targetPath);
        } catch (IOException e) {
            System.out.println("Failed to move/rename file: " + e.getMessage());
        }
    }

    public Path getCurrentPath() {
        return currentPath;
    }
}
