package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.data.repository.TicketRepository;
import es.upm.iwsim22_01.service.dto.ticket.OnlyProductsTicket;
import es.upm.iwsim22_01.service.dto.ticket.OnlyServicesTicket;
import es.upm.iwsim22_01.service.dto.ticket.ServicesAndProductsTicket;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;

import java.util.ArrayList;

/**
 * Servicio encargado de la gestión de tickets incluyendo creación, validación y conversión entre modelo y DTO.
 */
public class TicketService extends AbstractService<Ticket, AbstractTicketDTO, Integer> {
    private static final int TICKET_ID_LENGTH = 7;

    private final ProductService productService;

    /**
     * Crea el servicio de tickets utilizando el servicio de productos.
     *
     * @param productService servicio de productos asociado
     */
    public TicketService(ProductService productService) {
        super(new TicketRepository());

        this.productService = productService;
    }

    /**
     * Convierte un modelo Ticket en su correspondiente DTO según el tipo de ticket.
     *
     * @param model modelo de ticket
     * @return DTO del ticket
     */
    @Override
    protected AbstractTicketDTO toDto(Ticket model) {
        return switch (model.getTicketType()) {
            case ONLY_PRODUCTS -> new OnlyProductsTicket(model.getId(), model.getInitialDate(), model.getFinalDate(), AbstractTicketDTO.TicketState.valueOf(model.getTicketState()), new ArrayList<>(model.getProducts().stream().map(productService::toDto).toList()));
            case ONLY_SERVICES -> new OnlyServicesTicket(model.getId(), model.getInitialDate(), model.getFinalDate(), AbstractTicketDTO.TicketState.valueOf(model.getTicketState()), new ArrayList<>(model.getProducts().stream().map(productService::toDto).toList()));
            case SERVICES_AND_PRODUCTS -> new ServicesAndProductsTicket(model.getId(), model.getInitialDate(), model.getFinalDate(), AbstractTicketDTO.TicketState.valueOf(model.getTicketState()), new ArrayList<>(model.getProducts().stream().map(productService::toDto).toList()));
        };

    }

    /**
     * Convierte un DTO de ticket en su modelo de dominio correspondiente.
     *
     * @param dto DTO del ticket
     * @return modelo del ticket
     */
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

    /**
     * Crea un ticket que solo admite productos.
     *
     * @param id identificador del ticket
     * @return DTO del ticket creado
     */
    public AbstractTicketDTO addOnlyProductsTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new OnlyProductsTicket(id));
    }

    /**
     * Crea un ticket de solo productos con identificador generado automáticamente.
     *
     * @return DTO del ticket creado
     */
    public AbstractTicketDTO addOnlyProductsTicket() {
        return addOnlyProductsTicket(createNewId());
    }

    /**
     * Crea un ticket que solo admite servicios.
     *
     * @param id identificador del ticket
     */
    public AbstractTicketDTO addOnlyServicesTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new OnlyServicesTicket(id));
    }

    /**
     * Crea un ticket de solo servicios con identificador generado automáticamente.
     *
     * @return DTO del ticket creado
     */
    public AbstractTicketDTO addOnlyServicesTicket() {
        return addOnlyServicesTicket(createNewId());
    }

    /**
     * Crea un ticket que admite servicios y productos.
     *
     * @param id identificador del ticket
     * @return DTO del ticket creado
     */
    public AbstractTicketDTO addServicesAndProductsTicket(int id) {
        if (!checkId(id)) throw new IllegalArgumentException("Id format not valid");

        return add(new ServicesAndProductsTicket(id));
    }

    /**
     * Crea un ticket combinado con identificador generado automáticamente.
     *
     * @return DTO del ticket creado
     */
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
