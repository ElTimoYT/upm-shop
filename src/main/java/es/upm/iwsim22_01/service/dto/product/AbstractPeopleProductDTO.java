package es.upm.iwsim22_01.service.dto.product;

import es.upm.iwsim22_01.service.dto.Validable;

import java.time.LocalDateTime;

/**
 * Clase abstracta que representa un producto de tipo servicio.
 * Incluye información sobre el número máximo de participantes, la fecha de caducidad
 * y el número actual de personas apuntadas al servicio.
 */
public abstract class AbstractPeopleProductDTO extends AbstractProductDTO implements Validable {
    private int maxParticipant;
    private LocalDateTime expirationDate;
    private int participantsAmount;

    /**
     * Crea un producto con información completa incluyendo cantidad disponible.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param price precio unitario del producto
     * @param amount cantidad disponible
     * @param maxParticipant número máximo de participantes permitidos
     * @param expirationDate fecha y hora de expiración del servicio
     * @param participantsAmount número actual de participantes
     */
    public AbstractPeopleProductDTO(String id, String name, double price, int amount, int maxParticipant, LocalDateTime expirationDate, int participantsAmount) {
        super(id, name, price, amount);
        this.maxParticipant = maxParticipant;
        this.expirationDate = expirationDate;
        this.participantsAmount = participantsAmount;
    }

    /**
     * Crea un producto sin control de cantidad disponible.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param price precio unitario del producto
     * @param maxParticipant número máximo de participantes permitidos
     * @param expirationDate fecha y hora de expiración del servicio
     * @param participantsAmount número actual de participantes
     */
    public AbstractPeopleProductDTO(String id, String name, double price, int maxParticipant, LocalDateTime expirationDate, int participantsAmount){
        super(id, name,price);
        this.maxParticipant = maxParticipant;
        this.expirationDate = expirationDate;
        this.participantsAmount = participantsAmount;
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
     * Devuelve el número máximo de participantes permitidos.
     *
     * @return número máximo de participantes
     */
    public int getMaxParticipant() {
        return maxParticipant;
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
    @Override
    public boolean isValid() {
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
                ",price:" + (getPrice() * getAmount()) +
                ",participans:" + getAmount() +
                ",max_participant:" + getMaxParticipant() +
                ",expiration:" + getExpirationDate() +
                '}';
    }
}