package es.upm.iwsim22_01.models;

public class PersonalizableProduct extends Product{
    int maxPers;

    public PersonalizableProduct(int id, String name, Category category, double price, int maxPers){
        super(id, name, category, price);
        this.maxPers = maxPers;
    }
    int getMaxPers(){return maxPers;}
}
