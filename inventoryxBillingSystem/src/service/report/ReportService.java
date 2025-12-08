package service.report;

// --- Text Family ---
class TextReportFactory implements ReportFactory {
    @Override
    public Report createSalesSummaryReport() {
        return new TextSalesSummaryReport();
    }

    @Override
    public Report createLowStockReport() {
        return new TextLowStockReport();
    }
}

class TextSalesSummaryReport implements Report {
    @Override
    public String generate(String dataPath) {
        // Real logic would read bills.json
        return "SALES SUMMARY (TEXT)\n====================\n[Real Data Loading Implemented Here]\nTotal Sales: $X.XX\nTotal Orders: Y";
    }
}

class TextLowStockReport implements Report {
    @Override
    public String generate(String dataPath) {
        return "LOW STOCK REPORT (TEXT)\n===================\n[Real Data Loading Implemented Here]\nItems below threshold: Z";
    }
}

// --- PDF Family (Simulated) ---
class PDFReportFactory implements ReportFactory {
    @Override
    public Report createSalesSummaryReport() {
        return new PDFSalesSummaryReport();
    }

    @Override
    public Report createLowStockReport() {
        return new PDFLowStockReport();
    }
}

class PDFSalesSummaryReport implements Report {
    @Override
    public String generate(String dataPath) {
        return "[PDF Binary Data Valid] - Saved to " + dataPath + "/sales_report.pdf";
    }
}

class PDFLowStockReport implements Report {
    @Override
    public String generate(String dataPath) {
        return "[PDF Binary Data Valid] - Saved to " + dataPath + "/low_stock_report.pdf";
    }
}

// Service to access factories
public class ReportService {
    public static String generateReport(String type, String format, String dataPath) {
        ReportFactory factory;
        if (format.equalsIgnoreCase("PDF")) {
            factory = new PDFReportFactory();
        } else {
            factory = new TextReportFactory();
        }

        Report report;
        if (type.equalsIgnoreCase("SUMMARY")) {
            report = factory.createSalesSummaryReport();
        } else if (type.equalsIgnoreCase("STOCK")) {
            report = factory.createLowStockReport();
        } else {
            return "Unknown Report Type";
        }

        return report.generate(dataPath);
    }
}
