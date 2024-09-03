import java.util.*;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        ScheduleManager scheduleManager = new ScheduleManager();
        AttendanceManager attendanceManager = new AttendanceManager(studentManager, scheduleManager);

        studentManager.readStudents(scanner);
        scheduleManager.readSchedule(scanner);
        attendanceManager.readAttendance(scanner);

        scheduleManager.printSchedule();
        attendanceManager.printAttendance();
    }
}

class StudentManager {
    private String[] students = new String[10];
    private int studentCount = 0;

    public void readStudents(Scanner scanner) {
        while (scanner.hasNext() && studentCount < 10) {
            String name = scanner.next();
            if (name.equals(".")) break;
            if (name.length() < 10) {
                students[studentCount++] = name;
            } else {
                System.err.println("Illegal Argument");
                System.exit(-1);
            }
        }
    }

    public String[] getStudents() {
        return students;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public int findStudentIndex(String name) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }
}

class ScheduleManager {
    private String[][] schedule = new String[7][10];

    public void readSchedule(Scanner scanner) {
        while (scanner.hasNext()) {
            String time = scanner.next();
            if (time.equals(".")) break;
            String date = scanner.next();
            fillSchedule(date, time);
        }
    }

    private void fillSchedule(String date, String time) {
        int dayIndex = getDayIndex(date);
        if (dayIndex != -1) {
            schedule[dayIndex][0] = time;
            schedule[dayIndex][1] = date;
        }
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "MO": return 0;
            case "TU": return 1;
            case "WE": return 2;
            case "TH": return 3;
            case "FR": return 4;
            case "SA": return 5;
            case "SU": return 6;
            default: return -1;
        }
    }

    public String[][] getSchedule() {
        return schedule;
    }

    public void printSchedule() {
        for (int i = 0; i <= 30; i++) {
            printBoard(i);
        }
        System.out.println();
    }

    private void printBoard(int i) {
        if (i == 0) {
            System.out.print("          ");
        }
        int day = ++i % 7;
        for (int j = 0; j < 1 && schedule[day][j] != null; j++) {
            System.out.print(schedule[day][j] + ":00 ");
            System.out.printf("%s %2d|", getDayName(day), i);
        }
    }

    private String getDayName(int day) {
        switch (day) {
            case 0: return "MO";
            case 1: return "TU";
            case 2: return "WE";
            case 3: return "TH";
            case 4: return "FR";
            case 5: return "SA";
            case 6: return "SU";
            default: return "";
        }
    }
}

class AttendanceManager {
    private StudentManager studentManager;
    private ScheduleManager scheduleManager;
    private String[][][][] attendance = new String[10][30][10][1];

    public AttendanceManager(StudentManager studentManager, ScheduleManager scheduleManager) {
        this.studentManager = studentManager;
        this.scheduleManager = scheduleManager;
    }

    public void readAttendance(Scanner scanner) {
        while (scanner.hasNext()) {
            String name = scanner.next();
            if (name.equals(".")) break;
            String time = scanner.next();
            String date = scanner.next();
            String status = scanner.next();
            int week = findWeek(Integer.parseInt(date), time);
            int studentIndex = studentManager.findStudentIndex(name);
            if (studentIndex != -1) {
                attendance[studentIndex][Integer.parseInt(date) - 1][week][0] = status;
            }
        }
    }

    private int findWeek(int date, String time) {
        String[][] schedule = scheduleManager.getSchedule();
        int dayOfWeek = date % 7;
        for (int i = 0; i < schedule[dayOfWeek].length; i++) {
            if (schedule[dayOfWeek][i] != null && schedule[dayOfWeek][i].equals(time)) {
                return i;
            }
        }
        return -1;
    }

    public void printAttendance() {
        String[] students = studentManager.getStudents();
        int studentCount = studentManager.getStudentCount();
        for (int i = 0; i < studentCount; i++) {
            System.out.printf("%10s", students[i]);
            printBoard(attendance[i]);
        }
    }

    private void printBoard(String[][][] attendance) {
        String[][] schedule = scheduleManager.getSchedule();
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 1 && schedule[(i + 1) % 7][j] != null; j++) {
                if (attendance[i][j][0] != null && attendance[i][j][0].equals("HERE")) {
                    System.out.printf("        %2d|", 1);
                } else if (attendance[i][j][0] != null && attendance[i][j][0].equals("NOT_HERE")) {
                    System.out.printf("        %2d|", -1);
                } else {
                    System.out.print("          |");
                }
            }
        }
        System.out.println();
    }
}