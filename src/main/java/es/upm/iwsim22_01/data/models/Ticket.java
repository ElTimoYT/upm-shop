package es.upm.iwsim22_01.data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ticket {
    private int id;
    private Date initialDate;
    private Date finalDate;
    private String ticketState;
    private List<Product> products;
    private TicketType ticketType;

    public Ticket(int id, Date initialDate, Date finalDate, String ticketState, List<Product> products, TicketType ticketType) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.ticketState = ticketState;
        this.products = products == null ? new ArrayList<>() : products;
        this.ticketType = ticketType;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public String getTicketState() {
        return ticketState;
    }

    public void setTicketState(String ticketState) {
        this.ticketState = ticketState;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;

        if (o instanceof Ticket ticket) {
            return ticket.getId() == this.getId();
        }

        return false;
    }

    public enum TicketType {
        ONLY_PRODUCTS,
        ONLY_SERVICES,
        SERVICES_AND_PRODUCTS;
    }
}
