package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Cashier;
import es.upm.iwsim22_01.models.Client;
import es.upm.iwsim22_01.models.Ticket;

import java.util.Optional;

public class TicketManager extends AbstractManager<Ticket, Integer> {
    private static final int TICKET_ID_LENGTH = 7;

    public Ticket addTicket(int id) {
        Ticket ticket = new Ticket(id);

        add(ticket, id);
        return ticket;
    }

    public Ticket addTicket() {
        return addTicket(createNewId());
    }

    private int createNewId() {
        int id;

        do {
            id = (int) (Math.random() * Math.pow(10, TICKET_ID_LENGTH));
        } while (existId(id));

        return id;
    }
}
