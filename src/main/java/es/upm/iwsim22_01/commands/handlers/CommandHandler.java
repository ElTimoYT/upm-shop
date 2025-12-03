package es.upm.iwsim22_01.commands.handlers;

import es.upm.iwsim22_01.commands.CommandTokens;

/**
 * Interfaz para los componentes encargados de ejecutar comandos.
 * Cada implementación define cómo procesar un conjunto de tokens de comando.
 * @see CommandTokens
 */
public interface CommandHandler {

    /**
     * Ejecuta la lógica asociada al comando representado por los tokens.
     * @see CommandTokens
     *
     * @param tokens conjunto de tokens que representan el comando y sus argumentos
     */
    void runCommand(CommandTokens tokens);
}
