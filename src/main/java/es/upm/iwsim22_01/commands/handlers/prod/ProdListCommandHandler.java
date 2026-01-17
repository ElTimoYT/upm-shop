package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.inventory.ProductInventory;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;

import java.util.Comparator;


public class ProdListCommandHandler implements CommandHandler {
    private static final String PROD_LIST_OK ="Prod list ok",
    CATALOG = "Catalog: ";

    private final ProductInventory productService;

    public ProdListCommandHandler(ProductInventory productService) {
        this.productService = productService;

    }

    @Override
    public void runCommand(CommandTokens tokens) {
        System.out.println(CATALOG);

        productService.getAll().stream()
                .sorted(Comparator.comparingInt(AbstractProductDTO::getId))
                .forEach(p -> System.out.println("\t" + p));

        System.out.println(PROD_LIST_OK);

    }
}
