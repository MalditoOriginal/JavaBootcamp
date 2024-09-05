import java.util.Scanner;

public class CommandParser {
    private final FileManager fileManager;

    public CommandParser(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(fileManager.getCurrentPath().toAbsolutePath());
            System.out.print("-> ");
            String command = scanner.nextLine().trim();

            if (command.equals("exit")) {
                break;
            }

            processCommand(command);
        }
    }

    private void processCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "ls":
                fileManager.listFiles();
                break;
            case "cd":
                if (parts.length > 1) {
                    fileManager.changeDirectory(parts[1]);
                } else {
                    System.out.println("Usage: cd FOLDER_NAME");
                }
                break;
            case "mv":
                if (parts.length > 2) {
                    fileManager.moveOrRename(parts[1], parts[2]);
                } else {
                    System.out.println("Usage: mv WHAT WHERE");
                }
                break;
            default:
                System.out.println("Unknown command: " + parts[0]);
                break;
        }
    }
}
