package es.upm.iwsim22_01.Validators;

import es.upm.iwsim22_01.models.ProductService;
import es.upm.iwsim22_01.models.ServiceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ServiceProductTimeValidator {
    private final static String DATE_PASSED ="Date of the product already passed",
    DATE_FOOD ="Date of the Food product has to be at least 3 days in advance",
    DATE_MEETING="Date of the Meeting Prodyct has to be at least 12 hours in advance";

    private ServiceProductTimeValidator() {}

    public static boolean isValid(ProductService ps) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDateTime serviceDateTime = ps.getExpirationDate();

        if (serviceDateTime.isBefore(now)) {
            System.out.println(DATE_PASSED);
            return false;
        }

        if (ps.getServiceType() == ServiceType.FOOD) {
            LocalDateTime minFoodDateTime = now.plusDays(3);
            if(!serviceDateTime.isBefore(minFoodDateTime)) {
                System.out.println(DATE_FOOD);
                return false;
            }
            return true;
        }

        if (ps.getServiceType() == ServiceType.MEETING) {
            LocalDateTime minMeetingDateTime = now.plusHours(12);
            if (serviceDateTime.isBefore(minMeetingDateTime)) {
                System.out.println(DATE_MEETING);
                return false;
            }
            LocalDate serviceDate = serviceDateTime.toLocalDate();
            if (serviceDate.isEqual(today.plusDays(1))) {
                LocalDateTime todayNoon = LocalDateTime.of(today, LocalTime.NOON);
                if (now.isAfter(todayNoon)) {
                    System.out.println(DATE_MEETING);
                    return false;
                }
            }

            return true;
        }

        return true;
    }

}
