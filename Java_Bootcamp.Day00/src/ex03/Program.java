import java.util.*;

class WeekNode {
    int weekNumber;
    int minGrade;
    WeekNode next;

    WeekNode(int weekNumber, int minGrade) {
        this.weekNumber = weekNumber;
        this.minGrade = minGrade;
        this.next = null;
    }
}

public class Program {
    private static Scanner scanner = new Scanner(System.in);
    private static WeekNode head = null;
    private static WeekNode tail = null;

    public static void main(String[] args) {
        int currentWeek = 1;

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("42")) {
                break;
            }

            if (!input.equals("Week " + currentWeek)) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }

            int minGrade = readMinGrade();
            addWeekNode(currentWeek, minGrade);
            currentWeek++;

            if (currentWeek > 18) {
                break;
            }
        }

        printGraph();
        scanner.close();
    }

    private static int readMinGrade() {
        int minGrade = 9;
        for (int i = 0; i < 5; i++) {
            int grade = scanner.nextInt();
            if (grade < 1 || grade > 9) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }
            if (grade < minGrade) {
                minGrade = grade;
            }
        }
        scanner.nextLine();
        return minGrade;
    }

    private static void addWeekNode(int weekNumber, int minGrade) {
        WeekNode newNode = new WeekNode(weekNumber, minGrade);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    private static void printGraph() {
        WeekNode current = head;
        while (current != null) {
            System.out.print("Week " + current.weekNumber + " ");
            for (int i = 0; i < current.minGrade; i++) {
                System.out.print("=");
            }
            System.out.println(">");
            current = current.next;
        }
    }
}