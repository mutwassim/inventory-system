package repository;

/**
 * [PATTERN: FACTORY METHOD]
 * Reason: Encapsulate the creation logic of different repositories. 
 * Allows the client to request a repository without knowing the implementation details (file paths, etc).
 */
public class RepositoryFactory {

    public static IRepository<?> getRepository(String type, String businessPath) {
        if (type.equalsIgnoreCase("USER")) {
            return new UserRepository(businessPath);
        } else if (type.equalsIgnoreCase("PRODUCT")) {
            return new ProductRepository(businessPath);
        }
        // Future extensions
        return null;
    }
}
