package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.data.repository.TicketRepository;
import es.upm.iwsim22_01.service.dto.ticket.OnlyProductsTicket;
import es.upm.iwsim22_01.service.dto.ticket.OnlyServicesTicket;
import es.upm.iwsim22_01.service.dto.ticket.ServicesAndProductsTicket;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;
import es.upm.iwsim22_01.service.dto.user.CompanyDTO;

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

        String typeStr = model.getTicketType();
        TicketDTO.TicketType type = (typeStr == null || typeStr.isBlank())
                ? TicketDTO.TicketType.COMMON   // default para tickets antiguos
                : TicketDTO.TicketType.valueOf(typeStr);

        return new TicketDTO(
                model.getId(),
                model.getInitialDate(),
                model.getFinalDate(),
                TicketDTO.TicketState.valueOf(model.getTicketState()),
                model.getProducts().stream().map(productService::toDto).toList(),
                type
        );

    }

    @Override
    protected Ticket toModel(TicketDTO dto) {
        if (dto == null) throw new IllegalArgumentException("TicketDTO is null");
        if (dto.getTicketType() == null) {
            throw new IllegalStateException("TicketDTO.ticketType is null BEFORE saving, id=" + dto.getId());
        }

        if (dto.getTicketType() == null) {
            throw new IllegalStateException("TicketDTO.ticketType is null for id=" + dto.getId());
        }

        return new Ticket(
                dto.getId(),
                dto.getInitialDate(),
                dto.getFinalDate(),
                dto.getState().name(),
                dto.getProducts().stream().map(productService::toModel).toList(),
                dto.getTicketType().name()
        );
    }

    public TicketDTO addOnlyProductsTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new OnlyProductsTicket(id));
    }

    public TicketDTO addOnlyProductsTicket() {
        return addOnlyProductsTicket(createNewId());
    }

    public TicketDTO addOnlyServicesTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new OnlyServicesTicket(id));
    }

    public TicketDTO addOnlyServicesTicket() {
        return addOnlyServicesTicket(createNewId());
    }

    public TicketDTO addServicesAndProductsTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new ServicesAndProductsTicket(id));
    }

    public TicketDTO addServicesAndProductsTicket() {
        return addServicesAndProductsTicket(createNewId());
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
}
