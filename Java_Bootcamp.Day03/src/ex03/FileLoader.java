import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
    public static void loadUrls(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Program.addUrl(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
