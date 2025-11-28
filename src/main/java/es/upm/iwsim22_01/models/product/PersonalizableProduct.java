package es.upm.iwsim22_01.models.product;

public class PersonalizableProduct extends UnitProduct{
    int maxPers;

    public PersonalizableProduct(int id, String name, Category category, double price, int maxPers){
        super(id, name, category, price);
        this.maxPers = maxPers;

    }

    public int getMaxPers() {
        return maxPers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{class:ProductPersonalized")
                .append(", id:").append(getId())
                .append(", name:'").append(getName()).append('\'')
                .append(", category:").append(getCategory())
                .append(", price:").append(getPrice())
                .append(", maxPersonal:").append(maxPers);

        sb.append("}");
        return sb.toString();
    }

}
