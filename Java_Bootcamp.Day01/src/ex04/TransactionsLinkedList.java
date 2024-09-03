public class TransactionsLinkedList implements TransactionsList {
    private Node head;
    private int size;

    private class Node {
        Transaction transaction;
        Node next;

        Node(Transaction transaction) {
            this.transaction = transaction;
            this.next = null;
        }
    }

    @Override
    public void addTransaction(Transaction transaction) {
        Node newNode = new Node(transaction);
        newNode.next = head;
        head = newNode;
        size++;
    }

    @Override
    public void removeTransactionById(String id) throws TransactionNotFoundException {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current.transaction.getId().equals(id)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return;
            }
            previous = current;
            current = current.next;
        }
        throw new TransactionNotFoundException("Transaction with ID " + id + " not found.");
    }

    @Override
    public Transaction[] toArray() {
        Transaction[] array = new Transaction[size];
        Node current = head;
        int index = 0;

        while (current != null) {
            array[index++] = current.transaction;
            current = current.next;
        }
        return array;
    }
}