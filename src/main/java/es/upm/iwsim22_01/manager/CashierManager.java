package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

public class CashierManager extends AbstractManager<Cashier, String> {
    @Override
    public boolean add(Cashier cashier) {
        return add(cashier, cashier.getId()); //TODO: maxima cantidad de cajeros?
    }


}
