package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.ProductService;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

public class ProdRemoveCommandHandler implements CommandHandler {
    private static final String ERROR_INCORRECT_USE_REMOVE = "Incorrect use: prod remove <id>";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
    private static final String PROD_REMOVE_OK =  "prod remove: ok";

    private final ProductService productService;

    public ProdRemoveCommandHandler(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_REMOVE);
            return;
        }

        String productId = tokens.next();
        if (!productService.existsId(String.valueOf(productId))) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return;
        }

        AbstractProductDTO product = productService.get(String.valueOf(productId));

        productService.remove(productId);

        System.out.println(product);
        System.out.println(PROD_REMOVE_OK);
    }
}
