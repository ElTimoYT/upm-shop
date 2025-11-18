package es.upm.iwsim22_01.commands.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.UnitProduct;

public class ProdCommandHandler implements CommandHandler {
    private static final String ADD_SUBCOMMAND = "add",
        LIST_SUBCOMMAND = "list",
        REMOVE_SUBCOMMAND = "remove",
        UPDATE_SUBCOMMAND = "update",
        ADD_FOOD_SUBCOMMAND = "addfood",
        ADD_MEETING_SUBCOMMAND = "addmeeting",


        ERROR_INCORRECT_USE = "Incorrect use: prod add|list|update|remove|addFood|addMeeting",
        ERROR_INCORRECT_USE_ADD = "Incorrect use: prod add <id> \"<name>\" <category> <price>",
        ERROR_INCORRECT_USE_ADDFOOD = "Incorrect use: prod addFood <id> \"<name>\" <category> <price> " +
                "<expiration: yyyy-MM-dd> <max_people>",
        ERROR_INCORRECT_USE_ADDMEETING = "Incorrect use: prod addMeeting <id> \"<name>\" <category> <price>" +
                "<expiration: yyyy-MM-dd> <max_people>",
        ERROR_INCORRECT_USE_UPDATE = "Incorrect use: prod update <id> name|category|price <value>",
        ERROR_INCORRECT_USE_REMOVE = "Incorrect use: prod remove <id>",
        ERROR_MAX_PRODUCTS = "You have reached the max products amount",
        ERROR_INVALID_ID = "Invalid id",
        ERROR_INVALID_NAME = "Invalid name",
        ERROR_INVALID_CATEGORY = "Invalid category",
        ERROR_INVALID_PRICE = "Invalid price",
        ERROR_PRODUCT_NOT_FOUND = "Product not found",
        ERROR_INVALID_MAXPERS = "Invalid number of max text",
        ERROR_INVALID_DATE = "Invalid date",
        ERROR_INVALID_MAX_PEOPLE="Invalid max people",

        PROD_ADD_OK = "prod add: ok",
        CATALOG = "Catalog:",
        PROD_LIST_OK = "prod list: ok",
        PROD_UPDATE_OK = "prod update: ok",
        PROD_REMOVE_OK = "prod remove: ok",

        PROD_UPDATE_PARAMETER_CATEGORY = "category",
        PROD_UPDATE_PARAMETER_NAME = "name",
        PROD_UPDATE_PARAMETER_PRICE = "price";

    private ProductManager productManager;
    
