package es.upm.iwsim22_01.models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Cashier extends User {
    private static final Pattern REGEX = Pattern.compile("^[\\w-.]+@upm.es$");

    private List<Ticket> tickets;

    @Override
    protected boolean checkEmail(String email) {
        return REGEX.matcher(email).find();
    }

    public Cashier(String name, String email, String id) {
        super(name, email , id);
        this.tickets = new ArrayList<>();
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

}
