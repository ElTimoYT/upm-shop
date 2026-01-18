package es.upm.iwsim22_01.service.service;

import es.upm.iwsim22_01.data.models.Cashier;
import es.upm.iwsim22_01.data.repository.CashierRepository;
import es.upm.iwsim22_01.service.dto.ticket.AbstractTicketDTO;
import es.upm.iwsim22_01.service.dto.user.CashierDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Gestor de cajeros, encargado de la creación, validación y eliminación de instancias de {@link CashierDTO}.
 * Extiende {@link AbstractService} para heredar funcionalidades básicas de gestión de entidades.
 */
public class CashierService extends AbstractService<Cashier, CashierDTO, String> {
    private static final int CASHIER_ID_LENGTH = 7;
    private static final Pattern CASHIER_EMAIL_REGEX = Pattern.compile("^[\\w-.]+@upm.es$");

    private final TicketService ticketService;

    public CashierService(TicketService ticketService) {
        super(new CashierRepository());

        this.ticketService = ticketService;
    }

    @Override
    protected CashierDTO toDto(Cashier model) {
        List<AbstractTicketDTO> tickets = new ArrayList<>();

        if (model.getTicketsId() != null) {
            for (Integer tid : model.getTicketsId()) {
                if (tid != null && ticketService.existsId(tid)) {
                    tickets.add(ticketService.get(tid));
                }
            }
        }

        return new CashierDTO(
                model.getName(),
                model.getEmail(),
                model.getDNI(),
                tickets
        );
    }

    @Override
    protected Cashier toModel(CashierDTO dto) {
        return new Cashier(
                dto.getName(),
                dto.getEmail(),
                dto.getId(),
                dto.getTickets()
                        .stream()
                        .map(AbstractTicketDTO::getId)
                        .toList()
        );
    }

    /**
     * Añade un nuevo cajero al sistema con los datos proporcionados.
     *
     * @param name  Nombre del cajero.
     * @param email Correo electrónico del cajero (debe cumplir el formato válido).
     * @param id    Identificador único del cajero (debe cumplir el formato válido).
     * @return La instancia de {@link CashierDTO} creada.
     * @throws IllegalArgumentException Si el correo o el ID no cumplen el formato requerido.
     */
    public CashierDTO addCashier(String name, String email, String id) {
        if (!CASHIER_EMAIL_REGEX.matcher(email).find()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!checkId(id)) throw new IllegalArgumentException("Invalid ID format");

        return add(new CashierDTO(name, email, id));
    }

    /**
     * Añade un nuevo cajero al sistema generando automáticamente un ID único.
     *
     * @param name  Nombre del cajero.
     * @param email Correo electrónico del cajero (debe cumplir el formato válido).
     * @return La instancia de {@link CashierDTO} creada.
     * @throws IllegalArgumentException Si el correo no cumple el formato requerido.
     */
    public CashierDTO addCashier(String name, String email) {
        String id;
        do {
            id = createID();
        } while (existsId(id));

        return addCashier(name, email, id);
    }

    /**
     * Valida el formato de un identificador de cajero.
     * El formato válido es: "UW" seguido de 7 dígitos numéricos.
     *
     * @param id Identificador a validar.
     * @return {@code true} si el formato es válido, {@code false} en caso contrario.
     */
    public boolean checkId(String id){
        String regex = "(?i)^UW\\d{" + CASHIER_ID_LENGTH + "}$";
        return Pattern.matches(regex, id);
    }

    /**
     * Genera un identificador único para un cajero.
     * El formato generado es: "UW" seguido de 7 dígitos numéricos (rellenando con ceros a la izquierda si es necesario).
     *
     * @return Identificador único generado.
     */
    private String createID() {
        int num = (int) (Math.random() * Math.pow(10, CASHIER_ID_LENGTH));
        StringBuilder preemptiveID = new  StringBuilder(String.valueOf(num));

        for (int i = preemptiveID.length(); i < CASHIER_ID_LENGTH; i++) {
            preemptiveID.insert(0, '0');
        }

        preemptiveID.insert(0, "UW");

        return preemptiveID.toString();
    }
}
