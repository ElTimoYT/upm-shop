package es.upm.iwsim22_01.service.inventory;

import es.upm.iwsim22_01.data.models.Ticket;
import es.upm.iwsim22_01.data.repository.TicketRepository;
import es.upm.iwsim22_01.service.dto.ticket.CommonTicketDTO;
import es.upm.iwsim22_01.service.dto.ticket.CompanyTicketDTO;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;
import es.upm.iwsim22_01.service.dto.user.CompanyDTO;
import es.upm.iwsim22_01.service.printer.ProductTicketPrinter;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor de tickets, encargado de la creación y validación de instancias de {@link TicketDTO}.
 * Permite generar tickets con identificadores únicos y validar su formato.
 */
public class TicketInventory extends AbstractInventory<Ticket, TicketDTO, Integer> {
    private static final int TICKET_ID_LENGTH = 7;

    private final ProductInventory productService;

    public TicketInventory(ProductInventory productService) {
        super(new TicketRepository());
        this.productService = productService;
    }
    private final Map<Integer, TicketPrinter> printersByTicketId = new HashMap<>();

    public void setPrinterForTicket(int ticketId, TicketPrinter printer) {
        if (printer == null) return;
        printersByTicketId.put(ticketId, printer);
    }

    public TicketPrinter getPrinterForTicket(int ticketId) {
        // fallback razonable
        return printersByTicketId.getOrDefault(ticketId, new ProductTicketPrinter());
    }
    @Override
    protected TicketDTO toDto(Ticket model) {
        TicketDTO.TicketType type = TicketDTO.TicketType.valueOf(model.getTicketType());

        var products = new ArrayList<>(model.getProducts().stream()
                .map(productService::toDto)
                .toList());

        TicketDTO.TicketState state = TicketDTO.TicketState.valueOf(model.getTicketState());
        return switch (type) {
            case COMPANY -> new CompanyTicketDTO(
                    model.getId(),
                    model.getInitialDate(),
                    model.getFinalDate(),
                    state,
                    products
            );
            case COMMON -> new CommonTicketDTO(
                    model.getId(),
                    model.getInitialDate(),
                    model.getFinalDate(),
                    state,
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
                dto.getState().name(),
                dto.getProducts().stream().map(productService::toModel).toList(),
                dto.getTicketType().name()
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

    public TicketDTO addTicketForClient(ClientDTO client) {
        int id = createNewId();
        return addTicketForClient(client, id);
    }

    public TicketDTO addTicketForClient(ClientDTO client, int ticketId) {
        TicketDTO ticket = createTicketByClientType(client, ticketId);
        return add(ticket);
    }

    private TicketDTO createTicketByClientType(ClientDTO client, int ticketId) {
        if (client instanceof CompanyDTO) {
            return new CompanyTicketDTO(ticketId);
        }
        return new CommonTicketDTO(ticketId);
    }
}
