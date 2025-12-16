package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;

import java.util.Comparator;
import java.util.List;

public class ClientListCommandHandler  implements CommandHandler {
    private final ClientService clientManager;

    private static final String CLIENTS = "Clients: ", OK_CLIENT_LIST = "client list: ok";

    public ClientListCommandHandler(ClientService clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        List<ClientDTO> sortedItems = clientManager.getAll();

        sortedItems.sort(Comparator.comparing(
                client -> client.getName(),
                String.CASE_INSENSITIVE_ORDER));

        System.out.println(CLIENTS);
        for (ClientDTO client : sortedItems) {
            System.out.println("\t" + client);
        }
        System.out.println(OK_CLIENT_LIST);
    }
}
