package es.upm.iwsim22_01.commands.predicates;

import es.upm.iwsim22_01.manager.ProductManager;

import java.util.function.Predicate;

public class ValidPricePredicate implements Predicate<Double> {
    public final ProductManager productManager;

    public ValidPricePredicate(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Override
    public boolean test(Double value) {
        return productManager.isPriceValid(value);
    }
}
