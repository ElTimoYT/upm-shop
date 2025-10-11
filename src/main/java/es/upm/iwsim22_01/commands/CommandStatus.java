package es.upm.iwsim22_01.commands;

/**
 * Representa el resultado de la ejecución de un comando.
 * <p>
 * Esta clase encapsula el estado de éxito o fallo de un comando
 * junto con un mensaje descriptivo opcional.
 * </p>
 */
public class CommandStatus {
    private boolean status;
    private String message;

    /**
     * Constructor completo de CommandStatus.
     * 
     * @param status estado de éxito (true) o fallo (false)
     * @param message mensaje descriptivo del resultado
     */
    public CommandStatus(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor simplificado de CommandStatus sin mensaje.
     * 
     * @param status estado de éxito (true) o fallo (false)
     */
    public CommandStatus(boolean status) {
        this.status = status;
    }

    /**
     * Obtiene el estado del comando.
     * 
     * @return true si el comando se ejecutó correctamente, false en caso contrario
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Obtiene el mensaje del resultado del comando.
     * 
     * @return mensaje descriptivo del resultado, puede ser null
     */
    public String getMessage() {
        return message;
    }
}
