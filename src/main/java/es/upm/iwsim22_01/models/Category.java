package es.upm.iwsim22_01.models;

public enum Category {
    MERCH(0),
    PAPELERIA(0.05),
    ROPA(0.07),
    LIBRO(0.1),
    ELECTRONICA(0.03);

    private final double discount;

    Category(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return this.discount;
    }
}
