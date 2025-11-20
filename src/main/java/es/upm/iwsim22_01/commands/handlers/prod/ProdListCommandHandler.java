package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;


public class ProdListCommandHandler implements CommandHandler {
    private ProductManager productManager;

    private final String PROD_LIST_OK ="Prod list ok",
    CATALOG = "Catalog: ";


    @Override
    public void runCommand(CommandTokens tokens) {
        System.out.println(CATALOG);
        productManager.getAll().forEach(p -> {
            System.out.println("\t" + p);
        });

        System.out.println(PROD_LIST_OK);

    }
    public ProdListCommandHandler(ProductManager productManager) {
        this.productManager = productManager;

    }
}
