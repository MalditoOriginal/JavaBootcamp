import java.util.*;
import java.util.UUID;

public class Transaction {
    private UUID identifier;
    private User recipient;
    private User sender;
    private Integer transferAmount;
    private Category transferCategory;

    public enum Category {
        DEBIT, CREDIT
    }

    public Transaction(User sender, User recipient, Integer transferAmount, Category transferCategory) {
        if (transferAmount < 0 && transferCategory == Category.DEBIT) {
            System.err.println("Wrong transaction");
        } else if ((sender.getBalance() < transferAmount && transferCategory == Category.DEBIT) ||
                (sender.getBalance() < -transferAmount && transferCategory == Category.CREDIT)) {
            System.err.println("Insufficient funds in the account for user " + sender.getName());
        } else {
            this.identifier = UUID.randomUUID();
            this.recipient = recipient;
            this.sender = sender;
            this.transferAmount = transferAmount;
            this.transferCategory = transferCategory;
        }
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Integer getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Category getTransferCategory() {
        return transferCategory;
    }

    public void setTransferCategory(Category transferCategory) {
        this.transferCategory = transferCategory;
    }

    @Override
    public String toString() {
        return "Transaction(" + identifier + ")" +
                "{" +
                sender +
                " ==>(" +
                transferAmount +
                ") " +
                recipient +
                " with category - " +
                transferCategory +
                "}";
    }
}
