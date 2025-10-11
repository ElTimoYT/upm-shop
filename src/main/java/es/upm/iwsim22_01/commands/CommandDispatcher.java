package es.upm.iwsim22_01.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.upm.iwsim22_01.commands.commands.CommandHandler;

/**
 * Dispatcher central que gestiona los comandos de la aplicación.
 * <p>
 * Responsabilidades:
 * <ul>
 *     <li>Tokenizar comando</li>
 *     <li>Disparar las funciones correspondientes a cada comando</li>
 *     <li>Devolver un CommandStatus con el resultado de la ejecución del comando</li>
 * </ul>
 * </p>
 * @see CommandStatus
 * @see CommandHandler */
public class CommandDispatcher {
    private final Map<String, CommandHandler> COMMANDS = new TreeMap<>();

    /**
     * Registra un nuevo comando en el dispatcher.
     * 
     * @param name nombre del comando a registrar
     * @param commandHandler manejador que procesará el comando
     */
    public void addCommand(String name, CommandHandler commandHandler) {
        COMMANDS.put(name, commandHandler);
    }

    /**
     * Procesa un comando completo ejecutando la acción correspondiente.
     * <p>
     * El método tokeniza el comando, extrae el nombre del comando y delega
     * la ejecución al handler correspondiente. Si el comando no existe,
     * devuelve un error.
     * </p>
     * 
     * @param command cadena de texto con el comando completo a procesar
     * @return CommandStatus con el resultado de la ejecución del comando
     */
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
     * Tokeniza un comando separándolo en partes individuales.
     * <p>
     * Este método utiliza expresiones regulares para dividir el comando en tokens.
     * Maneja texto entre comillas preservando espacios y mayúsculas, mientras que
     * el resto de tokens se convierten a minúsculas para evitar problemas de
     * case-sensitivity.
     * </p>
     * <p>
     * Ejemplos de tokenización:
     * <ul>
     *     <li>"prod add 1 \"Laptop Gaming\" ELECTRONICS 999.99" → ["prod", "add", "1", "Laptop Gaming", "electronics", "999.99"]</li>
     *     <li>"help" → ["help"]</li>
     *     <li>"ticket add 5 2" → ["ticket", "add", "5", "2"]</li>
     * </ul>
     * </p>
     * 
     * @param command cadena de texto que representa el comando a tokenizar
     * @return Iterator con todos los tokens del comando
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
