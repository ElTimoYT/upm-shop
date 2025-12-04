package es.upm.iwsim22_01.models.product;

import java.time.LocalDateTime;

/**
 * Clase abstracta que representa un producto de tipo servicio.
 * Incluye información sobre el número máximo de participantes, la fecha de caducidad
 * y el número actual de personas apuntadas al servicio.
 */
public abstract class ProductService extends AbstractProduct {
    private int maxParticipant;
    private LocalDateTime expirationDate;
    private int participantsAmount;

    /**
     * Constructor de la clase ProductService.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param price Precio del producto.
     * @param maxParticipant Número máximo de participantes permitidos.
     * @param expirationDate Fecha y hora de caducidad o realización del servicio.
     */
    public ProductService(int id, String name, double price, int maxParticipant, LocalDateTime expirationDate){
        super(id, name,price);
        this.maxParticipant = maxParticipant;
        this.expirationDate = expirationDate;
        this.participantsAmount = 0;
    }

    /**
     * Obtiene la fecha y hora de caducidad del servicio.
     *
     * @return Fecha y hora de caducidad.
     */
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Obtiene el número actual de personas apuntadas al servicio.
     *
     * @return Número de personas apuntadas.
     */
    public int getParticipantsAmount() {
        return participantsAmount;
    }

    /**
     * Establece el número de personas apuntadas al servicio.
     *
     * @param participantsAmount Nuevo número de personas apuntadas.
     */
    public void setParticipantsAmount(int participantsAmount) {
        this.participantsAmount = participantsAmount;
    }

    /**
     * Verifica si la fecha de caducidad del servicio es posterior a la fecha y hora actual.
     *
     * @return true si la fecha de caducidad es válida, false en caso contrario.
     */
    public boolean checkTime() {
        LocalDateTime now = LocalDateTime.now();

        return !getExpirationDate().isBefore(now);
    }

    /**
     * Devuelve una representación en cadena del producto de servicio.
     *
     * @return Cadena que representa el producto, incluyendo su clase, identificador, nombre,
     * precio total (precio por persona multiplicado por el número de personas apuntadas),
     * número máximo de participantes y fecha de caducidad.
     */
    @Override
    public String toString() {
        return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                ",id:" + getId() +
                ",name:'" + getName() + '\'' +
                ",price:" + (getPrice() * participantsAmount) +
                ",max_participant:" + maxParticipant +
                ",expiration:" + expirationDate +
                '}';
    }

    /**
     * Devuelve una representación en cadena del producto de servicio, enfocada en la impresión de tickets.
     *
     * @return Cadena que representa el producto, incluyendo detalles sobre el evento,
     * como la fecha, el número máximo de personas permitidas y el número actual de personas apuntadas.
     */
    public String printTicketWithPeople(){
        return "Product{" +
            "class:" + this.getClass().getSimpleName() +
            " ,id:" + getId() +
            " ,name:'" + getName() + '\'' +
            " ,price:" + (getPrice() * participantsAmount) +
                " ,date of event: " + expirationDate +
            " ,max people allowed:" + maxParticipant +
                ",actual people in event; "+ participantsAmount +"}";
    }
}