package es.upm.iwsim22_01.models;

import java.util.regex.Pattern;

public class Client extends User {

    private Cashier cashierWhoRegisters;

    public Client(String name, String DNI, String email, Cashier cashierWhoRegisters) {
        super(name, email, DNI);
        this.cashierWhoRegisters = cashierWhoRegisters;
    }

    public Cashier getCashier() {
        return cashierWhoRegisters;
    }

    private void remove(String DNI) {

    }

    private static final Pattern REGEX = Pattern.compile("^(?![\\w.-]+@upm\\.es$)[\\w.-]+@([\\w-]+\\.)+[\\w-]+$");

    @Override
    protected boolean checkEmail(String email) {
        return REGEX.matcher(email).find();
    }


}
