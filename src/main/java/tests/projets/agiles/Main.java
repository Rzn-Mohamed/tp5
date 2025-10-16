package tests.projets.agiles;

import dao.IDao;
import entities.Product;
import metier.ProductDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.HibernateConfig;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Spring Hibernate Demo Application ===");

        ApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        IDao<Product> productDao = context.getBean(ProductDaoImpl.class);
        Scanner scanner = new Scanner(System.in);

        try {
            boolean running = true;
            while (running) {
                System.out.println("\n--- Product Management Menu ---");
                System.out.println("1. Create Product");
                System.out.println("2. List All Products");
                System.out.println("3. Find Product by ID");
                System.out.println("4. Update Product");
                System.out.println("5. Delete Product");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        createProduct(productDao, scanner);
                        break;
                    case 2:
                        listAllProducts(productDao);
                        break;
                    case 3:
                        findProductById(productDao, scanner);
                        break;
                    case 4:
                        updateProduct(productDao, scanner);
                        break;
                    case 5:
                        deleteProduct(productDao, scanner);
                        break;
                    case 6:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            ((AnnotationConfigApplicationContext) context).close();
        }
    }

    private static void createProduct(IDao<Product> productDao, Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Product product = new Product(name, price);
        if (productDao.create(product)) {
            System.out.println("Product created successfully: " + product);
        } else {
            System.out.println("Failed to create product.");
        }
    }

    private static void listAllProducts(IDao<Product> productDao) {
        System.out.println("\n--- All Products ---");
        List<Product> products = productDao.findAll();
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
                System.out.println(p);
            }
        } else {
            System.out.println("No products found.");
        }
    }

    private static void findProductById(IDao<Product> productDao, Scanner scanner) {
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Product product = productDao.findById(id);
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product not found with ID: " + id);
        }
    }

    private static void updateProduct(IDao<Product> productDao, Scanner scanner) {
        System.out.print("Enter product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Product product = productDao.findById(id);
        if (product != null) {
            System.out.println("Current product: " + product);
            System.out.print("Enter new name (or press Enter to keep current): ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                product.setName(name);
            }

            System.out.print("Enter new price (or -1 to keep current): ");
            double price = scanner.nextDouble();
            scanner.nextLine();
            if (price >= 0) {
                product.setPrice(price);
            }

            if (productDao.update(product)) {
                System.out.println("Product updated successfully: " + product);
            } else {
                System.out.println("Failed to update product.");
            }
        } else {
            System.out.println("Product not found with ID: " + id);
        }
    }

    private static void deleteProduct(IDao<Product> productDao, Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Product product = productDao.findById(id);
        if (product != null) {
            System.out.println("Product to delete: " + product);
            System.out.print("Are you sure? (y/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                if (productDao.delete(product)) {
                    System.out.println("Product deleted successfully.");
                } else {
                    System.out.println("Failed to delete product.");
                }
            } else {
                System.out.println("Delete operation cancelled.");
            }
        } else {
            System.out.println("Product not found with ID: " + id);
        }
    }
}