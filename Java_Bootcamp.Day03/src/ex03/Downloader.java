import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Downloader implements Runnable {
    private final int threadId;

    public Downloader(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        while (true) {
            String url = Program.getNextUrl();
            if (url == null) {
                if (Program.isFileQueueEmpty()) {
                    Program.decrementActiveThreads();
                }
                break;
            }

            int fileNumber = Program.getNextFileNumber();
            Program.logStart(threadId, fileNumber);
            Program.incrementActiveThreads();

            downloadFile(url);

            Program.logFinish(threadId, fileNumber);
            Program.decrementActiveThreads();
        }
    }

    private void downloadFile(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream in = connection.getInputStream();
                     OutputStream out = new java.io.FileOutputStream(fileUrl.substring(fileUrl.lastIndexOf('/') + 1))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                    responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                String newUrl = connection.getHeaderField("Location");
                downloadFile(newUrl);
            } else {
                System.err.println("Error downloading file. Server returned HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error downloading file: " + fileUrl);
            e.printStackTrace();
        }
    }
}
