package es.upm.iwsim22_01.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.upm.iwsim22_01.commands.handlers.CommandHandler;

public class CommandDispatcher {

    private static final String ERROR_NO_COMMAND_FOUND = "No command found",
            ERROR_UNKNOWN_COMMAND = "Unknown command",
            ERROR_UNEXPECTED_EXCEPTION = "Unexpected exception.";


    private final Map<String, CommandHandler> COMMANDS = new TreeMap<>();
    private final String noCommandFoundMessage, unknownCommandMessage;

    public CommandDispatcher() {
        this(ERROR_NO_COMMAND_FOUND, ERROR_UNKNOWN_COMMAND);
    }

    public CommandDispatcher(String noCommandFoundMessage, String unknownCommandMessage) {
        this.noCommandFoundMessage = noCommandFoundMessage;
        this.unknownCommandMessage = unknownCommandMessage;
    }

    public void addCommand(String name, CommandHandler commandHandler) {
        COMMANDS.put(name, commandHandler);
    }

    public void processCommand(String command) {
        processCommand(tokenizeCommand(command));
    }

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
