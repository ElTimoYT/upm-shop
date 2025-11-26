package es.upm.iwsim22_01.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Meetings extends ProductService{
    public Meetings(int id, String name, double pricePerPerson, int maxPers, LocalDateTime expirationDate){
    super(id,name, pricePerPerson,maxPers,expirationDate);
    }
    @Override
    public ServiceType getServiceType() {return ServiceType.MEETING;}
}
