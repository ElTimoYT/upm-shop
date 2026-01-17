package es.upm.iwsim22_01.service.dto.user;

import java.util.List;

public class UserDTO extends ClientDTO{
    public UserDTO(String name, String id, String email, CashierDTO cashier) {
        super(name, id, email, cashier);
    }

    @Override
    public String toString() {
        return "USER{identifier='" + getId() +
                "', name='" + getName() +
                "', email='" + getEmail() +
                "', cash=" + getCashier().getId() +
                "}";
    }
}
