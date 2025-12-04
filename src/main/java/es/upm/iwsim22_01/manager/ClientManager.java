package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.user.Cashier;
import es.upm.iwsim22_01.models.user.Client;

import java.util.regex.Pattern;

/**
 * Gestor de clientes, encargado de la creación, validación y registro de instancias de {@link Client}.
 * Valida el formato del DNI/NIE, el correo electrónico y la existencia del cajero que realiza el registro.
 */
public class ClientManager extends AbstractManager<Client, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+@\\w+\\.\\w+$"),
        DNI_PATTERN = Pattern.compile("^[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]$"),
        NIE_PATTERN = Pattern.compile("^[XYZ][0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]$");

    private final CashierManager cashierManager;

    /**
     * Constructor de la clase.
     *
     * @param cashierManager Gestor de cajeros necesario para validar la existencia del cajero que registra al cliente.
     */
    public ClientManager(CashierManager cashierManager) {
        this.cashierManager = cashierManager;
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
     * @return La instancia de {@link Client} creada.
     * @throws IllegalArgumentException Si el cajero no existe, el correo no es válido o el DNI/NIE no es válido.
     */
    public Client addClient(String name, String DNI, String email, String cashierWhoRegistersId) {
        if (!cashierManager.existId(cashierWhoRegistersId)) throw new IllegalArgumentException("No cashier found with such an id");
        if (!checkEmail(email)) throw new IllegalArgumentException("Email is not valid");
        if (!checkDNI(DNI)) throw new IllegalArgumentException("Invalid DNI");

        Cashier cashier = cashierManager.get(cashierWhoRegistersId);

        Client client = new  Client(name, DNI, email, cashier);
        add(client, DNI);

        return client;
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
