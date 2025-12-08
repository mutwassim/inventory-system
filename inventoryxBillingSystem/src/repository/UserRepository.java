package repository;

import model.User;
import java.util.Map;
import java.util.List;

public class UserRepository extends FileRepository<User> implements IUserRepository {

    public UserRepository(String businessPath) {
        super(businessPath + "/users.json", User.class);
    }

    @Override
    protected User mapObject(Object obj) {
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            String role = (String) map.get("role");
            return new User(username, password, role);
        }
        return null; // or throw exception
    }

    // [PATTERN: DAO (Data Access Object)]
    // Reason: Providing specific data operations for Users without exposing
    // implementation details.
    public User findByUsername(String username) {
        List<User> users = findAll();
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void update(User item) {
        List<User> users = findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(item.getUsername())) {
                users.set(i, item);
                saveAll(users);
                return;
            }
        }
    }

    @Override
    public void delete(String username) {
        List<User> users = findAll();
        users.removeIf(u -> u.getUsername().equals(username));
        saveAll(users);
    }

    @Override
    public User findById(String id) {
        return findByUsername(id);
    }
}
