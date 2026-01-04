package es.upm.iwsim22_01.data.models;

public class Client {
    private String name;
    private String email;
    private String dni;
    private String cashierWhoRegisters;

    public Client(String name, String dni, String email, String cashierWhoRegisters) {
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.cashierWhoRegisters = cashierWhoRegisters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDNI() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCashierWhoRegisters() {
        return cashierWhoRegisters;
    }

    public void setCashierWhoRegisters(String cashierWhoRegisters) {
        this.cashierWhoRegisters = cashierWhoRegisters;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;

        if (o instanceof Client client) {
            return client.getDNI().equals(this.getDNI());
        }

        return false;
    }
}
