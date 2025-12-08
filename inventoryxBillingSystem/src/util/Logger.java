package util;

/**
 * [PATTERN: SINGLETON]
 * Reason: We need a single global access point for logging to ensure
 * all logs go to the same stream/file and management is centralized.
 */
public class Logger {
    
    private static Logger instance;
    private ILogger loggerBackend;

    private Logger() {
        // Default to console if not configured, or a default file
        // Here we use our Adapter by default to log to a file
        this.loggerBackend = new LoggerAdapter("data/system.log");
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void info(String message) {
        System.out.println("[CONSOLE INFO]: " + message);
        loggerBackend.log(message);
    }

    public void error(String message) {
        System.err.println("[CONSOLE ERROR]: " + message);
        loggerBackend.error(message);
    }
    
    public void setLoggerBackend(ILogger loggerBackend) {
        this.loggerBackend = loggerBackend;
    }
}
