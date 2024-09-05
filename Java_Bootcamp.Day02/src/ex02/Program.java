public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--current-folder=")) {
            System.out.println("Usage: java Program --current-folder=ABSOLUTE_PATH");
            return;
        }

        String currentFolderPath = args[0].substring("--current-folder=".length());
        FileManager fileManager = new FileManager(currentFolderPath);
        CommandParser parser = new CommandParser(fileManager);

        parser.start();
    }
}
