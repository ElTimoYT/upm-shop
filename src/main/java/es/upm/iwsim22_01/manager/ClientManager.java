package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

import java.util.Optional;

public class ClientManager extends AbstractManager<Client, String> {
    private final CashierManager cashierManager;

    public ClientManager(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    public Client addClient(String name, String DNI, String email, String cashierWhoRegistersId) {

        Optional<Cashier> cashier = cashierManager.get(cashierWhoRegistersId);
        if (cashier.isEmpty()) throw new IllegalArgumentException("No cashier found with such an id");

        Client client = new  Client(name, DNI, email, cashier.get());
        add(client, DNI);

        return client;
    }
}
