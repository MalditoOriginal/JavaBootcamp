import java.util.Arrays;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        int[] counts = countCharacters(input);
        char[] topChars = new char[10];
        int[] topCounts = new int[10];

        findTopCharacters(counts, topChars, topCounts);
        printResults(topChars, topCounts);
    }

    private static int[] countCharacters(String input) {
        int[] counts = new int[65536];
        for (char c : input.toCharArray()) {
            counts[c]++;
        }
        return counts;
    }

    private static void findTopCharacters(int[] counts, char[] topChars, int[] topCounts) {
        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];
            for (int j = 0; j < topCounts.length; j++) {
                if (count > topCounts[j]) {
                    insertIntoTopList(topCounts, topChars, count, (char) i, j);
                    break;
                } else if (count == topCounts[j] && i < topChars[j]) {
                    insertIntoTopList(topCounts, topChars, count, (char) i, j);
                    break;
                }
            }
        }
    }

    private static void insertIntoTopList(int[] topCounts, char[] topChars, int count, char c, int index) {
        for (int k = topCounts.length - 1; k > index; k--) {
            topCounts[k] = topCounts[k - 1];
            topChars[k] = topChars[k - 1];
        }
        topCounts[index] = count;
        topChars[index] = c;
    }

    private static void printResults(char[] topChars, int[] topCounts) {
        int maxCount = topCounts[0];
        System.out.println();
        printTopCounts(topCounts, maxCount);
        printHistogram(topCounts, maxCount);
        printTopCharacters(topChars);
    }

    private static void printTopCounts(int[] topCounts, int maxCount) {
        for (int i = 0; i < 10; i++) {
            if (topCounts[i] == maxCount) {
                System.out.print(topCounts[i] + "\t");
            }
        }
        System.out.println();
    }

    private static void printHistogram(int[] topCounts, int maxCount) {
        for (int i = 10; i > 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (topCounts[j] * 10 / maxCount >= i) {
                    System.out.print("#\t");
                } else if (topCounts[j] * 10 / maxCount == i - 1) {
                    if (topCounts[j] != 0) {
                        System.out.print(topCounts[j] + "\t");
                    }
                } else {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }

    private static void printTopCharacters(char[] topChars) {
        for (int i = 0; i < 10; i++) {
            System.out.print(topChars[i] + "\t");
        }
    }
}