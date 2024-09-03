import java.util.UUID;

public class Transaction {
    private final String id;
    private String recipient;
    private String sender;
    private String type;
    private int amount;

    public Transaction(String recipient, String sender, String type, int amount) {
        this.id = UUID.randomUUID().toString();
        this.recipient = recipient;
        this.sender = sender;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}