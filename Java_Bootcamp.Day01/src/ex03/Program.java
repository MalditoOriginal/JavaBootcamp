public class Program {
    public static void main(String[] args) {
        User user1 = new User("Alice", 1000);
        User user2 = new User("Bob", 500);

        Transaction transaction1 = new Transaction("Alice", "Bob", "transfer", 200);
        Transaction transaction2 = new Transaction("Bob", "Alice", "transfer", 100);

        user1.addTransaction(transaction1);
        user2.addTransaction(transaction2);

        System.out.println("User 1 Transactions:");
        for (Transaction t : user1.getTransactions()) {
            System.out.println(t);
        }

        System.out.println("User 2 Transactions:");
        for (Transaction t : user2.getTransactions()) {
            System.out.println(t);
        }

        try {
            user1.removeTransactionById(transaction1.getId());
            System.out.println("Transaction removed successfully from User 1.");
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (user1.getTransactions().length > 0) {
            System.out.println("User 1 Transactions after removal:");
            for (Transaction t : user1.getTransactions()) {
                System.out.println(t);
            }
        }

        try {
            user2.removeTransactionById("non-existent-id");
            System.out.println("Transaction removed successfully from User 2.");
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (user2.getTransactions().length > 0) {
            System.out.println("User 2 Transactions after removal attempt:");
            for (Transaction t : user2.getTransactions()) {
                System.out.println(t);
            }
        }
    }
}