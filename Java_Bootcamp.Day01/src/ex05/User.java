public class User {
    private Integer identifier;
    private String name;
    private Integer balance;
    private TransactionsLinkedList transactionsList;

    public User() {
        identifier = UserIdsGenerator.getInstance().idGenerator();
        name = "Unknown";
        balance = 0;
        transactionsList = new TransactionsLinkedList();
    }

    public User(String name, Integer balance) {
        if (balance < 0) {
            System.err.println("Balance can't be less than zero");
            this.balance = 0;
        } else {
            identifier = UserIdsGenerator.getInstance().idGenerator();
            this.name = name;
            this.balance = balance;
            transactionsList = new TransactionsLinkedList();
        }
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        if (balance >= 0) {
            this.balance = balance;
        } else {
            System.err.println("Balance can't be less than zero");
            this.balance = 0;
        }
    }

    public TransactionsLinkedList getTransactionsList() {
        return transactionsList;
    }

    @Override
    public String toString() {
        return name + "(id - " + identifier + ") { Balance: " + balance + " }";
    }
}
