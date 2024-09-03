public class UserIdsGenerator {
    private static UserIdsGenerator instance;
    private int currentId;

    private UserIdsGenerator() {
        currentId = 0;
    }

    public static synchronized UserIdsGenerator getInstance() {
        if (instance == null) {
            instance = new UserIdsGenerator();
        }
        return instance;
    }

    public int generateId() {
        return ++currentId;
    }
}
