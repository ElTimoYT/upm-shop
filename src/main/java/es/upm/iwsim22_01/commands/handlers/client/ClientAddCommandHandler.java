package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;

import java.util.NoSuchElementException;

public class ClientAddCommandHandler  implements CommandHandler {
    private final ClientService clientService;
    private final CashierService cashierService;

    private static final String
            OK_CLIENT_ADD = "client add: ok",

            ERROR_INCORRECT_USE = "Incorrect use: client add \"<nombre>\" <DNI> <email> <cashId>",
            ERROR_ID_ALREADY_FOUND = "Client ID already exists.",
            ERROR_NONEXISTANT_CASHIER = "Cashier does not exist.",
            ERROR_EMAIL_INVALID = "Email does not adhere to required rules.",
            ERROR_DNI_INVALID = "DNI does not adhere to required rules.";

    public ClientAddCommandHandler(ClientService clientService, CashierService cashierService) {
        this.clientService = clientService;
        this.cashierService = cashierService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            String clientTentativeName = tokens.next();

            String clientTentativeId = tokens.next();

            if (clientService.existsId(clientTentativeId)) {
                System.out.println(ERROR_ID_ALREADY_FOUND);
                return;
            }
            if (!clientService.checkDNI(clientTentativeId)) {
                System.out.println(ERROR_DNI_INVALID);
                return;
            }

            String clientTentativeEmail = tokens.next();
            if (!clientService.checkEmail(clientTentativeEmail)) {
                System.out.println(ERROR_EMAIL_INVALID);
            }

            String cashierTentativeId = tokens.next();
            if (!cashierService.existsId(cashierTentativeId)) {
                System.out.println(ERROR_NONEXISTANT_CASHIER);
                return;
            }

            ClientDTO client = clientService.addClient(clientTentativeName, clientTentativeId, clientTentativeEmail, cashierTentativeId);
            System.out.println(client);
            System.out.println(OK_CLIENT_ADD);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE);
        }
    }
}