package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.data.repository.TicketRepository;
import es.upm.iwsim22_01.service.dto.ticket.CommonTicketDTO;
import es.upm.iwsim22_01.service.dto.ticket.CompanyTicketDTO;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;

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
        TicketDTO.TicketType type = TicketDTO.TicketType.valueOf(model.getTicketType());

        var products = new ArrayList<>(model.getProducts()
                .stream()
                .map(productService::toDto)
                .toList());

        return switch (type) {
            case COMMON -> new es.upm.iwsim22_01.service.dto.ticket.CommonTicketDTO(
                    model.getId(),
                    model.getInitialDate(),
                    model.getFinalDate(),
                    TicketDTO.TicketState.valueOf(model.getTicketState()),
                    products
            );
            case COMPANY -> new es.upm.iwsim22_01.service.dto.ticket.CompanyTicketDTO(
                    model.getId(),
                    model.getInitialDate(),
                    model.getFinalDate(),
                    TicketDTO.TicketState.valueOf(model.getTicketState()),
                    products
            );
        };
    }

    @Override
    protected Ticket toModel(TicketDTO dto) {
        return new Ticket(
                dto.getId(),
                dto.getInitialDate(),
                dto.getFinalDate(),
                dto.getState().toString(),
                dto.getProducts().stream().map(productService::toModel).toList(),
                dto.getTicketType().toString()
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

        // Por defecto (cuando el comando no especifica tipo), creamos un ticket común.
        // Esto evita instancias con ticketType == null que luego romperían en toModel().
        return add(new CommonTicketDTO(id));
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
    public int createNewId() {
        int id;

        do {
            id = (int) (Math.random() * Math.pow(10, TICKET_ID_LENGTH));
        } while (existsId(id));

        return id;
    }

    public TicketDTO addCommonTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");
        return add(new CommonTicketDTO(id));
    }

    public TicketDTO addCompanyTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");
        return add(new CompanyTicketDTO(id));
    }

    public TicketDTO addCommonTicket() {
        return addCommonTicket(createNewId());
    }

    public TicketDTO addCompanyTicket() {
        return addCompanyTicket(createNewId());
    }
}
