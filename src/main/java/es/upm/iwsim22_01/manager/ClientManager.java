package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

import java.util.Optional;
import java.util.regex.Pattern;

public class ClientManager extends AbstractManager<Client, String> {
    private final CashierManager cashierManager;

    public ClientManager(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    public Client addClient(String name, String DNI, String email, String cashierWhoRegistersId) {
        if (!cashierManager.existId(cashierWhoRegistersId)) throw new IllegalArgumentException("No cashier found with such an id");

        Cashier cashier = cashierManager.get(cashierWhoRegistersId);

        Client client = new  Client(name, DNI, email, cashier);
        add(client, DNI);

        return client;
    }

    private static final Pattern REGEX = Pattern.compile("^(?![\\w.-]+@upm\\.es$)[\\w.-]+@([\\w-]+\\.)+[\\w-]+$");

    @Override
    public boolean checkEmail(String email) {
        return REGEX.matcher(email).find();
    }
}
