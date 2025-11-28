package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Ticket;

public class TicketManager extends AbstractManager<Ticket, Integer> {
    private static final int TICKET_ID_LENGTH = 7;
    private final ClientManager clientManager;
    private final CashierManager cashierManager;

    public TicketManager(ClientManager clientManager, CashierManager cashierManager) {
        this.clientManager = clientManager;
        this.cashierManager = cashierManager;
    }

    public Ticket addTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        Ticket ticket = new Ticket(id);

        add(ticket, id);
        return ticket;
    }

    public Ticket addTicket() {
        return addTicket(createNewId());
    }

    public boolean checkId(int id){
        return Math.log10(id) <= TICKET_ID_LENGTH;
    }

    private int createNewId() {
        int id;

        do {
            id = (int) (Math.random() * Math.pow(10, TICKET_ID_LENGTH));
        } while (existId(id));

        return id;
    }
}
