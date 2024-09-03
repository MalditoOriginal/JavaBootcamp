import java.util.*;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        scanner.close();

        if (num <= 1) {
            System.out.println("Illegal Argument");
            return;
        }

        int i = 2;
        for (; i*i <= num; i++) {
            if (num % i == 0) {
                System.out.println("false " + (--i));
                return;
            }
        }
        System.out.println("true " + (--i));
    }
}