    public ProdCommandHandler(ProductManager productManager) {
        this.productManager = productManager;
    }
    
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
            case ADD_FOOD_SUBCOMMAND -> addFoodCommand(tokens);
            case ADD_MEETING_SUBCOMMAND -> addMeetingCommand(tokens);
            default -> System.out.println(ERROR_INCORRECT_USE);
        };
    }

    private void addProductCommand(Iterator<String> tokens) {
        if (productManager.isProductListFull()) {
            System.out.println(ERROR_MAX_PRODUCTS);
            return;
        }

        //Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty() || productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        //Name
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        String productName = tokens.next();
        if (!productManager.isNameValid(productName)) {
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
        OptionalDouble optionalPrice = Converter.stringToDouble(tokens.next());
        if (optionalPrice.isEmpty() || !productManager.isPriceValid(optionalPrice.getAsDouble())) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        Product create = null;
        if (tokens.hasNext()) {
            OptionalInt optMaxPers = Converter.stringToInt(tokens.next());
            if (optMaxPers.isEmpty() || optMaxPers.getAsInt() < 1) {
                System.out.println(ERROR_INVALID_MAXPERS);
                return;
            }
            create = productManager.addCustomizableProduct(
                    optionalId.getAsInt(),
                    productName,
                    productCategory.get(),
                    optionalPrice.getAsDouble(),
                    optMaxPers.getAsInt()
            );

        } else {
            create = productManager.addProduct(
                    optionalId.getAsInt(),
                    productName,
                    productCategory.get(),
                    optionalPrice.getAsDouble()
            );
        }

        System.out.println(create);
        System.out.println(PROD_ADD_OK);
    }

    private void listProductCommand(Iterator<String> tokens) {
        System.out.println(CATALOG);
        productManager.getAll().forEach(p -> {
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
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if (!productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }
        Product product = productManager.get(optionalId.getAsInt());

        //Parametro a cambiar
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_UPDATE);
            return;
        }
        switch (tokens.next()) {
            case PROD_UPDATE_PARAMETER_NAME -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);
                    return;
                }
                String productName = tokens.next();
                if (!productManager.isNameValid(productName)) {
                    System.out.println(ERROR_INVALID_NAME);
                    return;
                }

                product.setName(productName);
            }
            case PROD_UPDATE_PARAMETER_CATEGORY -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);;
                }

                if (!(product instanceof UnitProduct unitProduct)) {
                    System.out.println("This product type has no category");
                    return;
                }

                Optional<Category> optionalCat = Converter.stringToCategory(tokens.next());
                if (optionalCat.isEmpty()) {
                    System.out.println(ERROR_INVALID_CATEGORY);
                    return;
                }

                unitProduct.setCategory(optionalCat.get());

            }
            case PROD_UPDATE_PARAMETER_PRICE -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);;
                }
                OptionalDouble optionalPrice = Converter.stringToDouble(tokens.next());
                if (optionalPrice.isEmpty() || !productManager.isPriceValid(optionalPrice.getAsDouble())) {
                    System.out.println(ERROR_INVALID_PRICE);
                    return;
                }

                product.setPrice(optionalPrice.getAsDouble());
            }
            default -> {
                System.out.println(ERROR_INCORRECT_USE_UPDATE);
                return;
            }
        }

        System.out.println(product);
        System.out.println(PROD_UPDATE_OK);
    }

    private void removeProductCommand(Iterator<String> tokens) {
        //Id
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_REMOVE);
            return;
        }
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty()) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if (!productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }
        Product product = productManager.get(optionalId.getAsInt());

        productManager.remove(optionalId.getAsInt());
        System.out.println(product);
        System.out.println(PROD_REMOVE_OK);
    }

    private void addFoodCommand(Iterator<String> tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty() || productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if(!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        String name = tokens.next();

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        OptionalDouble optionalPrice = Converter.stringToDouble(tokens.next());
        if (optionalPrice.isEmpty() || !productManager.isPriceValid(optionalPrice.getAsDouble())) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        Optional<java.time.LocalDate> expiration = Converter.stringToLocal(tokens.next());
        if (expiration.isEmpty()) {
            System.out.println(ERROR_INVALID_DATE);
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate minAllowedDate = today.plusDays(3);
        LocalDate eventDate = expiration.get();

        if (eventDate.isBefore(minAllowedDate)) {
            System.out.println("Food must be scheduled at least 3 days in advance");
            return;
        }

        if  (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        OptionalInt optionalMaxPeople = Converter.stringToInt(tokens.next());
        if (optionalMaxPeople.isEmpty() || optionalMaxPeople.getAsInt() < 1) {
            System.out.println(ERROR_INVALID_MAX_PEOPLE);
            return;
        }

        Product food = productManager.addFoodProduct(optionalId.getAsInt(),name, optionalPrice.getAsDouble(), expiration.get(), optionalMaxPeople.getAsInt());
        System.out.println(food);
        System.out.println(PROD_ADD_OK);

    }
    private void addMeetingCommand (Iterator<String> tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty() || productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if(!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }

        String name = tokens.next();

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }

        OptionalDouble optionalPrice = Converter.stringToDouble(tokens.next());
        if (optionalPrice.isEmpty() || !productManager.isPriceValid(optionalPrice.getAsDouble())) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }

        Optional<java.time.LocalDate> expiration = Converter.stringToLocal(tokens.next());
        if (expiration.isEmpty()) {
            System.out.println(ERROR_INVALID_DATE);
            return;
        }
        LocalDate date = expiration.get();          // fecha de la reunión
        LocalDateTime meetingDateTime = date.atStartOfDay(); // 00:00 de ese día
        LocalDateTime now = LocalDateTime.now();
        long hoursBetween = ChronoUnit.HOURS.between(now, meetingDateTime);

        if (hoursBetween < 12) {
            System.out.println("Meeting must be scheduled at least 12 hours in advance");
            return;
        }

        if  (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }
        OptionalInt optionalMaxPeople = Converter.stringToInt(tokens.next());
        if (optionalMaxPeople.isEmpty() || optionalMaxPeople.getAsInt() < 1) {
            System.out.println(ERROR_INVALID_MAX_PEOPLE);
            return;
        }

        Product meeting = productManager.addMeetingProduct(optionalId.getAsInt(), name, optionalPrice.getAsDouble(), expiration.get()
        ,optionalMaxPeople.getAsInt());
        System.out.println(meeting);
        System.out.println(PROD_ADD_OK);
    }


}
