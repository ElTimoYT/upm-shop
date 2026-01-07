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
        this.tickets = tickets;
    }

    private Client.ClientType clientType;

    public Client.ClientType getClientType() {
        return clientType;
    }

    public void setClientType(Client.ClientType clientType) {
        this.clientType = clientType;
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

    /**
     * Devuelve una representación en cadena del cliente.
     *
     * @return Cadena que representa al cliente, incluyendo su identificador, nombre, correo y cajero asociado.
     */
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
