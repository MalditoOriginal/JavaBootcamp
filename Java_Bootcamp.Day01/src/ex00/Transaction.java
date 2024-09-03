import java.util.UUID;

public class Transaction {
    private final String id;
    private final User recipient;
    private final User sender;
    private final TransferCategory category;
    private final int amount;

    public enum TransferCategory {
        DEBIT, CREDIT
    }

    public Transaction(User sender, User recipient, TransferCategory category, int amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.recipient = recipient;
        this.category = category;
        if ((category == TransferCategory.DEBIT && amount <= 0) || (category == TransferCategory.CREDIT && amount >= 0)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public User getRecipient() {
        return recipient;
    }

    public User getSender() {
        return sender;
    }

    public TransferCategory getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public void execute() {
        if (category == TransferCategory.DEBIT) {
            sender.adjustBalance(-amount);
            recipient.adjustBalance(amount);
        } else if (category == TransferCategory.CREDIT) {
            recipient.adjustBalance(amount);
            sender.adjustBalance(-amount);
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", recipient=" + recipient.getName() +
                ", sender=" + sender.getName() +
                ", category=" + category +
                ", amount=" + amount +
                '}';
    }
}