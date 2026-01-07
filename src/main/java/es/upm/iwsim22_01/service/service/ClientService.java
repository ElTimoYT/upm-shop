package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Client;
import es.upm.iwsim22_01.data.repository.ClientRepository;
import es.upm.iwsim22_01.service.dto.ticket.TicketDTO;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Gestor de clientes, encargado de la creación, validación y registro de instancias de {@link ClientDTO}.
 * Valida el formato del DNI/NIE, el correo electrónico y la existencia del cajero que realiza el registro.
 */
public class ClientService extends AbstractService<Client, ClientDTO, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+@\\w+\\.\\w+$"),
        DNI_PATTERN = Pattern.compile("^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$"),
        NIE_PATTERN = Pattern.compile("^[XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]$");

    private final CashierService cashierService;
    private final TicketService ticketService;

    /**
     * Constructor de la clase.
     *
     * @param cashierService Gestor de cajeros necesario para validar la existencia del cajero que registra al cliente.
     */
    public ClientService(CashierService cashierService, TicketService ticketService) {
        super(new ClientRepository());
        this.cashierService = cashierService;
        this.ticketService = ticketService;
    }

    @Override
    protected ClientDTO toDto(Client model) {
        return new ClientDTO(
                model.getName(),
                model.getDNI(),
                model.getEmail(),
                cashierService.get(model.getCashierWhoRegisters()),
                new ArrayList<>(model.getTicketsId()
                        .stream()
                        .map(ticketService::get)
                        .toList()
                )
        );
    }

    @Override
    protected Client toModel(ClientDTO dto) {
        return new Client(
                dto.getName(),
                dto.getId(),
                dto.getEmail(),
                dto.getCashier().getId(),
                dto.getTickets()
                        .stream()
                        .map(TicketDTO::getId)
                        .toList()
        );
    }

    /**
     * Añade un nuevo cliente al sistema.
     * Valida que el cajero que realiza el registro exista, que el correo electrónico sea válido,
     * y que el DNI/NIE tenga un formato correcto.
     *
     * @param name                  Nombre del cliente.
     * @param DNI                   DNI o NIE del cliente (debe cumplir el formato válido).
     * @param email                 Correo electrónico del cliente (debe cumplir el formato válido).
     * @param cashierWhoRegistersId Identificador del cajero que realiza el registro.
     * @return La instancia de {@link ClientDTO} creada.
     * @throws IllegalArgumentException Si el cajero no existe, el correo no es válido o el DNI/NIE no es válido.
     */
    public ClientDTO addClient(String name, String DNI, String email, String cashierWhoRegistersId) {
        if (!cashierService.existsId(cashierWhoRegistersId)) throw new IllegalArgumentException("No cashier found with such an id");
        if (!checkEmail(email)) throw new IllegalArgumentException("Email is not valid");
        if (!checkDNI(DNI)) throw new IllegalArgumentException("Invalid DNI");

        CashierDTO cashier = cashierService.get(cashierWhoRegistersId);

        return add(new ClientDTO(name, DNI, email, cashier));
    }

    /**
     * Valida el formato de un DNI o NIE.
     * Acepta tanto DNI español (8 dígitos + letra) como NIE (X/Y/Z + 7 dígitos + letra).
     *
     * @param dni DNI o NIE a validar.
     * @return {@code true} si el formato es válido, {@code false} en caso contrario.
     */
    public boolean checkDNI(String dni) {
        return DNI_PATTERN.matcher(dni).find() || NIE_PATTERN.matcher(dni).find();
    }

    /**
     * Valida el formato de un correo electrónico.
     * Acepta direcciones con el formato estándar: usuario@dominio.extensión.
     *
     * @param email Correo electrónico a validar.
     * @return {@code true} si el formato es válido, {@code false} en caso contrario.
     */
    public boolean checkEmail(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }
}
