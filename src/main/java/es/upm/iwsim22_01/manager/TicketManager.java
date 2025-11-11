package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Optional;

public class TicketManager extends AbstractManager<Ticket, Integer> {

    private final ClientManager clientManager;
    private final CashierManager cashierManager;

    public TicketManager(ClientManager clientManager, CashierManager cashierManager) {
        this.clientManager = clientManager;
        this.cashierManager = cashierManager;
    }

    public void addTicket(int id, String cashierId, String clientId) {
        Optional<Cashier> cashier = cashierManager.get(cashierId);
        if (cashier.isEmpty()) throw new IllegalArgumentException("No cashier found with id: " + cashierId);

        Optional<Client> client = clientManager.get(clientId);
        if (client.isEmpty()) throw new IllegalArgumentException("No client found with id: " + clientId);

        Ticket ticket = new Ticket(id, cashier.get(), client.get());

        add(ticket, id);
    }
}
