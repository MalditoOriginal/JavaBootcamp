public class Program {
    public static void main(String[] args) {
        User user_1 = new User("Alice", 1000);
        User user_2 = new User("Bob", 500);
        User user_3 = new User("Charlie", 300);

        System.out.println("Users:\n" + user_1 + '\n' + user_2 + '\n' + user_3);
    }
}
