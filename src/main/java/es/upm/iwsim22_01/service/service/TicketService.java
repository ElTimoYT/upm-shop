package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.data.repository.TicketRepository;
import es.upm.iwsim22_01.service.dto.ticket.OnlyProductsTicket;
import es.upm.iwsim22_01.service.dto.ticket.OnlyServicesTicket;
import es.upm.iwsim22_01.service.dto.ticket.ServicesAndProductsTicket;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;

/**
 * Gestor de tickets, encargado de la creación y validación de instancias de {@link AbstractTicketDTO}.
 * Permite generar tickets con identificadores únicos y validar su formato.
 */
public class TicketService extends AbstractService<Ticket, AbstractTicketDTO, Integer> {
    private static final int TICKET_ID_LENGTH = 7;

    private final ProductService productService;

    public TicketService(ProductService productService) {
        super(new TicketRepository());

        this.productService = productService;
    }

    @Override
    protected AbstractTicketDTO toDto(Ticket model) {
        return switch (model.getTicketType()) {
            case ONLY_PRODUCTS -> new OnlyProductsTicket(model.getId(), model.getInitialDate(), model.getFinalDate(), AbstractTicketDTO.TicketState.valueOf(model.getTicketState()), new ArrayList<>(model.getProducts().stream().map(productService::toDto).toList()));
            case ONLY_SERVICES -> new OnlyServicesTicket(model.getId(), model.getInitialDate(), model.getFinalDate(), AbstractTicketDTO.TicketState.valueOf(model.getTicketState()), new ArrayList<>(model.getProducts().stream().map(productService::toDto).toList()));
            case SERVICES_AND_PRODUCTS -> new ServicesAndProductsTicket(model.getId(), model.getInitialDate(), model.getFinalDate(), AbstractTicketDTO.TicketState.valueOf(model.getTicketState()), new ArrayList<>(model.getProducts().stream().map(productService::toDto).toList()));
        };

    }

    @Override
    protected Ticket toModel(AbstractTicketDTO dto) {
        return new Ticket(
                dto.getId(),
                dto.getInitialDate(),
                dto.getFinalDate(),
                dto.getState().name(),
                dto.getProducts().stream().map(productService::toModel).toList(),
                switch (dto.getTicketType()) {
                    case ONLY_PRODUCTS -> Ticket.TicketType.ONLY_PRODUCTS;
                    case ONLY_SERVICES -> Ticket.TicketType.ONLY_SERVICES;
                    case SERVICES_AND_PRODUCTS -> Ticket.TicketType.SERVICES_AND_PRODUCTS;
                }
        );
    }

    public AbstractTicketDTO addOnlyProductsTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new OnlyProductsTicket(id));
    }

    public AbstractTicketDTO addOnlyProductsTicket() {
        return addOnlyProductsTicket(createNewId());
    }

    public AbstractTicketDTO addOnlyServicesTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new OnlyServicesTicket(id));
    }

    public AbstractTicketDTO addOnlyServicesTicket() {
        return addOnlyServicesTicket(createNewId());
    }

    public AbstractTicketDTO addServicesAndProductsTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new ServicesAndProductsTicket(id));
    }

    public AbstractTicketDTO addServicesAndProductsTicket() {
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
