package es.upm.iwsim22_01.models.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meeting extends ProductService{
    public Meeting(int id, String name, double pricePerPerson, int maxPers, LocalDateTime expirationDate){
        super(id,name, pricePerPerson,maxPers,expirationDate);
    }

    @Override
    public boolean checkTime() {
        if (!super.checkTime()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        LocalDateTime minMeetingDateTime = now.plusHours(12);
        if (getExpirationDate().isBefore(minMeetingDateTime)) {
            return false;
        }

        LocalDate serviceDate = getExpirationDate().toLocalDate();
        if (serviceDate.isEqual(today.plusDays(1))) {
            LocalDateTime todayNoon = LocalDateTime.of(today, LocalTime.NOON);
            return !now.isAfter(todayNoon);
        }

        return true;
    }
}
