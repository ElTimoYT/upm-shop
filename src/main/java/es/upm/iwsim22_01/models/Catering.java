package es.upm.iwsim22_01.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Catering extends ProductService {
    public Catering(int id, String name, double pricePerPerson, int maxPers, LocalDateTime expirationDate){
        super(id,name, pricePerPerson, maxPers, expirationDate);
    }

    @Override
    public boolean checkTime() {
        if (!super.checkTime()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minFoodDateTime = now.plusDays(3);
        return getExpirationDate().isBefore(minFoodDateTime);
    }

}
