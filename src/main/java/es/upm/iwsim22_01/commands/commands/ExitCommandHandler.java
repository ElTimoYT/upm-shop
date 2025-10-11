package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.commands.CommandStatus;

/**
 * Manejador del comando 'exit'.
 * <p>
 * Este comando termina la ejecución de la aplicación cerrando el bucle principal.
 * </p> */
public class ExitCommandHandler implements CommandHandler {
    
    /**
     * Ejecuta el comando exit cerrando la aplicación.
     * 
     * @param tokens iterador con los tokens del comando (no utilizado)
     * @return CommandStatus indicando éxito de la operación
     */
    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        App.exitMenu();

        return new CommandStatus(true);
    }
}
