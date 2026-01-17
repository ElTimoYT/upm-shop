package es.upm.iwsim22_01.service.dto.product;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProductService extends AbstractProductDTO {

    private final CategoryDTO category;
    private final LocalDate expirationDate;

    private static final DateTimeFormatter EXP_FMT =
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    public ProductService(int id, LocalDate expirationDate, CategoryDTO category) {
        // Servicios NO tienen nombre ni precio
        super(id, "", 0.0, 0);
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public CategoryDTO getCategory() {
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

