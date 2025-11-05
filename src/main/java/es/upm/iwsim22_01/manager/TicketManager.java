package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Ticket;

public class TicketManager extends AbstractManager<Ticket, String> {
    public void addTicket(String id) {
        Ticket ticket = new Ticket();

        add(ticket, id);
    }
}
