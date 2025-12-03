package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.user.Cashier;

import java.util.regex.Pattern;

/**
 * Gestor de cajeros, encargado de la creación, validación y eliminación de instancias de {@link Cashier}.
 * Extiende {@link AbstractManager} para heredar funcionalidades básicas de gestión de entidades.
 */
public class CashierManager extends AbstractManager<Cashier, String> {
    private static final int CASHIER_ID_LENGTH = 7;
    private static final Pattern CASHIER_EMAIL_REGEX = Pattern.compile("^[\\w-.]+@upm.es$");

    /**
     * Añade un nuevo cajero al sistema con los datos proporcionados.
     *
     * @param name  Nombre del cajero.
     * @param email Correo electrónico del cajero (debe cumplir el formato válido).
     * @param id    Identificador único del cajero (debe cumplir el formato válido).
     * @return La instancia de {@link Cashier} creada.
     * @throws IllegalArgumentException Si el correo o el ID no cumplen el formato requerido.
     */
    public Cashier addCashier(String name, String email, String id) {
        if (!CASHIER_EMAIL_REGEX.matcher(email).find()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!checkId(id)) throw new IllegalArgumentException("Invalid ID format");

        Cashier cashier = new Cashier(name, email, id);
        add(cashier, id);
        
        return cashier;
    }

    /**
     * Añade un nuevo cajero al sistema generando automáticamente un ID único.
     *
     * @param name  Nombre del cajero.
     * @param email Correo electrónico del cajero (debe cumplir el formato válido).
     * @return La instancia de {@link Cashier} creada.
     * @throws IllegalArgumentException Si el correo no cumple el formato requerido.
     */
    public Cashier addCashier(String name, String email) {
        String id;
        do {
            id = createID();
        } while (existId(id));

        return addCashier(name, email, id);
    }

    /**
     * Elimina un cajero y todos sus tickets asociados del sistema.
     *
     * @param id             Identificador único del cajero a eliminar.
     * @param ticketManager  Gestor de tickets para eliminar los tickets asociados al cajero.
     * @return {@code true} si el cajero fue encontrado y eliminado, {@code false} en caso contrario.
     */
    public boolean removeCashierAndTickets(String id, TicketManager ticketManager) {
        Cashier cashier = get(id);

        if (cashier == null){
            return false;
        }

        cashier.getTickets().forEach(t -> ticketManager.remove(t.getId()));
        this.remove(id);

        return true;
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
