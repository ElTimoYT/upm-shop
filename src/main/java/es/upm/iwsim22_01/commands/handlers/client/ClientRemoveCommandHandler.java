package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;

import java.util.NoSuchElementException;

public class ClientRemoveCommandHandler  implements CommandHandler {
    private final ClientService clientManager;

    private static final String
            ERROR_FAILED_REMOVAL = "Client found, but removal of client failed",
            OK_CLIENT_REMOVE = "client remove: ok",

            ERROR_INCORRECT_USE =  "Incorrect use: client remove <DNI>",
            ERROR_ID_NOT_FOUND = "Client ID not found";

    public ClientRemoveCommandHandler(ClientService clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            String id = tokens.next();

            if (!clientManager.existsId(id)) {
                System.out.println(ERROR_ID_NOT_FOUND);
                return;
            }

            ClientDTO client = clientManager.remove(id);
            if (client == null) {
                System.out.println(ERROR_FAILED_REMOVAL);
                return;
            }

            System.out.println(OK_CLIENT_REMOVE);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE);
        }
    }
}
