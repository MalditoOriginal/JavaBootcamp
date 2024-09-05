import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Program {
    private static final Queue<String> fileQueue = new LinkedList<>();
    private static final Object lock = new Object();
    private static final AtomicInteger activeThreads = new AtomicInteger(0);
    private static final AtomicInteger fileCounter = new AtomicInteger(0);
    private static volatile boolean finished = false;
    private static int totalThreads;

    public static void main(String[] args) {
        totalThreads = 3;
        if (args.length > 0 && args[0].startsWith("--threadsCount=")) {
            totalThreads = Integer.parseInt(args[0].split("=")[1]);
        }

        FileLoader.loadUrls("files_urls.txt");
        startDownloadThreads(totalThreads);
        waitForCompletion();
    }

    private static void startDownloadThreads(int threadsCount) {
        for (int i = 0; i < threadsCount; i++) {
            new Thread(new Downloader(i + 1)).start();
        }
    }

    private static void waitForCompletion() {
        synchronized (lock) {
            while (!finished || !fileQueue.isEmpty() || activeThreads.get() > 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static String getNextUrl() {
        synchronized (fileQueue) {
            return fileQueue.poll();
        }
    }

    public static boolean isFileQueueEmpty() {
        synchronized (fileQueue) {
            return fileQueue.isEmpty();
        }
    }

    public static void addUrl(String url) {
        synchronized (fileQueue) {
            fileQueue.add(url);
        }
    }

    public static void logStart(int threadId, int fileNumber) {
        System.out.println("Thread-" + threadId + " start download file number " + fileNumber);
    }

    public static void logFinish(int threadId, int fileNumber) {
        System.out.println("Thread-" + threadId + " finish download file number " + fileNumber);
    }

    public static void incrementActiveThreads() {
        activeThreads.incrementAndGet();
    }

    public static void decrementActiveThreads() {
        if (activeThreads.decrementAndGet() == 0 && isFileQueueEmpty()) {
            finished = true;
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    public static int getNextFileNumber() {
        return fileCounter.incrementAndGet();
    }
}
