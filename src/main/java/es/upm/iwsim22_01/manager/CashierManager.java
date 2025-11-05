package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

public class CashierManager extends AbstractManager<Cashier, String> {
    private static final int CASHIER_ID_LENGTH = 7;

    public Cashier addCashier(String name, String email, String id) {
        Cashier cashier = new Cashier(name, email, id);
        add(cashier, id);
        
        return cashier;
    }

    public Cashier addCashier(String name, String email) {
        String id;
        do {
            id = createID();
        } while (existId(id));

        return addCashier(name, email, id);
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
