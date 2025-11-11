package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

public class ClientManager extends AbstractManager<Client, String> {
    private final CashierManager cashierManager;

    public ClientManager(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
    }

    public Client addClient(String name, String DNI, String email, Cashier cashierWhoRegisters) {


        Client client = new  Client(name, DNI, email, cashierWhoRegisters);
        add(client, DNI);

        return client;
    }
}
