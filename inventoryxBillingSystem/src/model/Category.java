package model;

/**
 * [PATTERN: FLYWEIGHT]
 * Reason: Category objects are shared across thousands of products.
 * Storing "Electronics" string repeatedly is wasteful. We share the instance.
 */
public class Category {
    private final String name;

    // Package-private constructor, only Factory creates it
    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
