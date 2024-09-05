import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Program --arraySize=<size> --threadsCount=<count>");
            return;
        }

        int arraySize = Integer.parseInt(args[0].split("=")[1]);
        int threadsCount = Integer.parseInt(args[1].split("=")[1]);

        if (arraySize > 2000000 || threadsCount > arraySize) {
            System.out.println("Invalid input parameters.");
            return;
        }

        int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = 1;
        }

        int standardSum = arraySize;
        System.out.println("Sum: " + standardSum);

        List<SumThread> threads = new ArrayList<>();
        int sectionSize = arraySize / threadsCount;
        int remainder = arraySize % threadsCount;

        int start = 0;
        for (int i = 0; i < threadsCount; i++) {
            int end = start + sectionSize + (i < remainder ? 1 : 0);
            threads.add(new SumThread(i + 1, array, start, end));
            start = end;
        }

        for (SumThread thread : threads) {
            thread.start();
        }

        int sumByThreads = 0;
        for (SumThread thread : threads) {
            try {
                thread.join();
                sumByThreads += thread.getSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SumThread thread : threads) {
            System.out.println("Thread " + thread.getThreadId() + ": from " + thread.getStart() + " to " + (thread.getEnd() - 1) + " sum is " + thread.getSum());
        }

        System.out.println("Sum by threads: " + sumByThreads);
    }
}