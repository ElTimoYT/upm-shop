package es.upm.iwsim22_01.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.upm.iwsim22_01.commands.commands.CommandHandler;

public class CommandDispatcher {

    private static final String ERROR_NO_COMMAND_FOUND = "No command found",
            ERROR_UNKNOWN_COMMAND = "Unknown command";


    private final Map<String, CommandHandler> COMMANDS = new TreeMap<>();

    public void addCommand(String name, CommandHandler commandHandler) {
        COMMANDS.put(name, commandHandler);
    }

    public void processCommand(String command) {
        Iterator<String> tokens = tokenizeCommand(command);

        if (!tokens.hasNext()) {
            System.out.println(ERROR_NO_COMMAND_FOUND);
            return;
        }

        String commandName = tokens.next();
        if (!COMMANDS.containsKey(command)) {
            System.out.println(ERROR_UNKNOWN_COMMAND);
            return;
        }

        COMMANDS.get(commandName).runCommand(tokens);
    }

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
