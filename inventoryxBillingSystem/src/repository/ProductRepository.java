package repository;

import model.Product;
import java.util.Map;
import java.util.List;

public class ProductRepository extends FileRepository<Product> {

    public ProductRepository(String businessPath) {
        super(businessPath + "/inventory.json", Product.class);
    }

    @Override
    protected Product mapObject(Object obj) {
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            String id = (String) map.get("id");
            String name = (String) map.get("name");
            // Handle number types coming from simple JSON parsing (might be Double or
            // Integer)
            double price = 0.0;
            Object priceObj = map.get("price");
            if (priceObj instanceof Number) {
                price = ((Number) priceObj).doubleValue();
            }

            int stock = 0;
            Object stockObj = map.get("stockQuantity");
            if (stockObj instanceof Number) {
                stock = ((Number) stockObj).intValue();
            }

            String catName = (String) map.get("categoryName");
            // If categoryName is missing, check recursive for map key "category" -> "name"
            // logic if we stored full obj
            // But simple implementation suggests we reconstruct.
            if (catName == null && map.containsKey("category")) {
                // handle legacy or nested structure if we decided to store full category object
            }

            return new Product(id, name, price, stock, catName != null ? catName : "General");
        }
        return null;
    }

    @Override
    public void update(Product item) {
        List<Product> products = findAll();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(item.getId())) {
                products.set(i, item);
                saveAll(products);
                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        List<Product> products = findAll();
        products.removeIf(p -> p.getId().equals(id));
        saveAll(products);
    }

    @Override
    public Product findById(String id) {
        List<Product> products = findAll();
        for (Product p : products) {
            if (p.getId().equals(id))
                return p;
        }
        return null;
    }
}
