package es.upm.iwsim22_01.service.dto.user;

import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un cajero en el sistema, especialización de AbstractUser.
 * Un cajero puede gestionar múltiples tickets asociados a su cuenta.
 */
public class CashierDTO extends AbstractUserDTO {
    private List<TicketDTO> tickets;

    public CashierDTO(String name, String email, String id, List<TicketDTO> tickets) {
        super(name, email , id);
        this.tickets = tickets;
    }

    /**
     * Constructor de la clase Cashier.
     *
     * @param name Nombre del cajero.
     * @param email Correo electrónico del cajero.
     * @param id Identificador único del cajero.
     */
    public CashierDTO(String name, String email, String id) {
        this(name, email , id, new ArrayList<>());
    }

    /**
     * Obtiene la lista de tickets asociados al cajero.
     *
     * @return Lista de tickets del cajero.
     */
    public List<TicketDTO> getTickets() {
        return tickets;
    }

    /**
     * Añade un ticket a la lista de tickets del cajero.
     *
     * @param ticket Ticket a añadir.
     */
    public void addTicket(TicketDTO ticket) {
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

    /**
     * Compara este cajero con otro objeto para determinar si son iguales.
     * Utiliza la implementación de la clase padre.
     *
     * @param object Objeto con el que comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    /**
     * Genera un código hash para el cajero.
     * Utiliza la implementación de la clase padre.
     *
     * @return Código hash del cajero.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
