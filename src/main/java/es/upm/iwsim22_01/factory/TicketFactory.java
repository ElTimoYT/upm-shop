package es.upm.iwsim22_01.factory;

import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Ticket;

public class TicketFactory {
    private TicketManager ticketManager;

    public TicketFactory(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    /*public Ticket createTicket() {

    }*/ //TODO: falta constructor de ticket
}
