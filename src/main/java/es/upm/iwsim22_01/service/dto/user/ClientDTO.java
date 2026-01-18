package es.upm.iwsim22_01.service.dto.user;

import es.upm.iwsim22_01.data.models.Client;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un cliente en el sistema, especialización de AbstractUser.
 * Un cliente está asociado a un cajero que lo registró y puede tener múltiples tickets.
 */
public class ClientDTO extends AbstractUserDTO {
    private CashierDTO cashierWhoRegisters;
    private List<TicketDTO> tickets;

    public ClientDTO(String name, String DNI, String email, CashierDTO cashierWhoRegisters, List<TicketDTO> tickets) {
        super(name, email, DNI);
        this.cashierWhoRegisters = cashierWhoRegisters;
        this.tickets = (tickets == null) ? new ArrayList<>() : new ArrayList<>(tickets);
    }


    /**
     * Constructor de la clase Client.
     *
     * @param name Nombre del cliente.
     * @param DNI Identificador único del cliente (DNI/NIE).
     * @param email Correo electrónico del cliente.
     * @param cashierWhoRegisters Cajero que registró al cliente.
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
    public void addTicket(TicketDTO ticket) {
        tickets.add(ticket);
    }

    public List<TicketDTO> getTickets() {
        return new ArrayList<>(tickets);
    }
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
