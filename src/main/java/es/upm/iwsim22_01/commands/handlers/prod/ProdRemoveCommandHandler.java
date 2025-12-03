package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.product.AbstractProduct;

public class ProdRemoveCommandHandler implements CommandHandler {
    private static final String ERROR_INCORRECT_USE_REMOVE = "Incorrect use: prod remove <id>";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
    private static final String PROD_REMOVE_OK =  "prod remove: ok";

    private final ProductManager productManager;

    public ProdRemoveCommandHandler(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_REMOVE);
            return;
        }
        int productId = tokens.nextInt();
        if (!productManager.existId(productId)) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }

        AbstractProduct product = productManager.get(productId);

        productManager.remove(productId);

        System.out.println(product);
        System.out.println(PROD_REMOVE_OK);
    }
}
