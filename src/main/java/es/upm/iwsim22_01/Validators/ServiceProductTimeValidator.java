package es.upm.iwsim22_01.Validators;

import es.upm.iwsim22_01.models.ProductService;
import es.upm.iwsim22_01.models.ServiceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ServiceProductTimeValidator {

    private ServiceProductTimeValidator() {}

    public static boolean isValid(ProductService ps) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDateTime serviceDateTime = ps.getExpirationDate();

        if (serviceDateTime.isBefore(now)) {
            return false;
        }

        if (ps.getServiceType() == ServiceType.FOOD) {
            LocalDateTime minFoodDateTime = now.plusDays(3);
            return !serviceDateTime.isBefore(minFoodDateTime);
        }

        if (ps.getServiceType() == ServiceType.MEETING) {
            LocalDateTime minMeetingDateTime = now.plusHours(12);
            if (serviceDateTime.isBefore(minMeetingDateTime)) {
                return false;
            }
            LocalDate serviceDate = serviceDateTime.toLocalDate();
            if (serviceDate.isEqual(today.plusDays(1))) {
                LocalDateTime todayNoon = LocalDateTime.of(today, LocalTime.NOON);
                if (now.isAfter(todayNoon)) {
                    return false;
                }
            }

            return true;
        }

        return true;
    }

}
