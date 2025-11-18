package es.upm.iwsim22_01.models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Cashier extends User {
    private List<Ticket> tickets;

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

    @Override
    public String toString() {
        return "Cash{" +
                "identifier='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
