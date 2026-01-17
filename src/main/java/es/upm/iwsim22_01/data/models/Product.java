package es.upm.iwsim22_01.data.models;

import java.time.LocalDateTime;

public class Product {
    private int id;
    private String name;
    private double price;
    private int amount;

    private ProductType type;

    //Para UnitProduct y PersonalizableProduct
    private String category;

    //Para PersonalizableProduct
    private String[] lines;

    //Para servicios
    private Integer maxParticipant;
    private LocalDateTime expirationDate;
    private Integer participantsAmount;

    public Product(ProductType type, int id, String name, double price, int amount, String category, String[] lines, Integer maxParticipant, LocalDateTime expirationDate, Integer participantsAmount) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.category = category;
        this.lines = lines;
        this.maxParticipant = maxParticipant;
        this.expirationDate = expirationDate;
        this.participantsAmount = participantsAmount;
    }

    public static Product createUnit(int id, String name, String category, double price, int amount) {
        return new Product(ProductType.UNIT_PRODUCT, id, name, price, amount, category, null, null, null, null);
    }

    public static Product createPersonalizable(int id, String name, String category, double price, int amount, String[] lines) {
        return new Product(ProductType.PERSONALIZABLE_PRODUCT, id, name, price, amount, category, lines, null, null, null);
    }

    public static Product createCatering(int id, String name, double price, int amount, int maxParticipant, LocalDateTime expirationDate, int participantsAmount) {
        return new Product(ProductType.CATERING, id, name, price, amount, null, null, maxParticipant, expirationDate, participantsAmount);
    }

    public static Product createMeeting(int id, String name, double price, int amount, int maxParticipant, LocalDateTime expirationDate, int participantsAmount) {
        return new Product(ProductType.MEETING, id, name, price, amount, null, null, maxParticipant, expirationDate, participantsAmount);
    }

    public static Product createService(int id, LocalDateTime expirationDate,String category) {
        return new Product(ProductType.SERVICE, id, "",0,0,category,null,0,expirationDate,null);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ProductType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public Integer getMaxParticipant() {
        return maxParticipant;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public Integer getParticipantsAmount() {
        return participantsAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;

        if (o instanceof Product product) {
            return product.getId() == this.getId();
        }

        return false;
    }

    public enum ProductType {
        UNIT_PRODUCT,
        PERSONALIZABLE_PRODUCT,
        CATERING,
        MEETING,
        SERVICE;
    }
}
