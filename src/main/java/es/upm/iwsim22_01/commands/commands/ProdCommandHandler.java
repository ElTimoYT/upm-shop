package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.commands.CommandStatus;
import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;

/**
 * Manejador del comando 'prod' para gestionar productos.
 * <p>
 * Este comando permite realizar operaciones CRUD sobre productos:
 * añadir, listar, actualizar y eliminar productos del catálogo.
 * </p>
 */
public class ProdCommandHandler implements CommandHandler {
    
    /**
     * Ejecuta el comando prod delegando a la suboperación correspondiente.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        CommandStatus incorrectUsage = new CommandStatus(false, "Incorrect use: prod add|list|update|remove");

        if (!tokens.hasNext()) {
            return incorrectUsage;
        }

        return switch (tokens.next()) {
            case "add" -> addProductCommand(tokens);
            case "list" -> listProductCommand(tokens);
            case "remove" -> removeProductCommand(tokens);
            case "update" -> updateProductCommand(tokens);
            default -> incorrectUsage;
        };
    }

    /**
     * Maneja la suboperación 'add' del comando prod.
     * Añade un nuevo producto al catálogo.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus addProductCommand(Iterator<String> tokens) {
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

    /**
     * Maneja la suboperación 'list' del comando prod.
     * Lista todos los productos del catálogo.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus listProductCommand(Iterator<String> tokens) {
        System.out.println("Catalog:");
        ProductManager.getProductManager().getProducts().forEach(p -> {
            System.out.println("\t" + p);
        });

        return new CommandStatus(true, "prod list: ok");
    }

    /**
     * Maneja la suboperación 'update' del comando prod.
     * Actualiza un producto existente en el catálogo.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus updateProductCommand(Iterator<String> tokens) {
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

    /**
     * Maneja la suboperación 'remove' del comando prod.
     * Elimina un producto del catálogo.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus con el resultado de la operación
     */
    private CommandStatus removeProductCommand(Iterator<String> tokens) {
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
