import java.util.Scanner;

public class Program {

    public static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int sumOfDigits(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;

        while (true) {
            int query = scanner.nextInt();
            if (query == 42) {
                break;
            }
            if (isPrime(sumOfDigits(query))) {
                count++;
            }
        }

        System.out.println("Count of coffee-request – " + count);
        scanner.close();
    }
}