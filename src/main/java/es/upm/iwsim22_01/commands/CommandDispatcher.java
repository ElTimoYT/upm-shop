package es.upm.iwsim22_01.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.upm.iwsim22_01.commands.handlers.CommandHandler;

/**
 * Clase encargada de gestionar y despachar comandos a sus respectivos manejadores.
 * Permite registrar comandos y procesar una entrada en forma de cadena,
 * dividiéndola en tokens y ejecutando el comando correspondiente.
 */
public class CommandDispatcher {

    private static final String ERROR_NO_COMMAND_FOUND = "No command found",
            ERROR_UNKNOWN_COMMAND = "Unknown command",
            ERROR_UNEXPECTED_EXCEPTION = "Unexpected error.";


    private final Map<String, CommandHandler> COMMANDS = new TreeMap<>();
    private final String noCommandFoundMessage, unknownCommandMessage;

    /**
     * Crea un dispatcher con los mensajes de error por defecto.
     */
    public CommandDispatcher() {
        this(ERROR_NO_COMMAND_FOUND, ERROR_UNKNOWN_COMMAND);
    }

    /**
     * Crea un dispatcher personalizando los mensajes mostrados cuando no se encuentra
     * un comando o cuando el comando no está registrado.
     *
     * @param noCommandFoundMessage mensaje mostrado cuando no se proporciona un comando
     * @param unknownCommandMessage mensaje mostrado cuando el comando no está registrado
     */
    public CommandDispatcher(String noCommandFoundMessage, String unknownCommandMessage) {
        this.noCommandFoundMessage = noCommandFoundMessage;
        this.unknownCommandMessage = unknownCommandMessage;
    }

    /**
     * Registra un nuevo comando asociándolo a un manejador.
     *
     * @param name nombre del comando que se utilizará para invocarlo
     * @param commandHandler manejador que ejecutará la lógica del comando
     */
    public void addCommand(String name, CommandHandler commandHandler) {
        COMMANDS.put(name, commandHandler);
    }

    /**
     * Procesa una cadena de texto interpretándola como un comando.
     * La cadena se tokeniza y se reenvía a la otra variante del método.
     *
     * @param command cadena con el comando y sus argumentos
     */
    public void processCommand(String command) {
        processCommand(tokenizeCommand(command));
    }

    /**
     * Procesa un conjunto de tokens que representan un comando.
     * Ejecuta el manejador asociado al comando si existe.
     *
     * @param tokens tokens que representan el comando y sus argumentos
     */
    public void processCommand(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(noCommandFoundMessage);
            return;
        }

        String commandName = tokens.next();
        if (!COMMANDS.containsKey(commandName)) {
            System.out.println(unknownCommandMessage);
            return;
        }

        try {
            COMMANDS.get(commandName).runCommand(tokens);
        } catch (Exception exception) {
            System.out.println(ERROR_UNEXPECTED_EXCEPTION);
        }
    }

    /**
     * Convierte una cadena de texto en tokens, respetando textos entre comillas.
     * Los fragmentos entre comillas se consideran un único token.
     * @see CommandTokens
     *
     * @param command cadena del comando completa
     * @return objeto CommandTokens con los tokens generados
     */
    private CommandTokens tokenizeCommand(String command) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"|\\S+"); //Dividimos por espacios o comillas: "([^"]+)"|\S+
        Matcher matcher = pattern.matcher(command);

        List<String> tokens = new ArrayList<>();

        while (matcher.find()) {
            String token = matcher.group();
            if (token.startsWith("\"") && token.endsWith("\"")) { //Si está entre comillas quitar las comillas.
                tokens.add(token.substring(1, token.length() -1));
            } else {
                tokens.add(token);
            }
        }

        return new CommandTokens(tokens);
    }
}
