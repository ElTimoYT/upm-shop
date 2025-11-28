package es.upm.iwsim22_01.models.product;

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

    @Override
    public String toString() {
        return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                ",id:" + id +
                ",name:'" + name + '\'' +
                ",price:" + price +
                ",Category: "+category+
                '}';

    }
}
