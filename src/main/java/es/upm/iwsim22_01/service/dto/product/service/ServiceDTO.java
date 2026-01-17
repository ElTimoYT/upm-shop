package es.upm.iwsim22_01.service.dto.product.service;

import es.upm.iwsim22_01.service.dto.Validable;
import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.category.Categorizable;
import es.upm.iwsim22_01.service.dto.product.category.ServiceCategoryDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ServiceDTO extends AbstractProductDTO implements Validable, Categorizable {
    private final ServiceCategoryDTO category;
    private final LocalDate expirationDate;

    private static final DateTimeFormatter EXP_FMT =
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    public ServiceDTO(String id, LocalDate expirationDate, ServiceCategoryDTO category) {
        super(id, null, 0);
        this.category = category;
        this.expirationDate = expirationDate;
    }

    @Override
    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean isValid() {
        return !expirationDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        ZoneId zone = ZoneId.systemDefault(); // CET en tu output
        ZonedDateTime zdt = expirationDate.atStartOfDay(zone);

        return "{class:" + getClass().getSimpleName() +
                ", id:" + id +
                ", category:" + category +
                ", expiration:" + EXP_FMT.format(zdt) +
                "}";
    }
}

