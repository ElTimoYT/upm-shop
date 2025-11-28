package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.UnitProduct;

public class ProdUpdateCommandHnadler implements CommandHandler {
    private static final String ERROR_INCORRECT_USE_UPDATE =
            "Incorrect use: prod update <id> NAME|CATEGORY|PRICE <value>";
    private static final String ERROR_INVALID_ID = "Invalid id";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
    private static final String ERROR_INVALID_NAME = "Invalid name";
    private static final String ERROR_INVALID_CATEGORY = "Invalid category";
    private static final String ERROR_INVALID_PRICE = "Invalid price";
    private static final String PROD_UPDATE_OK = "prod update: ok";
    private static final String PROD_UPDATE_PARAMETER_NAME = "NAME";
    private static final String PROD_UPDATE_PARAMETER_CATEGORY = "CATEGORY";
    private static final String PROD_UPDATE_PARAMETER_PRICE = "PRICE";

    private ProductManager productManager;
    @Override
    public void runCommand(CommandTokens tokens) {
        //id
        Integer id = tokens.nextAsIntegerId(productManager, false, ERROR_INCORRECT_USE_UPDATE, ERROR_INVALID_ID);
        if (id == null) {
            return;
        }
        Product product = productManager.get(id);

       //param
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_UPDATE);
            return;
        }
        String parameter = tokens.next();

        switch (parameter) {

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
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);
                    return;
                }

                if (!(product instanceof UnitProduct unitProduct)) {
                    System.out.println("This product type has no category");
                    return;
                }
                if (!tokens.hasNextCategory()) {
                    System.out.println(ERROR_INVALID_CATEGORY);
                    return;
                }
                Category category = tokens.nextCategory();
                unitProduct.setCategory(category);
            }

            case PROD_UPDATE_PARAMETER_PRICE -> {
                if (!tokens.hasNext()) {
                    System.out.println(ERROR_INCORRECT_USE_UPDATE);
                    return;
                }

                if (!tokens.hasNextDouble()) {
                    System.out.println(ERROR_INVALID_PRICE);
                    return;
                }
                double price = tokens.nextDouble();
                if (!productManager.isPriceValid(price)) {
                    System.out.println(ERROR_INVALID_PRICE);
                    return;
                }

                product.setPrice(price);
            }

            default -> {
                System.out.println(ERROR_INCORRECT_USE_UPDATE);
                return;
            }
        }
        System.out.println(product);
        System.out.println(PROD_UPDATE_OK);
    }

    public  ProdUpdateCommandHnadler(ProductManager productManager) {
        this.productManager = productManager;
    }

}
