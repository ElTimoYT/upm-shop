package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Ticket;

/**
 * Gestor de tickets, encargado de la creación y validación de instancias de {@link Ticket}.
 * Permite generar tickets con identificadores únicos y validar su formato.
 */
public class TicketManager extends AbstractManager<Ticket, Integer> {
    private static final int TICKET_ID_LENGTH = 7;

    /**
     * Añade un nuevo ticket al sistema con el identificador especificado.
     *
     * @param id Identificador único del ticket (debe ser un número positivo con un máximo de {@value #TICKET_ID_LENGTH} dígitos).
     * @return La instancia de {@link Ticket} creada.
     * @throws IllegalArgumentException Si el formato del identificador no es válido.
     */
    public Ticket addTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        Ticket ticket = new Ticket(id);

        add(ticket, id);
        return ticket;
    }

    /**
     * Añade un nuevo ticket al sistema generando automáticamente un identificador único.
     *
     * @return La instancia de {@link Ticket} creada.
     */
    public Ticket addTicket() {
        return addTicket(createNewId());
    }

    /**
     * Valida el formato de un identificador de ticket.
     * El identificador debe ser un número positivo con un máximo de {@value #TICKET_ID_LENGTH} dígitos.
     *
     * @param id Identificador a validar.
     * @return {@code true} si el formato es válido, {@code false} en caso contrario.
     */
    public boolean checkId(int id){
        return Math.log10(id) <= TICKET_ID_LENGTH;
    }

    /**
     * Genera un nuevo identificador único para un ticket.
     * El identificador generado será un número aleatorio de hasta {@value #TICKET_ID_LENGTH} dígitos.
     *
     * @return Identificador único generado.
     */
    private int createNewId() {
        int id;

        do {
            id = (int) (Math.random() * Math.pow(10, TICKET_ID_LENGTH));
        } while (existId(id));

        return id;
    }
}
