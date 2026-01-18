package es.upm.iwsim22_01.service.dto.user;

import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un cajero del sistema con capacidad para gestionar múltiples tickets.
 */
public class CashierDTO extends AbstractUserDTO {
    private List<AbstractTicketDTO> tickets;

    /**
     * Crea un cajero con una lista inicial de tickets.
     *
     * @param name nombre del cajero
     * @param email correo electrónico del cajero
     * @param id identificador único del cajero
     * @param tickets lista inicial de tickets
     */
    public CashierDTO(String name, String email, String id, List<AbstractTicketDTO> tickets) {
        super(name, email , id);
        this.tickets = tickets;
    }

    /**
     * Crea un cajero sin tickets asociados inicialmente.
     *
     * @param name nombre del cajero
     * @param email correo electrónico del cajero
     * @param id identificador único del cajero
     */
    public CashierDTO(String name, String email, String id) {
        this(name, email , id, new ArrayList<>());
    }

    /**
     * Obtiene la lista de tickets asociados al cajero.
     *
     * @return Lista de tickets del cajero.
     */
    public List<AbstractTicketDTO> getTickets() {
        return tickets;
    }

    /**
     * Añade un ticket a la lista de tickets del cajero.
     *
     * @param ticket Ticket a añadir.
     */
    public void addTicket(AbstractTicketDTO ticket) {
        this.tickets.add(ticket);
    }

    /**
     * Devuelve una representación en cadena del cajero.
     *
     * @return Cadena que representa al cajero, incluyendo su identificador, nombre y correo electrónico.
     */
    @Override
    public String toString() {
        return "Cash{" +
                "identifier='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                "}";
    }
}
