package es.upm.iwsim22_01.service.dto.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class ProductService extends AbstractProductDTO {

    private final CategoryDTO category;
    private final LocalDateTime expirationDate;
    private static final DateTimeFormatter EXP_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ProductService(int id, LocalDateTime expirationDate, CategoryDTO category) {
        // Servicios NO tienen nombre ni precio
        super(id, "", 0.0, 0);
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean isValid() {
        return !expirationDate.isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "{class:" + getClass().getSimpleName() +
                ", id:" + id +
                ", category:" + category +
                ", expiration:" + expirationDate.format(EXP_FMT) +
                "}";
    }
}

