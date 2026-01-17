package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.dto.product.category.ServiceCategoryDTO;
import es.upm.iwsim22_01.service.service.ProductService;
import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.time.LocalDateTime;

public class ProdAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_ADD = "Incorrect use: prod add [<id>] \"<name>\" <category> <price> || (\"<name>\" <category> )",
            ERROR_INCORRECT_USE_ADD_SERVICE = "Incorrect use: prod add <expiration:yyyy-MM-dd> <category>",
            ERROR_MAX_PRODUCTS = "Maximum number of products exceeded.",
            ERROR_INVALID_ID = "Id not valid",
            ERROR_INVALID_NAME ="Name not supported",
            ERROR_INVALID_CATEGORY ="Category not valid",
            ERROR_INVALID_PRICE ="Price not valid",
            ERROR_INVALID_MAXPERS = "Max lines not valid",
            PROD_ADD_OK ="Prod add ok";


    private final ProductService productService;

    public ProdAddCommandHandler(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        if (tokens.hasNextDate()) {
            addService(tokens);
        } else {
            addProduct(tokens);
        }
    }

    private void addProduct(CommandTokens tokens) {
        //id
        Integer productId = null;
        if (tokens.hasNextInt()) {
            productId = tokens.nextInt();
            if (productService.existsId(String.valueOf(productId))) {
                System.out.println(ERROR_INVALID_ID);
                return;
            }
        }

        //name
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD);
            return;
        }
        String productName = tokens.next();
        if (!productService.isNameValid(productName)) {
            System.out.println(ERROR_INVALID_NAME);
            return;
        }

        //category
        if (!tokens.hasNextProductCategory()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADD);
            } else {
                System.out.println(ERROR_INVALID_CATEGORY);
            }
            return;
        }
        ProductCategoryDTO category = tokens.nextProductCategory();

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
                created = productId == null ? productService.addPersonalizable(productName, category, price, maxPers) :  productService.addPersonalizable(productId, productName, category, price, maxPers);
            }

        } else {
            created = productId == null ? productService.addProduct(productName, category, price) :   productService.addProduct(productId, productName, category, price);
        }
        System.out.println(created);
        System.out.println(PROD_ADD_OK);
    }

    private void addService(CommandTokens tokens) {
        // expiration
        LocalDateTime expiration = tokens.nextDate();

        // category
        if (!tokens.hasNextServiceCategory()) {
            if (!tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_ADD_SERVICE);
            } else {
                System.out.println(ERROR_INVALID_CATEGORY);
            }
            return;
        }

        ServiceCategoryDTO category = tokens.nextServiceCategory();

        // No deben sobrar tokens
        if (tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADD_SERVICE);
            return;
        }

        AbstractProductDTO created = productService.addService(category, expiration);

        System.out.println(created);
        System.out.println(PROD_ADD_OK);
        return;
    }
}
