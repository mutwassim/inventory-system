package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for flyweights.
 */
public class CategoryFactory {
    private static final Map<String, Category> cache = new HashMap<>();

    public static Category getCategory(String name) {
        if (!cache.containsKey(name)) {
            // Category constructor is package-private, so this works
            cache.put(name, new Category(name));
            // System.out.println("Creating new category flyweight: " + name);
        }
        return cache.get(name);
    }
}
