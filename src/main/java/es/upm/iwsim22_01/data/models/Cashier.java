package es.upm.iwsim22_01.data.models;

import java.util.List;

public class Cashier {
    private String name;
    private String email;
    private String dni;
    private List<Integer> tickets;

    public Cashier(String name, String email, String dni, List<Integer> tickets) {
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.tickets = tickets;
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

        if (o instanceof Cashier cashier) {
            return cashier.getDNI().equals(this.getDNI());
        }

        return false;
    }
}
