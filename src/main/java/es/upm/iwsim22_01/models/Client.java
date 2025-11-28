package es.upm.iwsim22_01.models;

import java.util.ArrayList;
import java.util.List;

public class Client extends User {

    private Cashier cashierWhoRegisters;
    private List<Ticket> tickets = new ArrayList<>();

    public Client(String name, String DNI, String email, Cashier cashierWhoRegisters) {
        super(name, email, DNI);
        this.cashierWhoRegisters = cashierWhoRegisters;
    }

    public Cashier getCashier() {
        return cashierWhoRegisters;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ",name=" + getName() +
                ",email=" + getEmail() +
                ",cash=" + cashierWhoRegisters.getId() +
                '}';
    }
}
