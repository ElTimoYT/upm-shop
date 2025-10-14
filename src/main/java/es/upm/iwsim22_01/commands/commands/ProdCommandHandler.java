package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;

public class ProdCommandHandler implements CommandHandler {

    @Override
    public void runCommand(Iterator<String> tokens) {
        String incorrectUsage = "Incorrect use: prod add|list|update|remove";

        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }

        switch (tokens.next()) {
            case "add" -> addProductCommand(tokens);
            case "list" -> listProductCommand(tokens);
            case "remove" -> removeProductCommand(tokens);
            case "update" -> updateProductCommand(tokens);
            default -> System.out.println(incorrectUsage);
        };
    }

    private void addProductCommand(Iterator<String> tokens) {
        String incorrectUsage = "Incorrect use: prod add <id> \"<name>\" <category> <price>";

        //Id
        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (!Product.checkId(productId)) {
            System.out.println("Invalid id");
            return;
        }

        //Name
        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }
        String productName = tokens.next();
        if (!Product.checkName(productName)) {
            System.out.println("Invalid name");
            return;
        }

        //Category
        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);;
            return;
        }
        Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
        if (productCategory.isEmpty()) {
            System.out.println("Invalid category");
            return;
        }

        //Price
        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }
        OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
        if (!Product.checkPrice(productPrice)) {
            System.out.println("Invalid price");
            return;
        }

        Product product = new Product(
                productId.getAsInt(),
                productName,
                productCategory.get(),
                productPrice.getAsDouble()
        );

        if (ProductManager.getProductManager().addProduct(product)) {
            System.out.println(product);
            System.out.println("prod add: ok");
        } else {
            System.out.println("Unable to add the product");
        }
    }

    private void listProductCommand(Iterator<String> tokens) {
        System.out.println("Catalog:");
        ProductManager.getProductManager().getProducts().forEach(p -> {
            System.out.println("\t" + p);
        });

        System.out.println("prod list: ok");
    }

    private void updateProductCommand(Iterator<String> tokens) {
        String incorrectUsage = "Incorrect use: prod update <id> name|category|price <value>";

        //Id
        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println("Invalid id");
            return;
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            System.out.println("Product not found");
            return;
        }
        Product product = optionalProduct.get();

        //Parametro a cambiar
        if (!tokens.hasNext()) {
            System.out.println(incorrectUsage);
            return;
        }
        switch (tokens.next()) {
            case "name" -> {
                if (!tokens.hasNext()) {
                    System.out.println(incorrectUsage);
                    return;
                }
                String productName = tokens.next();
                if (!Product.checkName(productName)) {
                    System.out.println("Invalid name");
                    return;
                }

                product.setName(productName);
                System.out.println(product);
                System.out.println("prod update: ok");
            }
            case "category" -> {
                if (!tokens.hasNext()) {
                    System.out.println(incorrectUsage);;
                }
                Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
                if (productCategory.isEmpty()) {
                    System.out.println("Invalid category");
                    return;
                }

                product.setCategory(productCategory.get());
                System.out.println(product);
                System.out.println("prod update: ok");
            }
            case "price" -> {
                if (!tokens.hasNext()) {
                    System.out.println(incorrectUsage);;
                }
                OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
                if (!Product.checkPrice(productPrice)) {
                    System.out.println("Invalid price");
                    return;
                }

                product.setPrice(productPrice.getAsDouble());
                System.out.println(product);
                System.out.println("prod update: ok");
            }
            default -> {
                System.out.println(incorrectUsage);;
            }
        }
    }

    private void removeProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            System.out.println("Incorrect use: prod remove <id>");
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println("Invalid id");
            return;
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            System.out.println("Product not found");
            return;
        }
        Product product = optionalProduct.get();

        ProductManager.getProductManager().removeProduct(product.getId());
        System.out.println(product);
        System.out.println("prod remove: ok");
    }
}
