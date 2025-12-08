package repository;

import java.util.List;

/**
 * [PATTERN: BRIDGE] (Abstraction)
 * Reason: Defines the interface for data access, separated from the implementation (File/DB).
 */
public interface IRepository<T> {
    void save(T item);
    void update(T item);
    void delete(String id);
    T findById(String id);
    List<T> findAll();
}
