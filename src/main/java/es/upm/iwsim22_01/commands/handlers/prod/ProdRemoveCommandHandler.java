package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Product;

public class ProdRemoveCommandHandler implements CommandHandler {
    private ProductManager productManager;
    private static final String ERROR_INCORRECT_USE_REMOVE =
            "Incorrect use: prod remove <id>";
    private static final String ERROR_PRODUCT_NOT_FOUND =
            "Product not found";
    private static final String PROD_REMOVE_OK =
            "prod remove: ok";

    @Override
    public void runCommand(CommandTokens tokens) {

        Integer id = tokens.nextAsIntegerId(productManager, false, ERROR_INCORRECT_USE_REMOVE, ERROR_PRODUCT_NOT_FOUND);
        if (id == null) {return;}

        Product product = productManager.get(id);

        productManager.remove(id);

        System.out.println(product);
        System.out.println(PROD_REMOVE_OK);
    }



    public ProdRemoveCommandHandler(ProductManager productManager) {
        this.productManager = productManager;
    }
}
