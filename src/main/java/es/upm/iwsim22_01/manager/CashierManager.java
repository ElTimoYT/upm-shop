package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;

import java.util.regex.Pattern;

public class CashierManager extends AbstractManager<Cashier, String> {
    private static final int CASHIER_ID_LENGTH = 7;
    private static final Pattern CASHIER_EMAIL_REGEX = Pattern.compile("^[\\w-.]+@upm.es$");

    public Cashier addCashier(String name, String email, String id) {
        if (!CASHIER_EMAIL_REGEX.matcher(email).find()) {
            throw new IllegalArgumentException("Invalid email format");
        }

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

    public boolean removeCashierAndTickets(String id, TicketManager ticketManager) {
        Cashier cashier = get(id);

        if (cashier == null){
            return false;
        }

        cashier.getTickets().forEach(t -> ticketManager.remove(t.getId()));
        this.remove(id);

        return true;
    }

    public boolean correctIdFormat(String id){
        String regex = "^UW\\d{" + CASHIER_ID_LENGTH + "}$";
        return Pattern.matches(regex, id);
    }

    private String createID() {
        int num = (int) (Math.random() * Math.pow(10, CASHIER_ID_LENGTH));
        StringBuilder preemptiveID = new  StringBuilder(String.valueOf(num));

        for (int i = preemptiveID.length(); i < CASHIER_ID_LENGTH; i++) {
            preemptiveID.insert(0, '0');
        }

        preemptiveID.insert(0, "UW");

        return preemptiveID.toString();
    }
}
