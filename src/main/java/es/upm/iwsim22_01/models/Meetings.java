package es.upm.iwsim22_01.models;

import java.time.LocalDate;

public class Meetings extends ProductService{
    public Meetings(int id, String name, double pricePerPerson, int maxPers, LocalDate expirationDate){
    super(id,name, pricePerPerson,maxPers,expirationDate);
    }
}
