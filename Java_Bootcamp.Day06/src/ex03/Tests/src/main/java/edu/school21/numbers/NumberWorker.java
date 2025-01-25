package edu.school21.numbers;

public class NumberWorker {

    public boolean isPrime(int number) {
        if (number <= 1) {
            throw new IllegalNumberException("Number must be greater than 1");
        }
        if (number == 2 || number == 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    public int digitsSum(int number) {
        int sum = 0;
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    public static class IllegalNumberException extends RuntimeException {
        public IllegalNumberException(String message) {
            super(message);
        }
    }
}