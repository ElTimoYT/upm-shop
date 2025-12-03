package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.product.AbstractProduct;

import java.time.LocalDateTime;

public class ProdAddFoodCommandHandler implements CommandHandler {
    private static final String ERROR_MAX_PRODUCTS = "Maximum products reached.";
    private static final String ERROR_INCORRECT_USE_ADDFOOD = "Incorrect use: prod addfood <id> <name> <price> <expirationDate> <maxPeople>";
    private static final String ERROR_INVALID_ID = "Invalid id";
    private static final String ERROR_INVALID_PRICE = "Invalid price";
    private static final String ERROR_INVALID_DATE = "Invalid date";
    private static final String ERROR_INVALID_MAX_PEOPLE = "Invalid max people";
    private static final String PROD_ADD_OK = "prod addfood: ok";

    private final ProductManager productManager;

    public ProdAddFoodCommandHandler(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        if (productManager.isProductListFull()) {
            System.out.println(ERROR_MAX_PRODUCTS);
            return;
        }

        //id
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        int productId = tokens.nextInt();
        if (productManager.existId(productId)) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        String name = tokens.next();

        if (!tokens.hasNextDouble()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            } else {
                System.out.println(ERROR_INVALID_PRICE);
            }
            return;
        }
        double price = tokens.nextDouble();
        if (!productManager.isPriceValid(price)) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        if (!tokens.hasNextDate()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            } else {
                System.out.println(ERROR_INVALID_DATE);
            }
            return;
        }
        LocalDateTime expiration = tokens.nextDate();

        if (!tokens.hasNextInt()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            } else {
                System.out.println(ERROR_INVALID_MAX_PEOPLE);
            }
            return;
        }
        int maxPeople = tokens.nextInt();
        if (maxPeople < 1 || maxPeople > 100) {
            System.out.println(ERROR_INVALID_MAX_PEOPLE);
            return;
        }

        AbstractProduct food = productManager.addFoodProduct(
                productId,
                name,
                price,
                expiration,
                maxPeople
        );

        System.out.println(food);
        System.out.println(PROD_ADD_OK);
    }
}
