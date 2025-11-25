package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.models.Client;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ClientCommandHandler implements CommandHandler {
    private ClientManager clientManager;
    private CashierManager cashierManager;

    private static final String
    REMOVE_SUBCOMMAND = "remove",
            ERROR_FAILED_REMOVAL = "Client found, but removal of client failed",
    ADD_SUBCOMMAND = "add",
    LIST_SUBCOMMAND = "list",

    ERROR_INCORRECT_USE =  "Incorrect formatting of the command, please try again",
    ERROR_ID_NOT_FOUND = "Client ID not found in database",
    ERROR_ID_ALREADY_FOUND = "Client ID already exists within database",
    ERROR_NONEXISTANT_CASHIER = "Cashier to add client to database does not exist",
    ERROR_EMAIL_INVALID = "Email does not adhere to required rules";

    public ClientCommandHandler(ClientManager clientManager, CashierManager cashierManager) {
        this.clientManager = clientManager;
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
        }

        switch (tokens.next()) {
            case REMOVE_SUBCOMMAND -> clientRemoveCommand(tokens);
            case ADD_SUBCOMMAND -> clientAddCommand(tokens);
            case LIST_SUBCOMMAND -> clientListCommand();
            default -> System.out.println(ERROR_INCORRECT_USE);
        }
    }

    private void clientAddCommand(CommandTokens tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        String clientTentativeName = tokens.next();

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        String clientTentativeId = tokens.next();

        if (clientManager.existId(clientTentativeId)) {
            System.out.println(ERROR_ID_ALREADY_FOUND);
            return;
        }

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        String clientTentativeEmail =  tokens.next();
        if (!clientManager.checkEmail(clientTentativeEmail)) {
            System.out.println(ERROR_EMAIL_INVALID);
        }

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        String cashierTentativeId = tokens.next();
        if (!cashierManager.existId(cashierTentativeId)) {
            System.out.println(ERROR_NONEXISTANT_CASHIER);
        } else {
            clientManager.addClient(clientTentativeName, clientTentativeId, clientTentativeEmail, cashierTentativeId);
        }
    }

    private void clientRemoveCommand(CommandTokens tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        String id = tokens.next();

        if (!clientManager.existId(id)) {
            System.out.println(ERROR_ID_NOT_FOUND);
            return;
        }

        Client client = clientManager.remove(id);
        if (client == null) {
            System.out.println(ERROR_FAILED_REMOVAL);
            return;
        } else {
            System.out.println(client + "removed");
        }

    }

    private void clientListCommand() {
        List<Client> sortedItems = clientManager.getAll();

        sortedItems.sort(Comparator.comparing(
                        client -> client.getName(),
                    String.CASE_INSENSITIVE_ORDER));

        sortedItems.forEach(System.out::println);
    }


}
