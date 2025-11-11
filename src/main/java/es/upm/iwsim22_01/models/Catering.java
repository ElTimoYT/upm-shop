package es.upm.iwsim22_01.models;

import java.time.LocalDate;

public class Catering extends ProductService{
    public Catering(int id, String name, double pricePerPerson, int maxPers, LocalDate expirationDate){
        super(id,name, pricePerPerson, maxPers,expirationDate);
    }
}
