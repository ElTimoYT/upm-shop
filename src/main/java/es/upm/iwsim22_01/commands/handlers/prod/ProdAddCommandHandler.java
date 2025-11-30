package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.product.Category;
import es.upm.iwsim22_01.models.product.Product;

public class ProdAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_ADD = "Incorrect use: prod add <id> \"<name>\" <category> <price>",
            ERROR_MAX_PRODUCTS = "Maximum number of products exceeded.",
            ERROR_INVALID_ID = "Id not valid",
            ERROR_INVALID_NAME ="Name not supported",
            ERROR_INVALID_CATEGORY ="Category not valid",
            ERROR_INVALID_PRICE ="Price not valid",
            ERROR_INVALID_MAXPERS = "Max lines not valid",
            PROD_ADD_OK ="Prod add ok";


    private final ProductManager productManager;

    @Override
    public void runCommand(CommandTokens tokens) {

        if (productManager.isProductListFull()) {
            System.out.println(ERROR_MAX_PRODUCTS);
            return;
        }

        //id
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        int productId = tokens.nextInt();
        if (productManager.existId(productId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        //name

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        String productName = tokens.next();
        if (!productManager.isNameValid(productName)) {
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
        Category category = tokens.nextCategory();

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
        Product created;

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
                created = productManager.addCustomizableProduct(productId, productName, category, price, maxPers);
            }

        } else {
            created = productManager.addProduct(productId, productName, category, price);
        }
        System.out.println(created);
        System.out.println(PROD_ADD_OK);

    }

    public ProdAddCommandHandler(ProductManager productManager) {
        this.productManager = productManager;
    }
}
