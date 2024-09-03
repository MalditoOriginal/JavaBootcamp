public class UsersArrayList implements UsersList {
    private User[] users;
    private int size;
    private int capacity;

    public UsersArrayList() {
        this.capacity = 10;
        this.users = new User[capacity];
        this.size = 0;
    }

    @Override
    public void addUser(User user) {
        if (size == capacity) {
            increaseCapacity();
        }
        users[size++] = user;
    }

    @Override
    public User getUserById(int id) {
        for (int i = 0; i < size; i++) {
            if (users[i].getId().equals(id)) {
                return users[i];
            }
        }
        throw new UserNotFoundException("User with ID " + id + " not found.");
    }

    @Override
    public User getUserByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        return users[index];
    }

    @Override
    public int getNumberOfUsers() {
        return size;
    }

    private void increaseCapacity() {
        capacity = capacity + capacity / 2;
        User[] newUsers = new User[capacity];
        System.arraycopy(users, 0, newUsers, 0, size);
        users = newUsers;
    }
}
