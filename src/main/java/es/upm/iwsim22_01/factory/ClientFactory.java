package es.upm.iwsim22_01.factory;

import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;

public class ClientFactory {
    private ClientManager clientManager;

    private ClientFactory(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public Client createClient(String name, String DNI, String email, Cashier cashierWhoRegisters) {

        if (clientManager.existId(DNI)) throw new  IllegalArgumentException("Client with DNI " + DNI + " already exists");

        Client client = new Client(name, DNI, email, cashierWhoRegisters);
        clientManager.add(client);

        return client;
    }
}
