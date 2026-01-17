package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.ProductInventory;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.time.LocalDateTime;


public class ProdAddMeetingCommandHandler implements CommandHandler {
    private ProductInventory productService;
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
        if (productService.isProductListFull()) {
            System.out.println(ERROR_MAX_PRODUCTS);
            return;
        }

        //id
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_ADDM);
            return;
        }
        int productId = tokens.nextInt();
        if (productService.existsId(productId)) {
            System.out.println(ERROR_INVALID_ID);
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
        if (!productService.isPriceValid(price)) {
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

        AbstractProductDTO meeting;
        meeting = productService.addMeetingProduct(productId, name, price, expiration, maxPeople);

        System.out.println(meeting);
        System.out.println(PROD_ADD_OK);

    }

    public ProdAddMeetingCommandHandler(ProductInventory productService) {
        this.productService = productService;

    }
}
