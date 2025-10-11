package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.commands.CommandStatus;

/**
 * Interfaz que define el contrato para los manejadores de comandos.
 * <p>
 * Todos los comandos del sistema deben implementar esta interfaz
 * para poder ser procesados por el CommandDispatcher.
 * </p> */
public interface CommandHandler {

    /**
     * Ejecuta el comando con los tokens proporcionados.
     * 
     * @param tokens iterador con los tokens del comando parseado
     * @return CommandStatus con el resultado de la ejecución
     *
     * @see CommandStatus
     */
    CommandStatus runCommand(Iterator<String> tokens);
}
