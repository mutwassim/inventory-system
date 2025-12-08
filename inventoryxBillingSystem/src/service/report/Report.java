package service.report;

/**
 * [PATTERN: STRATEGY] or Command (lightweight), usually part of Abstract
 * Factory products.
 */
public interface Report {
    String generate(String dataPath);
}
