package es.upm.iwsim22_01.service.dto.user;

import java.util.Objects;

/**
 * Clase abstracta que representa un usuario genérico del sistema con atributos comunes.
 */
public abstract class AbstractUserDTO {
    private String name;
    private String email;
    private String id;

    /**
     * Crea un usuario con nombre, correo electrónico e identificador.
     *
     * @param name nombre del usuario
     * @param email correo electrónico del usuario
     * @param id identificador único del usuario
     */
    public AbstractUserDTO(String name, String email, String id) {
        this.name= name;
        this.id = id;
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

    /**
     * Compara dos usuarios basándose en su nombre.
     *
     * @param object objeto a comparar
     * @return true si ambos usuarios tienen el mismo nombre
     */
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        AbstractUserDTO abstractUserDTO = (AbstractUserDTO) object;

        return Objects.equals(getId(), abstractUserDTO.getId());
    }
}
