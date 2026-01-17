package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.ProductInventory;
import es.upm.iwsim22_01.service.dto.product.CategoryDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.time.LocalDateTime;

public class ProdAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_ADD = "Incorrect use: prod add <id> \"<name>\" <category> <price>",
            ERROR_INCORRECT_USE_ADD_SERVICE = "Incorrect use: prod add <expiration:yyyy-MM-dd> <category>",
            ERROR_MAX_PRODUCTS = "Maximum number of products exceeded.",
            ERROR_INVALID_ID = "Id not valid",
            ERROR_INVALID_NAME ="Name not supported",
            ERROR_INVALID_CATEGORY ="Category not valid",
            ERROR_INVALID_PRICE ="Price not valid",
            ERROR_INVALID_MAXPERS = "Max lines not valid",
            PROD_ADD_OK ="Prod add ok";


    private final ProductInventory productInventory;

    @Override
    public void runCommand(CommandTokens tokens) {


        if (tokens.hasNextDate()) {

            if (productInventory.isProductListFull()) {
                System.out.println(ERROR_MAX_PRODUCTS);
                return;
            }

            // expiration
            LocalDateTime expiration = tokens.nextDate();

            // category
            if (!tokens.hasNextCategory()) {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_ADD_SERVICE);
                } else {
                    System.out.println(ERROR_INVALID_CATEGORY);
                }
                return;
            }
            CategoryDTO category = tokens.nextCategory();

            // Validar que la categoría sea de servicio
            if (!(category == CategoryDTO.INSURANCE
                    || category == CategoryDTO.TRANSPORT
                    || category == CategoryDTO.SHOW)) {
                System.out.println(ERROR_INVALID_CATEGORY);
                return;
            }

            // No deben sobrar tokens
            if (tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADD_SERVICE);
                return;
            }

            AbstractProductDTO created = productInventory.addProductService(expiration, category);

            System.out.println(created);
            System.out.println(PROD_ADD_OK);
            return;
        }

        if (productInventory.isProductListFull()) {
            System.out.println(ERROR_MAX_PRODUCTS);
            return;
        }

        //id
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        int productId = tokens.nextInt();
        if (productInventory.existsId(productId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        //name

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        String productName = tokens.next();
        if (!productInventory.isNameValid(productName)) {
            System.out.println(ERROR_INVALID_NAME);
            return;
        }

        //category
        if (!tokens.hasNextCategory()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADD);
            } else {
                System.out.println(ERROR_INVALID_CATEGORY);
            }
            return;
        }
        CategoryDTO category = tokens.nextCategory();

        //price
        if (!tokens.hasNextDouble()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADD);
            } else {
                System.out.println(ERROR_INVALID_PRICE);
            }
            return;
        }
        double price = tokens.nextDouble();

        //crear producto
        AbstractProductDTO created;

        if (tokens.hasNext()) {

            if (!tokens.hasNextInt()) {
                System.out.println(ERROR_INCORRECT_USE_ADD);
                return;
            }

            int maxPers = tokens.nextInt();
            if (maxPers < 1) {
                System.out.println(ERROR_INVALID_MAXPERS);
                return;
            } else {
                created = productInventory.addPersonalizableProduct(productId, productName, category, price, maxPers);
            }

        } else {
            created = productInventory.addUnitProduct(productId, productName, category, price);
        }
        System.out.println(created);
        System.out.println(PROD_ADD_OK);

    }

    public ProdAddCommandHandler(ProductInventory productService) {
        this.productInventory = productService;
    }

    private static boolean isDateToken(String token) {
        return token != null && token.matches("\\d{4}-\\d{2}-\\d{2}");
    }

}
