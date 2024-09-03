public class Program {
    public static void main(String[] args) {
        TransactionsService service = new TransactionsService();

        User user1 = new User("Alice", 1000);
        User user2 = new User("Bob", 500);

        service.addUser(user1);
        service.addUser(user2);

        System.out.println("User 1 Balance: " + service.getUserBalance(user1.getId()));
        System.out.println("User 2 Balance: " + service.getUserBalance(user2.getId()));

        try {
            service.performTransferTransaction(user1.getId(), user2.getId(), 200);
            System.out.println("Transaction performed successfully.");
        } catch (UserNotFoundException | IllegalTransactionException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("User 1 Balance after transaction: " + service.getUserBalance(user1.getId()));
        System.out.println("User 2 Balance after transaction: " + service.getUserBalance(user2.getId()));

        Transaction[] user1Transactions = service.getUserTransactions(user1.getId());
        System.out.println("User 1 Transactions:");
        for (Transaction t : user1Transactions) {
            System.out.println(t);
        }

        Transaction[] user2Transactions = service.getUserTransactions(user2.getId());
        System.out.println("User 2 Transactions:");
        for (Transaction t : user2Transactions) {
            System.out.println(t);
        }

        try {
            service.removeTransactionForUser(user1.getId(), user1Transactions[0].getId());
            System.out.println("Transaction removed successfully from User 1.");
        } catch (UserNotFoundException | TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        Transaction[] unpairedTransactions = service.validateTransactions();
        System.out.println("Unpaired Transactions:");
        for (Transaction t : unpairedTransactions) {
            System.out.println(t);
        }
    }
}