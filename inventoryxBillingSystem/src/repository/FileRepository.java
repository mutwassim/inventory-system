package repository;

import util.JSONHelper;
import java.util.ArrayList;
import java.util.List;

import util.Logger;

/**
 * [PATTERN: TEMPLATE METHOD] (Implicit in how methods are structured if
 * subclasses follow strict flow,
 * but primarily a base implementation for Bridge/Adapter concepts).
 * Handles reading/writing lists of objects to JSON files.
 */
public abstract class FileRepository<T> implements IRepository<T> {
    protected String filePath;
    protected Class<T> type;
    protected FileStore fileStore;

    public FileRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
        this.fileStore = FileStore.getInstance();
    }

    protected List<T> loadAll() {
        String json = fileStore.read(filePath);
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        // Basic JSON array parsing via Helper
        // Since our JSONHelper is simple, we might need custom logic per type if
        // generic parsing is weak.
        // For now, we assume simple list parsing fits our demo needs or we cast
        // results.
        Object parsed = JSONHelper.parse(json);
        List<T> results = new ArrayList<>();
        if (parsed instanceof List) {
            List<?> list = (List<?>) parsed;
            for (Object obj : list) {
                // In a real robust system, we'd map Map<String,Object> back to T via
                // Reflection/Gson.
                // For this exercise, assume we handle mapping in concrete classes or use a
                // simple mapper.
                results.add(mapObject(obj));
            }
        }
        return results;
    }

    protected void saveAll(List<T> items) {
        String json = JSONHelper.toJson(items);
        fileStore.writeResult(filePath, json);
    }

    // Abstract method to let subclasses handle mapping from Map back to Object T
    protected abstract T mapObject(Object obj);

    @Override
    public List<T> findAll() {
        return loadAll();
    }

    @Override
    public void save(T item) {
        List<T> all = loadAll();
        // Check duplication logic or ID logic in subclasses usually,
        // but here we just append for simple cases or overwrite in concrete.
        all.add(item);
        saveAll(all);
        Logger.getInstance().info("Saved " + type.getSimpleName() + " to " + filePath);
    }
}
