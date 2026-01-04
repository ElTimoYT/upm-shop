package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.data.repository.TicketRepository;
import es.upm.iwsim22_01.service.dto.TicketDTO;

import java.util.ArrayList;

/**
 * Gestor de tickets, encargado de la creación y validación de instancias de {@link TicketDTO}.
 * Permite generar tickets con identificadores únicos y validar su formato.
 */
public class TicketService extends AbstractService<Ticket, TicketDTO, Integer> {
    private static final int TICKET_ID_LENGTH = 7;

    private final ProductService productService;

    public TicketService(ProductService productService) {
        super(new TicketRepository());

        this.productService = productService;
    }

    @Override
    protected TicketDTO toDto(Ticket model) {
        return new TicketDTO(
                model.getId(),
                model.getInitialDate(),
                model.getFinalDate(),
                TicketDTO.TicketState.valueOf(model.getTicketState()),
                new ArrayList<>(model.getProducts()
                        .stream()
                        .map(productService::toDto)
                        .toList())
        );
    }

    @Override
    protected Ticket toModel(TicketDTO dto) {
        return new Ticket(
                dto.getId(),
                dto.getInitialDate(),
                dto.getFinalDate(),
                dto.getState().toString(),
                dto.getProducts()
                        .stream()
                        .map(productService::toModel)
                        .toList()
        );
    }

    /**
     * Añade un nuevo ticket al sistema con el identificador especificado.
     *
     * @param id Identificador único del ticket (debe ser un número positivo con un máximo de {@value #TICKET_ID_LENGTH} dígitos).
     * @return La instancia de {@link TicketDTO} creada.
     * @throws IllegalArgumentException Si el formato del identificador no es válido.
     */
    public TicketDTO addTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new TicketDTO(id));
    }

    /**
     * Añade un nuevo ticket al sistema generando automáticamente un identificador único.
     *
     * @return La instancia de {@link TicketDTO} creada.
     */
    public TicketDTO addTicket() {
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
        } while (existsId(id));

        return id;
    }
}
