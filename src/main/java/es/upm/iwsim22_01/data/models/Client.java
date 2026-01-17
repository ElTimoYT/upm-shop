package es.upm.iwsim22_01.data.models;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private String email;
    private String dni;
    private String cashierWhoRegisters;
    private List<Integer> tickets;

    public Client(String name, String dni, String email, String cashierWhoRegisters, List<Integer> tickets) {
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.cashierWhoRegisters = cashierWhoRegisters;
        this.tickets = tickets == null ? new ArrayList<>() : tickets;
        this.dni=dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDNI() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCashierWhoRegisters() {
        return cashierWhoRegisters;
    }

    public void setCashierWhoRegisters(String cashierWhoRegisters) {
        this.cashierWhoRegisters = cashierWhoRegisters;
    }

    public List<Integer> getTicketsId() {
        return tickets;
    }

    public void setTickets(List<Integer> tickets) {
        this.tickets = tickets;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;

        if (o instanceof Client client) {
            return client.getDNI().equals(this.getDNI());
        }

        return false;
    }


}
