package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Ticket;

public class TicketManager extends AbstractManager<Ticket, String> {
    @Override
    public boolean add(Ticket ticket) {
        return add(ticket, ticket.getId()); //TODO: maxima cantidad de tickets?
    }
}
