package repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * [PATTERN: SINGLETON]
 * Reason: Only one FileManager should exist to avoid conflicting reads/writes.
 */
public class FileStore {

    private static FileStore instance;

    private FileStore() {
        // Private constructor
    }

    public static synchronized FileStore getInstance() {
        if (instance == null) {
            instance = new FileStore();
        }
        return instance;
    }

    public void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void writeResult(String path, String content) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (FileWriter query = new FileWriter(file)) {
                query.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String path) {
        try {
            if (!new File(path).exists()) return "";
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public boolean exists(String path) {
        return new File(path).exists();
    }
}
