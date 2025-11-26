package es.upm.iwsim22_01.models;

import java.util.ArrayList;
import java.util.List;

public class PersonalizableProduct extends UnitProduct{
    int maxPers;

    public PersonalizableProduct(int id, String name, Category category, double price, int maxPers){
        super(id, name, category, price);
        this.maxPers = maxPers;

    }

    int getMaxPers() {
        return maxPers;
    }


    public double getPriceWithPersonalization(int numTexts) {
        double base = getPrice(); // precio del producto sin personalizar
        return base * (1 + 0.10 * numTexts);
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
