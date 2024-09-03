public class User {
    private final Integer id;
    private String name;
    private int balance;
    private TransactionsList transactions;

    public User(String name, int initBalance) {
        this.id = UserIdsGenerator.getInstance().generateId();
        this.name = name;
        setBalance(initBalance);
        this.transactions = new TransactionsLinkedList();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance can't be negative.");
        }
        this.balance = balance;
    }

    public void adjustBalance(int amount) {
        if (this.balance + amount < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        this.balance += amount;
    }

    public void addTransaction(Transaction transaction) {
        transactions.addTransaction(transaction);
    }

    public void removeTransactionById(String id) throws TransactionNotFoundException {
        transactions.removeTransactionById(id);
    }

    public Transaction[] getTransactions() {
        return transactions.toArray();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}