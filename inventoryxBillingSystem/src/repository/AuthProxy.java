package repository;

import model.User;
import util.Logger;
import java.util.List;

/**
 * [PATTERN: PROXY]
 * Reason: Controls access to the UserRepository. Only Admins should be able to
 * delete users or view all.
 */
public class AuthProxy implements IUserRepository {

    private UserRepository realRepo;
    private User currentUser;

    public AuthProxy(UserRepository realRepo, User currentUser) {
        this.realRepo = realRepo;
        this.currentUser = currentUser;
    }

    @Override
    public void save(User item) {
        if (isAdmin()) {
            realRepo.save(item);
        } else {
            logAccessDenied("save user");
        }
    }

    @Override
    public void update(User item) {
        if (isAdmin() || isSelf(item)) {
            realRepo.update(item);
        } else {
            logAccessDenied("update user");
        }
    }

    @Override
    public void delete(String id) {
        if (isAdmin()) {
            realRepo.delete(id);
        } else {
            logAccessDenied("delete user");
        }
    }

    @Override
    public User findById(String id) {
        // Anyone can find? Or maybe restrictions. Let's say safe.
        return realRepo.findById(id);
    }

    @Override
    public List<User> findAll() {
        if (isAdmin()) {
            return realRepo.findAll();
        } else {
            logAccessDenied("view all users");
            return List.of();
        }
    }

    @Override
    public User findByUsername(String username) {
        // Assuming finding by username is allowed for login purposes or similar checks
        // We can delegate this to the realRepo directly.
        return realRepo.findByUsername(username);
    }

    private boolean isAdmin() {
        return currentUser != null && currentUser.getRole().equalsIgnoreCase("ADMIN");
    }

    private boolean isSelf(User target) {
        return currentUser != null && currentUser.getUsername().equals(target.getUsername());
    }

    private void logAccessDenied(String action) {
        Logger.getInstance()
                .error("Access Denied: User " + (currentUser != null ? currentUser.getUsername() : "Unknown")
                        + " attempted to " + action);
    }
}
