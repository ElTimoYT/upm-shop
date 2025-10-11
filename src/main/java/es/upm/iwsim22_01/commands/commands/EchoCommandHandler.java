package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.commands.CommandStatus;

/**
 * Manejador del comando 'echo'.
 * <p>
 * Este comando imprime en pantalla el mensaje proporcionado como parámetro.
 * </p> */
public class EchoCommandHandler implements CommandHandler {
    
    /**
     * Ejecuta el comando echo imprimiendo el mensaje proporcionado.
     * 
     * @param tokens iterador con los tokens del comando
     * @return CommandStatus indicando el éxito o fallo de la operación
     */
    @Override
    public CommandStatus runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: echo \"<message>\"");
        }

        System.out.println(tokens.next());
        return new CommandStatus(true);
    }
}
