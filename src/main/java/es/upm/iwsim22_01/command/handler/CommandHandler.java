package es.upm.iwsim22_01.command.handler;

import es.upm.iwsim22_01.App;
import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.models.Category;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler central que gesiona los comandos de la aplicación.
 * <br>
 * Responsabilidades:
 * <ul>
 *     <li>Tokenizar comando</li>
 *     <li>Disparar las funciones correspondientes a cada comando</li>
 *     <li>Devolver un CommandStatus con el resultado de la ejecución del comando</li>
 * </ul>
 * <br>
 * Los comandos Prod y Ticket tienen sus propios handlers.
 * @see ProdCommandHandler
 * @see TicketCommandHandler
 * @see CommandStatus
 */
public class CommandHandler {
    private static final Map<String, Function<Iterator<String>, CommandStatus>> COMMANDS = Map.of(
            "exit", CommandHandler::exitCommand,
            "echo", CommandHandler::echoCommand,
            "help", CommandHandler::helpCommand,
            "prod", CommandHandler::prodCommand,
            "ticket", CommandHandler::ticketCommand
    );

    public static CommandStatus runCommand(String command) {
        Iterator<String> tokens = tokenizeCommand(command);

        if (!tokens.hasNext()) {
            return new CommandStatus(false, "No command detected");
        }

        return COMMANDS.getOrDefault(
                tokens.next(),
                stringIterator -> new CommandStatus(false, "Unknown command")
        ).apply(tokens);
    }

    /**
     * Separamos el string en tokens por espacios, los ponemos en minúscula (evitar case-sensitive) y los metemos en un iterator. Preferible a un array, ya que no hay que recordar los índices y evitamos números mágicos. Los segmentos entrecomillados conservarán los espacios y las mayúsculas.
     * @param command String que queremos tokenizar.
     * @return Iterador con todos los tokens.
     */
    private static Iterator<String> tokenizeCommand(String command) {
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

    private static CommandStatus exitCommand(Iterator<String> tokens) {
        App.exitMenu();

        return new CommandStatus(true);
    }

    private static CommandStatus echoCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return new CommandStatus(false, "Incorrect use: echo \"<message>\"");
        }

        System.out.println(tokens.next());
        return new CommandStatus(true);
    }

    private static CommandStatus helpCommand(Iterator<String> tokens) {
        System.out.print("""
                Commands:
                 prod add <id> "<name>" <category> <price>
                 prod list
                 prod update <id> NAME|CATEGORY|PRICE <value>
                 prod remove <id>
                 ticket new
                 ticket add <prodId> <quantity>
                 ticket remove <prodId>
                 ticket print
                 echo "<texto>"
                 help
                 exit
                
                Categories:\s""");



        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.print(categories[i]);
            if (i < categories.length - 1) System.out.print(", ");

        }

        System.out.print("\nDiscounts if there are ≥2 units in the category: ");

        for (int i = 0; i < categories.length; i++) {
            System.out.print(categories[i] + " " + (int)(categories[i].getDiscount() * 100) + "%");
            if (i < categories.length - 1) System.out.print(", ");
            else System.out.print(".");
        }

        System.out.println();

        return new CommandStatus(true);
    }

    private static CommandStatus prodCommand(Iterator<String> tokens) {
        return ProdCommandHandler.runCommand(tokens);
    }

    private static CommandStatus ticketCommand(Iterator<String> tokens) {
        return TicketCommandHandler.runCommand(tokens);
    }
}
