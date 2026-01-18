package es.upm.iwsim22_01.service.dto.user;

import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase que representa a un cliente del sistema con tickets asociados y un cajero que lo registró.
 */
public class ClientDTO extends AbstractUserDTO {
    private CashierDTO cashierWhoRegisters;
    private List<AbstractTicketDTO> tickets;

    /**
     * Crea un cliente con todos sus atributos inicializados.
     *
     * @param name nombre del cliente
     * @param DNI identificador único del cliente
     * @param email correo electrónico del cliente
     * @param cashierWhoRegisters cajero que registró al cliente
     * @param tickets lista inicial de tickets del cliente
     */
    public ClientDTO(String name, String DNI, String email, CashierDTO cashierWhoRegisters, List<AbstractTicketDTO> tickets) {
        super(name, email, DNI);
        this.cashierWhoRegisters = cashierWhoRegisters;
        this.tickets = (tickets == null) ? new ArrayList<>() : new ArrayList<>(tickets);
    }

    /**
     * Crea un cliente sin tickets asociados inicialmente.
     *
     * @param name nombre del cliente
     * @param DNI identificador único del cliente
     * @param email correo electrónico del cliente
     * @param cashierWhoRegisters cajero que registró al cliente
     */
    public ClientDTO(String name, String DNI, String email, CashierDTO cashierWhoRegisters) {
        this(name, DNI, email, cashierWhoRegisters, new ArrayList<>());
    }

    /**
     * Obtiene el cajero que registró al cliente.
     *
     * @return El cajero asociado al cliente.
     */
    public CashierDTO getCashier() {
        return cashierWhoRegisters;
    }

    /**
     * Añade un ticket a la lista de tickets del cliente.
     *
     * @param ticket Ticket a añadir.
     */
    public void addTicket(AbstractTicketDTO ticket) {
        tickets.add(ticket);
    }

    /**
     * Devuelve una copia de la lista de tickets del cliente.
     *
     * @return lista de tickets del cliente
     */
    public List<AbstractTicketDTO> getTickets() {
        return new ArrayList<>(tickets);
    }

    /**
     * Devuelve una representación textual del cliente con sus datos básicos.
     *
     * @return representación textual del cliente
     */
    public String toString() {
        return "ClientDTO{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", dni='" + getId() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cashierWhoRegisters=" + (cashierWhoRegisters != null ? cashierWhoRegisters.getId() : "null") +
                '}';
    }
}
