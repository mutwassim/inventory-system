package service.report;

/**
 * [PATTERN: ABSTRACT FACTORY]
 * Reason: To create families of reports (Text vs PDF) without specifying
 * concrete classes.
 */
public interface ReportFactory {
    Report createSalesSummaryReport();

    Report createLowStockReport();
}
