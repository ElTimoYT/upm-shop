package es.upm.iwsim22_01.service.dto.user;

/**
 * Clase que representa una empresa como un tipo especial de cliente del sistema.
 */
public class CompanyDTO extends ClientDTO{
    /**
     * Crea una empresa asociada a un cajero que la registró.
     *
     * @param name nombre de la empresa
     * @param id identificador único de la empresa
     * @param email correo electrónico de la empresa
     * @param cashier cajero que registró la empresa
     */
    public CompanyDTO(String name, String id, String email, CashierDTO cashier) {
        super(name, id, email, cashier);
    }

    /**
     * Devuelve una representación textual de la empresa con sus datos básicos.
     *
     * @return representación textual de la empresa
     */
    @Override
    public String toString() {
        return "COMPANY{identifier='" + getId() +
                "', name='" + getName() +
                "', email='" + getEmail() +
                "', cash=" + getCashier().getId() +
                "}";
    }
}
