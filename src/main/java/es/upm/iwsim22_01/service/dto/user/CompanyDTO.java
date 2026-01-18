package es.upm.iwsim22_01.service.dto.user;

public class CompanyDTO extends ClientDTO{

    public CompanyDTO(String name, String id, String email, CashierDTO cashier) {
        super(name, id, email, cashier);
    }

    @Override
    public String toString() {
        return "COMPANY{identifier='" + getId() +
                "', name='" + getName() +
                "', email='" + getEmail() +
                "', cash=" + getCashier().getId() +
                "}";
    }
}
