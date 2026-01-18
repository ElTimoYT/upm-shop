package es.upm.iwsim22_01.service.dto.user;

/**
 * Clase que representa a un usuario final como un tipo concreto de cliente del sistema.
 */
public class UserDTO extends ClientDTO{
    /**
     * Crea un usuario asociado a un cajero que lo registró.
     *
     * @param name nombre del usuario
     * @param id identificador único del usuario
     * @param email correo electrónico del usuario
     * @param cashier cajero que registró al usuario
     */
    public UserDTO(String name, String id, String email, CashierDTO cashier) {
        super(name, id, email, cashier);
    }

    /**
     * Devuelve una representación textual del usuario con sus datos básicos.
     *
     * @return representación textual del usuario
     */
    @Override
    public String toString() {
        return "USER{identifier='" + getId() +
                "', name='" + getName() +
                "', email='" + getEmail() +
                "', cash=" + getCashier().getId() +
                "}";
    }
}
