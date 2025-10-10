package es.upm.iwsim22_01.commands;

import es.upm.iwsim22_01.commands.commands.CommandHandler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dispatcher central que gestiona los comandos de la aplicación.
 * <br>
 * Responsabilidades:
 * <ul>
 *     <li>Tokenizar comando</li>
 *     <li>Disparar las funciones correspondientes a cada comando</li>
 *     <li>Devolver un CommandStatus con el resultado de la ejecución del comando</li>
 * </ul>
 * <br>
 * @see CommandStatus
 * @see CommandHandler
 */
public class CommandDispatcher {
    private final Map<String, CommandHandler> COMMANDS = new TreeMap<>();

    public void addCommand(String name, CommandHandler commandHandler) {
        COMMANDS.put(name, commandHandler);
    }

    public CommandStatus processCommand(String command) {
        Iterator<String> tokens = tokenizeCommand(command);

        if (!tokens.hasNext()) {
            return new CommandStatus(false, "No command detected");
        }

        return COMMANDS.getOrDefault(
                tokens.next(),
                stringIterator -> new CommandStatus(false, "Unknown command")
        ).runCommand(tokens);
    }

    /**
     * Separamos el string en tokens por espacios, los ponemos en minúscula (evitar case-sensitive) y los metemos en un iterator. Preferible a un array, ya que no hay que recordar los índices y evitamos números mágicos. Los segmentos entrecomillados conservarán los espacios y las mayúsculas.
     * @param command String que queremos tokenizar.
     * @return Iterador con todos los tokens.
     */
    private Iterator<String> tokenizeCommand(String command) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"|\\S+"); //Dividimos por espacios o comillas: "([^"]+)"|\S+
        Matcher matcher = pattern.matcher(command);

        List<String> tokens = new ArrayList<>();

        while (matcher.find()) {
            String token = matcher.group();
            if (token.startsWith("\"") && token.endsWith("\"")) { //Si está entre comillas quitar las comillas.
                tokens.add(token.substring(1, token.length() -1));
            } else { //Poner en minusculas si no
                tokens.add(token.toLowerCase());
            }
        }

        return tokens.iterator();
    }
}
