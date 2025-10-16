import dao.IDao;
import entities.Product;
import metier.ProductDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.HibernateConfig;

import java.util.List;

public class Presentation2 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);

        // Fix: Use specific implementation class for proper type safety
        IDao<Product> productDao = context.getBean(ProductDaoImpl.class);

        try {
            // Create a new product
            Product product = new Product();
            product.setName("Produit 1");
            product.setPrice(100.0);

            System.out.println("Creating product...");
            if (productDao.create(product)) {
                System.out.println("Product created successfully: " + product.getName());
            }

            // Create another product
            Product product2 = new Product("Produit 2", 250.0);
            if (productDao.create(product2)) {
                System.out.println("Product 2 created successfully: " + product2.getName());
            }

            // Display all products
            System.out.println("\n--- All Products ---");
            List<Product> products = productDao.findAll();
            if (products != null) {
                for (Product p : products) {
                    System.out.println(p);
                }
            }

            // Find by ID example (if products were created)
            if (products != null && !products.isEmpty()) {
                Product foundProduct = productDao.findById(products.get(0).getId());
                if (foundProduct != null) {
                    System.out.println("\nFound product by ID: " + foundProduct);

                    // Update example
                    foundProduct.setPrice(150.0);
                    if (productDao.update(foundProduct)) {
                        System.out.println("Product updated successfully: " + foundProduct);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close context
            ((AnnotationConfigApplicationContext) context).close();
        }
    }
}