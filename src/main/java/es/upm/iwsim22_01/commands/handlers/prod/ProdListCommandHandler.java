package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.product.Product;

import java.util.Comparator;


public class ProdListCommandHandler implements CommandHandler {
    private ProductManager productManager;

    private final String PROD_LIST_OK ="Prod list ok",
    CATALOG = "Catalog: ";


    @Override
    public void runCommand(CommandTokens tokens) {
        System.out.println(CATALOG);

        productManager.getAll().stream()
                .sorted(Comparator.comparingInt(Product::getId))
                .forEach(p -> System.out.println("\t" + p));

        System.out.println(PROD_LIST_OK);

    }
    public ProdListCommandHandler(ProductManager productManager) {
        this.productManager = productManager;

    }
}
