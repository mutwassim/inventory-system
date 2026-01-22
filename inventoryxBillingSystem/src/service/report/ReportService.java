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
        String billsPath = dataPath + "/bills.json";
        String json = repository.FileStore.getInstance().read(billsPath);
        if (json == null || json.isEmpty()) {
            return "SALES SUMMARY (TEXT)\n====================\nNo sales records found.";
        }

        Object parsed = util.JSONHelper.parse(json);
        if (!(parsed instanceof java.util.List)) {
            return "SALES SUMMARY (TEXT)\n====================\nError reading sales data.";
        }

        java.util.List<?> list = (java.util.List<?>) parsed;
        double totalSales = 0;
        int count = list.size();

        for (Object o : list) {
            if (o instanceof java.util.Map) {
                java.util.Map<?, ?> m = (java.util.Map<?, ?>) o;
                Object net = m.get("netTotal");
                if (net instanceof Number) {
                    totalSales += ((Number) net).doubleValue();
                }
            }
        }

        return "SALES SUMMARY (TEXT)\n====================\n" +
                "Total Orders: " + count + "\n" +
                "Total Sales: " + String.format("%.2f", totalSales);
    }
}

class TextLowStockReport implements Report {
    @Override
    public String generate(String dataPath) {
        // Use ProductRepository to load inventory
        // dataPath is like "data/storeName"
        // ProductRepository expects this path to append /inventory.json
        // We create a new instance just for reporting
        try {
            repository.ProductRepository repo = new repository.ProductRepository(dataPath);
            java.util.List<model.Product> all = repo.findAll();

            StringBuilder sb = new StringBuilder();
            sb.append("LOW STOCK REPORT (TEXT)\n===================\n");

            boolean found = false;
            for (model.Product p : all) {
                if (p.getStockQuantity() <= 5) {
                    sb.append(String.format("- %s (ID: %s): %d left\n", p.getName(), p.getId(), p.getStockQuantity()));
                    found = true;
                }
            }

            if (!found) {
                sb.append("All items are well stocked.");
            }

            return sb.toString();
        } catch (Exception e) {
            return "Error generating low stock report: " + e.getMessage();
        }
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
