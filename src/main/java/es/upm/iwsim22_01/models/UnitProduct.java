package es.upm.iwsim22_01.models;

public class UnitProduct extends Product {
    private Category category;

    public UnitProduct(int id, String name, Category category, double price) {
        super(id,  name, price);

        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
