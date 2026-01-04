package es.upm.iwsim22_01.service.dto.user;

/**
 * Clase abstracta que representa a un usuario genérico en el sistema.
 * Define los atributos y métodos básicos comunes a todos los tipos de usuarios,
 * como nombre, correo electrónico e identificador único.
 */
public abstract class AbstractUserDTO {
    private String name;
    private String email;
    private String dni;

    /**
     * Constructor de la clase AbstractUser.
     *
     * @param name Nombre del usuario.
     * @param email Correo electrónico del usuario.
     * @param dni Identificador único del usuario.
     */
    public AbstractUserDTO(String name, String email, String dni) {
        this.name= name;
        this.dni = dni;
        this.email= email;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getName(){
        return name;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return El identificador del usuario.
     */
    public String getDNI(){
        return dni;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario.
     */
    public String getEmail(){
        return email;
    }
}
