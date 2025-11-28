package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

import java.util.regex.Pattern;

public class ClientManager extends AbstractManager<Client, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+@\\w+\\.\\w+$"),
        DNI_PATTERN = Pattern.compile("^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$"),
        NIE_PATTERN = Pattern.compile("^[XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]$");

    private final CashierManager cashierManager;

    public ClientManager(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    public Client addClient(String name, String DNI, String email, String cashierWhoRegistersId) {
        if (!cashierManager.existId(cashierWhoRegistersId)) throw new IllegalArgumentException("No cashier found with such an id");
        if (!checkEmail(email)) throw new IllegalArgumentException("Email is not valid");
        if (!checkDNI(DNI)) throw new IllegalArgumentException("Invalid DNI");

        Cashier cashier = cashierManager.get(cashierWhoRegistersId);

        Client client = new  Client(name, DNI, email, cashier);
        add(client, DNI);

        return client;
    }

    public boolean checkDNI(String dni) {
        return DNI_PATTERN.matcher(dni).find() || NIE_PATTERN.matcher(dni).find();
    }

    public boolean checkEmail(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }
}
