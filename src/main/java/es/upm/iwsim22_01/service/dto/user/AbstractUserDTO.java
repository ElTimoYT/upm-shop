package es.upm.iwsim22_01.service.dto.user;

/**
 * Clase abstracta que representa a un usuario genérico en el sistema.
 * Define los atributos y métodos básicos comunes a todos los tipos de usuarios,
 * como nombre, correo electrónico e identificador único.
 */
public abstract class AbstractUserDTO {
    private String name;
    private String email;
    private String id;

    /**
     * Constructor de la clase AbstractUser.
     *
     * @param name Nombre del usuario.
     * @param email Correo electrónico del usuario.
     * @param id Identificador único del usuario.
     */
    public AbstractUserDTO(String name, String email, String id) {
        this.name= name;
        this.id=id;
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
    public String getId(){
        return id;
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
