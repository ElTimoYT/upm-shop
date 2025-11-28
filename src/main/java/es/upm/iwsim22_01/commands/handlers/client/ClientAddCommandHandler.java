package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.models.Client;

import java.util.NoSuchElementException;

public class ClientAddCommandHandler  implements CommandHandler {
    private final ClientManager clientManager;
    private final CashierManager cashierManager;

    private static final String
            OK_CLIENT_ADD = "client add: ok",

            ERROR_INCORRECT_USE = "Incorrect use: client add \"<nombre>\" <DNI> <email> <cashId>",
            ERROR_ID_ALREADY_FOUND = "Client ID already exists.",
            ERROR_NONEXISTANT_CASHIER = "Cashier does not exist.",
            ERROR_EMAIL_INVALID = "Email does not adhere to required rules.",
            ERROR_DNI_INVALID = "DNI does not adhere to required rules.";

    public ClientAddCommandHandler(ClientManager clientManager, CashierManager cashierManager) {
        this.clientManager = clientManager;
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            String clientTentativeName = tokens.next();

            String clientTentativeId = tokens.next();

            if (clientManager.existId(clientTentativeId)) {
                System.out.println(ERROR_ID_ALREADY_FOUND);
                return;
            }
            if (!clientManager.checkDNI(clientTentativeId)) {
                System.out.println(ERROR_DNI_INVALID);
                return;
            }

            String clientTentativeEmail = tokens.next();
            if (!clientManager.checkEmail(clientTentativeEmail)) {
                System.out.println(ERROR_EMAIL_INVALID);
            }

            String cashierTentativeId = tokens.next();
            if (!cashierManager.existId(cashierTentativeId)) {
                System.out.println(ERROR_NONEXISTANT_CASHIER);
                return;
            }

            Client client = clientManager.addClient(clientTentativeName, clientTentativeId, clientTentativeEmail, cashierTentativeId);
            System.out.println(client);
            System.out.println(OK_CLIENT_ADD);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE);
        }
    }
}