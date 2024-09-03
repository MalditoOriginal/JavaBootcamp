public class User {
    private final Integer id;
    private String name;
    private int balance;

    public User(String name, int initBalance) {
        this.id = UserIdsGenerator.getInstance().generateId();
        this.name = name;
        setBalance(initBalance);
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
