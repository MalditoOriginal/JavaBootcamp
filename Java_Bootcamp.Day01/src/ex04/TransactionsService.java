import java.util.UUID;

public class TransactionsService {
    private UsersList usersList;

    public TransactionsService() {
        this.usersList = new UsersArrayList();
    }

    public void addUser(User user) {
        usersList.addUser(user);
    }

    public int getUserBalance(int userId) throws UserNotFoundException {
        User user = usersList.getUserById(userId);
        return user.getBalance();
    }

    public void performTransferTransaction(int senderId, int recipientId, int amount) throws UserNotFoundException, IllegalTransactionException {
        User sender = usersList.getUserById(senderId);
        User recipient = usersList.getUserById(recipientId);

        if (sender.getBalance() < amount) {
            throw new IllegalTransactionException("Insufficient funds for the transaction.");
        }

        String transactionId = UUID.randomUUID().toString();

        Transaction debitTransaction = new Transaction(transactionId, recipient.getName(), sender.getName(), "DEBIT", amount);
        Transaction creditTransaction = new Transaction(transactionId, recipient.getName(), sender.getName(), "CREDIT", -amount);

        sender.addTransaction(creditTransaction);
        recipient.addTransaction(debitTransaction);

        sender.adjustBalance(-amount);
        recipient.adjustBalance(amount);
    }

    public Transaction[] getUserTransactions(int userId) throws UserNotFoundException {
        User user = usersList.getUserById(userId);
        return user.getTransactions();
    }

    public void removeTransactionForUser(int userId, String transactionId) throws UserNotFoundException, TransactionNotFoundException {
        User user = usersList.getUserById(userId);
        user.removeTransactionById(transactionId);
    }

    public Transaction[] validateTransactions() {
        TransactionsList unpairedTransactions = new TransactionsLinkedList();

        for (int i = 0; i < usersList.getNumberOfUsers(); i++) {
            User user = usersList.getUserByIndex(i);
            Transaction[] transactions = user.getTransactions();

            for (Transaction transaction : transactions) {
                boolean isPaired = false;

                for (int j = 0; j < usersList.getNumberOfUsers(); j++) {
                    User otherUser = usersList.getUserByIndex(j);
                    if (otherUser.getId() != user.getId()) {
                        Transaction[] otherTransactions = otherUser.getTransactions();
                        for (Transaction otherTransaction : otherTransactions) {
                            if (otherTransaction.getId().equals(transaction.getId())) {
                                isPaired = true;
                                break;
                            }
                        }
                    }
                    if (isPaired) break;
                }

                if (!isPaired) {
                    unpairedTransactions.addTransaction(transaction);
                }
            }
        }

        return unpairedTransactions.toArray();
    }
}