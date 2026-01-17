package es.upm.iwsim22_01.service.dto.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Clase que representa un producto de tipo reunión, especialización de {@link AbstractTypeDTO}.
 * Implementa reglas específicas para validar la fecha y hora de las reuniones,
 * asegurando que se cumplan los plazos mínimos de reserva y cancelación.
 */
public class MeetingDTO extends AbstractTypeDTO {
    public MeetingDTO(int id, String name, double price, int amount, int maxPers, LocalDateTime expirationDate, int participantsAmount) {
        super(id, name, price, amount, maxPers, expirationDate, participantsAmount);
    }

    /**
     * Constructor de la clase Meeting.
     *
     * @param id               Identificador único del producto.
     * @param name             Nombre del producto.
     * @param price   Precio por persona.
     * @param maxPers          Número máximo de participantes.
     * @param expirationDate   Fecha y hora de caducidad/reserva del servicio.
     */
    public MeetingDTO(int id, String name, double price, int maxPers, LocalDateTime expirationDate, int participantsAmount) {
        super(id, name, price, maxPers, expirationDate, participantsAmount);
    }

    /**
     * Valida que la fecha y hora de la reunión cumplan las reglas de negocio:
     *
     * La fecha de caducidad debe ser posterior a 12 horas desde el momento actual.
     * Si la reunión es para el día siguiente, la reserva debe realizarse antes del mediodía del día actual.
     *
     * Además, verifica la validez de la fecha mediante la implementación de la clase padre.
     *
     * @return true si la fecha y hora son válidas según las reglas de negocio, false en caso contrario.
     */
    @Override
    public boolean isValid() {
        if (!super.isValid()) {
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
