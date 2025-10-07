package es.upm.iwsim22_01.command.handler;

import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.command.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;

import java.util.*;
import java.util.function.Function;

public class ProdCommandHandler {
    private static final Map<String, Function<Iterator<String>, CommandStatus>> PROD_SUBCOMMANDS = Map.of(
        "add", ProdCommandHandler::addProductCommand,
        "list", ProdCommandHandler::listProductCommand,
        "remove", ProdCommandHandler::removeProductCommand,
        "update", ProdCommandHandler::updateProductCommand
    );

    public static CommandStatus runCommand(Iterator<String> tokens) {
        CommandStatus incorrectUsage = new CommandStatus(false, "Incorrect use: prod add|list|update|remove");

        if (!tokens.hasNext()) {
            return incorrectUsage;
        }

        return PROD_SUBCOMMANDS.getOrDefault(
                tokens.next(),
                stringIterator -> incorrectUsage
        ).apply(tokens);
    }

    private static CommandStatus addProductCommand(Iterator<String> tokens) {
        CommandStatus incorrectUse = new CommandStatus(false, "Incorrect use: prod add <id> \"<name>\" <category> <price>");

        //Id
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (!Product.checkId(productId)) {
            return new CommandStatus(false, "Invalid id");
        }

        //Name
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        String productName = tokens.next();
        if (!Product.checkName(productName)) {
            return new CommandStatus(false, "Invalid name");
        }

        //Category
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
        if (productCategory.isEmpty()) {
            return new CommandStatus(false, "Invalid category");
        }

        //Price
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
        if (!Product.checkPrice(productPrice)) {
            return new CommandStatus(false, "Invalid price");
        }

        Product product = new Product(
                productId.getAsInt(),
                productName,
                productCategory.get(),
                productPrice.getAsDouble()
        );

        if (ProductManager.getProductManager().addProduct(product)) {
            System.out.println(product);
            return new CommandStatus(true, "prod add: ok");
        } else {
            return new CommandStatus(false, "Unknown error");
        }
    }

    private static CommandStatus listProductCommand(Iterator<String> tokens) {
        System.out.println("Catalog:");
        ProductManager.getProductManager().getProducts().forEach(p -> {
            System.out.println("\t" + p);
        });

        return new CommandStatus(true, "prod list: ok");
    }

    private static CommandStatus updateProductCommand(Iterator<String> tokens) {
        CommandStatus incorrectUse = new CommandStatus(false, "Incorrect use: prod update <id> name|category|price <value>");

        //Id
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            return new CommandStatus(false, "Invalid id");
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            return new CommandStatus(false, "Product not found");
        }
        Product product = optionalProduct.get();

        //Parametro a cambiar
        if (!tokens.hasNext()) {
            return incorrectUse;
        }
        switch (tokens.next()) {
            case "name" -> {
                if (!tokens.hasNext()) {
                    return incorrectUse;
                }
                String productName = tokens.next();
                if (!Product.checkName(productName)) {
                    return new CommandStatus(false, "Invalid name");
                }

                product.setName(productName);
                System.out.println(product);
                return new CommandStatus(true, "prod update: ok");
            }
            case "category" -> {
                if (!tokens.hasNext()) {
                    return incorrectUse;
                }
                Optional<Category> productCategory = Converter.stringToCategory(tokens.next());
                if (productCategory.isEmpty()) {
                    return new CommandStatus(false, "Invalid category");
                }

                product.setCategory(productCategory.get());
                System.out.println(product);
                return new CommandStatus(true, "prod update: ok");
            }
            case "price" -> {
                if (!tokens.hasNext()) {
                    return incorrectUse;
                }
                OptionalDouble productPrice = Converter.stringToDouble(tokens.next());
                if (!Product.checkPrice(productPrice)) {
                    return new CommandStatus(false, "Invalid price");
                }

                product.setPrice(productPrice.getAsDouble());
                System.out.println(product);
                return new CommandStatus(true, "prod update: ok");
            }
            default -> {
                return incorrectUse;
            }
        }
    }

    private static CommandStatus removeProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: prod remove <id>");
        }
        OptionalInt productId = Converter.stringToInt(tokens.next());
        if (productId.isEmpty()) {
            return new CommandStatus(false, "Invalid id");
        }

        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(productId.getAsInt());
        if (optionalProduct.isEmpty()) {
            return new CommandStatus(false, "Product not found");
        }
        Product product = optionalProduct.get();

        ProductManager.getProductManager().removeProduct(product.getId());
        System.out.println(product);
        return new CommandStatus(true, "prod remove: ok");
    }
}
