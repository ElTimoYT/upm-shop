package es.upm.iwsim22_01.service.dto.product;

import java.time.LocalDateTime;

/**
 * Clase que representa un producto de catering, especialización de {@link ProductServiceDTO}.
 * Implementa la validación específica para productos de catering, asegurando que la fecha de caducidad
 * sea al menos 3 días posterior a la fecha actual.
 */
public class CateringDTO extends ProductServiceDTO {
    /**
     * Constructor de la clase Catering.
     *
     * @param id               Identificador único del producto.
     * @param name             Nombre del producto.
     * @param pricePerPerson   Precio por persona.
     * @param maxPers          Número máximo de personas.
     * @param expirationDate   Fecha de caducidad del producto.
     */
    public CateringDTO(int id, String name, double pricePerPerson, int maxPers, LocalDateTime expirationDate){
        super(id,name, pricePerPerson, maxPers, expirationDate);
    }

    /**
     * Valida que la fecha de caducidad del producto sea al menos 3 días posterior a la fecha actual.
     * Además, verifica la validez de la fecha mediante la implementación de la clase padre.
     *
     * @return {@code true} si la fecha de caducidad es válida, {@code false} en caso contrario.
     */
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
