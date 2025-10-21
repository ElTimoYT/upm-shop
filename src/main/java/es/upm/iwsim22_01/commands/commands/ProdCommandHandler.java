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
    private static final String ADD_SUBCOMMAND = "add",
        LIST_SUBCOMMAND = "list",
        REMOVE_SUBCOMMAND = "remove",
        UPDATE_SUBCOMMAND = "update",

        ERROR_INCORRECT_USE = "Incorrect use: prod add|list|update|remove",
        ERROR_INCORRECT_USE_ADD = "Incorrect use: prod add <id> \"<name>\" <category> <price>",
        ERROR_INCORRECT_USE_UPDATE = "Incorrect use: prod update <id> name|category|price <value>",
        ERROR_INCORRECT_USE_REMOVE = "Incorrect use: prod remove <id>",
        ERROR_INVALID_ID = "Invalid id",
        ERROR_INVALID_NAME = "Invalid name",
        ERROR_INVALID_CATEGORY = "Invalid category",
        ERROR_INVALID_PRICE = "Invalid price",
        ERROR_PRODUCT_NOT_FOUND = "Product not found",
        ERROR_UNABLE_ADD_PRODUCT = "Unable to add the product",

        PROD_ADD_OK = "prod add: ok",
        CATALOG = "Catalog:",
        PROD_LIST_OK = "prod list: ok",
        PROD_UPDATE_OK = "prod update: ok",
        PROD_REMOVE_OK = "prod remove: ok";

    @Override
    public void runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        switch (tokens.next()) {
            case ADD_SUBCOMMAND -> addProductCommand(tokens);
            case LIST_SUBCOMMAND -> listProductCommand(tokens);
            case REMOVE_SUBCOMMAND -> removeProductCommand(tokens);
            case UPDATE_SUBCOMMAND -> updateProductCommand(tokens);
            default -> System.out.println(ERROR_INCORRECT_USE);
        };
    }

    private void addProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (!Product.checkId(productId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        //Name
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        String productName = tokens.next();
        if (!Product.checkName(productName)) {
            System.out.println(ERROR_INVALID_NAME);
            return;
        }

        //Category
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
        if (productCategory.isEmpty()) {
            System.out.println(ERROR_INVALID_CATEGORY);
            return;
        }

        //Price
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
        if (!Product.checkPrice(productPrice)) {
            System.out.println(ERROR_INVALID_PRICE);
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
            System.out.println(PROD_ADD_OK);
        } else {
            System.out.println(ERROR_UNABLE_ADD_PRODUCT);
        }
    }

    private void listProductCommand(Iterator<String> tokens) {
        System.out.println(CATALOG);
        ProductManager.getProductManager().getProducts().forEach(p -> {
            System.out.println("\t" + p);
        });

        System.out.println(PROD_LIST_OK);
    }

    private void updateProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_UPDATE);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }
        Product product = optionalProduct.get();

        //Parametro a cambiar
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_UPDATE);
            return;
        }
        switch (tokens.next()) {
            case "name" -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);
                    return;
                }
                String productName = tokens.next();
                if (!Product.checkName(productName)) {
                    System.out.println(ERROR_INVALID_NAME);
                    return;
                }

                product.setName(productName);
                System.out.println(product);
                System.out.println(PROD_UPDATE_OK);
            }
            case "category" -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);;
                }
                Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
                if (productCategory.isEmpty()) {
                    System.out.println(ERROR_INVALID_CATEGORY);
                    return;
                }

                product.setCategory(productCategory.get());
                System.out.println(product);
                System.out.println(PROD_UPDATE_OK);
            }
            case "price" -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);;
                }
                OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
                if (!Product.checkPrice(productPrice)) {
                    System.out.println(ERROR_INVALID_PRICE);
                    return;
                }

                product.setPrice(productPrice.getAsDouble());
                System.out.println(product);
                System.out.println(PROD_UPDATE_OK);
            }
            default -> {
                System.out.println(ERROR_INCORRECT_USE_UPDATE);;
            }
        }
    }

    private void removeProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_REMOVE);
            return;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }
        Product product = optionalProduct.get();

        ProductManager.getProductManager().removeProduct(product.getId());
        System.out.println(product);
        System.out.println(PROD_REMOVE_OK);
    }
}
