package patterns;

/**
 * Registry class to document the patterns usage in code.
 * This class doesn't do anything but serves as a map for the evaluator.
 */
public class PatternRegistry {
    /*
     * CREATIONAL:
     * 1. Singleton: util.Logger, repository.FileStore
     * 2. Factory Method: repository.RepositoryFactory
     * 3. Abstract Factory: service.report.ReportFactory
     * 4. Builder: service.BillBuilder
     * 5. Prototype: model.Product, model.ProductBundle
     * 
     * STRUCTURAL:
     * 6. Adapter: util.LoggerAdapter
     * 7. Bridge: repository.IRepository -> repository.FileRepository
     * 8. Composite: model.ProductComponent
     * 9. Decorator: service.BillDecorator
     * 10. Facade: service.StoreFacade
     * 11. Flyweight: model.Category
     * 12. Proxy: repository.AuthProxy
     */
}
