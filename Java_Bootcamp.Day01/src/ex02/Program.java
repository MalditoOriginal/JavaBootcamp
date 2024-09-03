public class Program {
    public static void main(String[] args) {
        UsersList usersList = new UsersArrayList();

        User user1 = new User("Alice", 1000);
        User user2 = new User("Bob", 500);
        User user3 = new User("Charlie", 300);

        usersList.addUser(user1);
        usersList.addUser(user2);
        usersList.addUser(user3);

        System.out.println("Users list:");
        for (int i = 0; i < usersList.getNumberOfUsers(); i++) {
            System.out.println(usersList.getUserByIndex(i));
        }

        try {
            User user = usersList.getUserById(2);
            System.out.println("\nUser with ID 2: " + user);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            User user = usersList.getUserById(99);
            System.out.println("\nUser with ID 99: " + user);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
