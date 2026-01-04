package es.upm.iwsim22_01.service.dto.user;

import es.upm.iwsim22_01.service.dto.TicketDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un cliente en el sistema, especialización de AbstractUser.
 * Un cliente está asociado a un cajero que lo registró y puede tener múltiples tickets.
 */
public class ClientDTO extends AbstractUserDTO {
    private CashierDTO cashierWhoRegisters;
    private List<TicketDTO> tickets = new ArrayList<>();

    /**
     * Constructor de la clase Client.
     *
     * @param name Nombre del cliente.
     * @param DNI Identificador único del cliente (DNI/NIE).
     * @param email Correo electrónico del cliente.
     * @param cashierWhoRegisters Cajero que registró al cliente.
     */
    public ClientDTO(String name, String DNI, String email, CashierDTO cashierWhoRegisters) {
        super(name, email, DNI);
        this.cashierWhoRegisters = cashierWhoRegisters;
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

    /**
     * Devuelve una representación en cadena del cliente.
     *
     * @return Cadena que representa al cliente, incluyendo su identificador, nombre, correo y cajero asociado.
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + getDNI() +
                ",name=" + getName() +
                ",email=" + getEmail() +
                ",cash=" + cashierWhoRegisters.getDNI() +
                '}';
    }
}
