import java.io.*;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к файлу с сигнатурами:");
        String signatureFilePath = scanner.nextLine();

        try {
            FileSignatureChecker checker = new FileSignatureChecker(signatureFilePath);
            System.out.println("Введите пути к файлам для проверки (по одному на строку, завершите ввод командой 'exit'):");

            try (PrintWriter writer = new PrintWriter(new FileWriter("result.txt"))) {
                while (true) {
                    String filePath = scanner.nextLine();
                    if (filePath.equalsIgnoreCase("exit")) break;

                    var result = checker.checkSignature(filePath);
                    writer.println(result);
                    System.out.println("PROCESSED");
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлами: " + e.getMessage());
        }
    }
}
