package es.upm.iwsim22_01.models.product;

import java.time.LocalDateTime;

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
