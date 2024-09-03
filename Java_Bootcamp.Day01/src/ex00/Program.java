public class Program {
    public static void main(String[] args) {
        User user_1 = new User(1, "Alice", 1000);
        User user_2 = new User(2, "Bob", 500);

        System.out.println("Initial state: ");
        System.out.println(user_2 + "\n" + user_1);

        Transaction transaction = new Transaction(user_1, user_2, Transaction.TransferCategory.DEBIT, 200);

        System.out.println("\nTransaction details: \n" + transaction);

        transaction.execute();

        System.out.println("\nFinal state: " + "\n" + user_1 + "\n" + user_2);
    }
}