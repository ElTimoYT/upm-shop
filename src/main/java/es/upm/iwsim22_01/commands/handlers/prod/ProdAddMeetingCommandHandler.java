package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.product.Product;

import java.time.LocalDateTime;


public class ProdAddMeetingCommandHandler implements CommandHandler {
    private ProductManager productManager;
    private static final String ERROR_INCORRECT_USE_ADDM =
            "Incorrect use: prod addMeeting [<id>] \"<name>\" <price> <expiration: yyyy-MM-dd> <max_people>";
    private static final String ERROR_INVALID_ID = "Invalid id";
    private static final String ERROR_INVALID_PRICE = "Invalid price";
    private static final String ERROR_INVALID_DATE = "Invalid expiration date";
    private static final String ERROR_INVALID_MAXPEOPLE = "Invalid max people (must be >= 1)";
    private static final String PROD_ADD_OK = "prod addMeeting: ok";
    private static final String ERROR_MAX_PRODUCTS = "Maximum products reached";

    @Override
    public void runCommand(CommandTokens tokens) {
        if (productManager.isProductListFull()) {
            System.out.println(ERROR_MAX_PRODUCTS);
            return;
        }
        Integer id = tokens.nextAsIntegerId(productManager, true, ERROR_INCORRECT_USE_ADDM, ERROR_INVALID_ID);
        if (id == null) {
            return;
        }
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADDM);
            return;
        }
        String name = tokens.next();


        if (!tokens.hasNextDouble()) {
            if (!tokens.hasNext()) System.out.println(ERROR_INCORRECT_USE_ADDM);
            else System.out.println(ERROR_INVALID_PRICE);
            return;
        }
        double price = tokens.nextDouble();
        if (!productManager.isPriceValid(price)) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        if (!tokens.hasNextDate()) {
            if (!tokens.hasNext()) System.out.println(ERROR_INCORRECT_USE_ADDM);
            else System.out.println(ERROR_INVALID_DATE);
            return;
        }

        LocalDateTime expiration = tokens.nextDate();

        if (!tokens.hasNextInt()) {
            if (!tokens.hasNext()) System.out.println(ERROR_INCORRECT_USE_ADDM);
            else System.out.println(ERROR_INVALID_MAXPEOPLE);
            return;
        }
        int maxPeople = tokens.nextInt();
        if (maxPeople < 1 || maxPeople > 100) {
            System.out.println(ERROR_INVALID_MAXPEOPLE);
            return;
        }

        Product meeting;
        meeting = productManager.addMeetingProduct(id, name, price, expiration, maxPeople);

        System.out.println(meeting);
        System.out.println(PROD_ADD_OK);

    }

    public ProdAddMeetingCommandHandler(ProductManager productManager) {
        this.productManager = productManager;

    }
}
