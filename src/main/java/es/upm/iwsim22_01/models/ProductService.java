package es.upm.iwsim22_01.models;

import java.time.LocalDate;

public class ProductService extends Product{

    private int MAX_PARTICIPANT;
    private LocalDate expirationDate;


    public ProductService(int id, String name, double price,int MAX_PARTICIPANT,LocalDate expirationDate){
        super(id, name, Category.SERVICE,price);
        this.MAX_PARTICIPANT =MAX_PARTICIPANT;
        this.expirationDate = expirationDate;
    }

    public int getMAX_PARTICIPANT(){return MAX_PARTICIPANT;}
    public LocalDate getExpirationDate(){return expirationDate;}
    @Override
    public String toString() {
        return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                ",id:" + getId() +
                ",name:'" + getName() + '\'' +
                ",category:" + getCategory() +
                ",price:" + getPrice() +
                ",max_participant:" + MAX_PARTICIPANT +
                ",expiration:" + expirationDate +
                '}';
    }
}
