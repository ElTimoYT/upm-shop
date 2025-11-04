package es.upm.iwsim22_01.factory;

import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.models.Cashier;

public class CashierFactory {
    private static final int CASHIER_ID_LENGTH = 7;

    private CashierManager cashierManager;

    public CashierFactory(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    public Cashier createCashier(String name, String email, String id) {
        if (cashierManager.existId(id)) throw new  IllegalArgumentException("Cashier with id " + id + " already exists");

        Cashier cashier = new Cashier(name, email, id);
        cashierManager.add(cashier);

        return cashier;
    }

    public Cashier createCashier(String name, String email) {
        String id;
        do {
            id = createID();
        } while (cashierManager.existId(id));

        return createCashier(name, email, id);
    }


    private String createID() {
        int num = (int) (Math.random() * Math.pow(10, CASHIER_ID_LENGTH));
        StringBuilder preemptiveID = new  StringBuilder(String.valueOf(num));

        for (int i = preemptiveID.length(); i < CASHIER_ID_LENGTH; i++) {
            preemptiveID.insert(0, '0');
        }

        return preemptiveID.toString();
    }
}