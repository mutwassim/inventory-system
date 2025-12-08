package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * [PATTERN: ADAPTER]
 * Reason: We have a legacy or third-party style logging interface
 * (ModernLogger)
 * that we want to adapt to our system's expected logger interface (ILogger).
 */

// Target Interface

// Adaptee (The class we want to use but has different interface)
class FileLoggerSystem {
    private String filename;

    public FileLoggerSystem(String filename) {
        this.filename = filename;
    }

    public void writeToFile(String msg) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename, true))) {
            out.println("[FILE] " + LocalDateTime.now() + ": " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Adapter
public class LoggerAdapter implements ILogger {
    private FileLoggerSystem fileLogger;

    public LoggerAdapter(String filename) {
        this.fileLogger = new FileLoggerSystem(filename);
    }

    @Override
    public void log(String message) {
        // [PATTERN: ADAPTER]
        // Reason: Adapting generic log call to specific file write implementation
        fileLogger.writeToFile("INFO: " + message);
    }

    @Override
    public void error(String message) {
        fileLogger.writeToFile("ERROR: " + message);
    }
}
