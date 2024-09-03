import java.util.Scanner;
import java.util.UUID;

public class Menu {
    private TransactionsService transactionsService;
    private Scanner scanner;

    public Menu() {
        transactionsService = new TransactionsService();
        scanner = new Scanner(System.in);
    }

    public void start(boolean isDev) {
        printMenu(isDev);
        System.out.print("-> ");

        if(isDev) {
            userDevActions();
        } else {
            userActions();
        }
    }

    private void printMenu(boolean isDev) {
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");

        if(isDev) {
            System.out.println("5. DEV – remove a transfer by ID");
            System.out.println("6. DEV – check transfer validity");
            System.out.println("7. Finish execution");
        } else {
            System.out.println("5. Finish execution");
        }
    }

    private void userDevActions() {
        int input = scanner.nextInt();
        switch (input) {
            case 1:
                addUser(true);
                break;
            case 2:
                userBalance(true);
                break;
            case 3:
                createTransfer(true);
                break;
            case 4:
                getTransactions(true);
                break;
            case 5:
                removeTransferById(true);
                break;
            case 6:
                checkTransfer(true);
                break;
            case 7:
                System.exit(0);
                break;
            default:
                System.err.println("Try again but with correct statement");
                System.out.println("---------------------------------------------------------");
                start(true);
        }

    }

    private void userActions() {
        int input = scanner.nextInt();
        switch (input) {
            case 1:
                addUser(false);
                break;
            case 2:
                userBalance(false);
                break;
            case 3:
                createTransfer(false);
                break;
            case 4:
                getTransactions(false);
                break;
            case 5:
                System.exit(0);
                break;
            default:
                System.err.println("Try again but with correct statement");
                System.out.println("---------------------------------------------------------");
                start(false);
        }

    }

    private void addUser(boolean dev) {
        try {
            System.out.println("Enter a user name and a balance, example: \"Fleta 3000\"");

            String name = scanner.next();
            Integer balance = scanner.nextInt();

            User newUser = new User(name, balance);
            transactionsService.addUser(newUser);

            System.out.printf("User with id = %d is added\n", newUser.getIdentifier());
            System.out.println("---------------------------------------------------------");

            start(dev);

        } catch (RuntimeException rex) {
            System.out.println("An error occurred: " + rex.getMessage());
            System.out.println("Please try again.");
            addUser(dev);
        }
    }


    private void userBalance(boolean dev) {
        try {
            System.out.println("Enter a user ID");

            Integer id = scanner.nextInt();
            User user = transactionsService.getUserList().getUserById(id);

            System.out.printf("%s - %d\n", user.getName(), user.getBalance());
            System.out.println("---------------------------------------------------------");

            start(dev);

        } catch (RuntimeException rex) {
            System.out.println(rex);
            start(dev);
        }
    }


    private void createTransfer(boolean dev) {
        try {
            System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");

            Integer senderId = scanner.nextInt();
            Integer recipientId = scanner.nextInt();
            Integer transferAmount = scanner.nextInt();

            transactionsService.executeTransaction(senderId, recipientId, transferAmount);

            System.out.println("The transfer is completed");
            System.out.println("---------------------------------------------------------");

            start(dev);

        } catch (RuntimeException rex) {
            System.out.println(rex);
            start(dev);
        }
    }


    private void getTransactions(boolean dev) {
        try {
            System.out.println("Enter a user ID");

            Integer userId = scanner.nextInt();
            User user = transactionsService.getUserList().getUserById(userId);

            Transaction[] transactions = transactionsService.getUserTransactions(user);

            if (transactions == null || transactions.length == 0) {
                System.out.println("No transactions found for this user.");
            } else {
                for (Transaction trans : transactions) {
                    System.out.printf(
                            "To %s(id = %d) %d with id = %s\n",
                            user.getName(),
                            user.getIdentifier(),
                            trans.getTransferAmount(),
                            trans.getIdentifier()
                    );
                }
            }

            System.out.println("---------------------------------------------------------");

            start(dev);

        } catch (RuntimeException rex) {
            System.out.println(rex);
            start(dev);
        }
    }

    private void removeTransferById(boolean dev) {
        try {
            System.out.println("Enter a user ID and a transfer ID");

            Integer userId = scanner.nextInt();
            String transId = scanner.next();
            UUID transferId = UUID.fromString(transId);

            User user = transactionsService.getUserList().getUserById(userId);

            int amount = 0;

            for (Transaction trans : transactionsService.getUserTransactions(user)) {
                if (trans.getIdentifier().equals(transferId)) {
                    amount = trans.getTransferAmount();
                    break;
                }
            }

            transactionsService.removeTransaction(transferId, userId);

            System.out.printf(
                    "Transfer To %s(id = %d) %d removed\n",
                    user.getName(),
                    user.getIdentifier(),
                    amount
            );
            System.out.println("---------------------------------------------------------");

            start(dev);

        } catch (RuntimeException rex) {
            System.out.println(rex);
            start(dev);
        }
    }

    private void checkTransfer(boolean dev) {
        try {
            Transaction[] transactions = transactionsService.getUnpairedTransactions();

            if (transactions == null || transactions.length == 0) {
                System.out.println("No unacknowledged transfers found.");
            } else {
                for (Transaction transaction : transactions) {
                    System.out.printf(
                            "%s(id = %d) has an unacknowledged transfer id = %s " +
                                    "from %s(id = %d) for %d\n",
                            transaction.getRecipient().getName(),
                            transaction.getRecipient().getIdentifier(),
                            transaction.getIdentifier(),
                            transaction.getSender().getName(),
                            transaction.getSender().getIdentifier(),
                            transaction.getTransferAmount()
                    );
                    System.out.println("---------------------------------------------------------");
                }
            }

            start(dev);

        } catch (RuntimeException rex) {
            System.out.println(rex);
            start(dev);
        }
    }
}