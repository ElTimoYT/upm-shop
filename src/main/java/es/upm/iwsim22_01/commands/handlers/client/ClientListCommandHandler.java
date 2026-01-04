package es.upm.iwsim22_01.commands.handlers.client;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.dto.user.AbstractUserDTO;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.dto.user.ClientDTO;

import java.util.Comparator;
import java.util.List;

public class ClientListCommandHandler  implements CommandHandler {
    private final ClientService clientService;

    private static final String CLIENTS = "Clients: ", OK_CLIENT_LIST = "client list: ok";

    public ClientListCommandHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        List<ClientDTO> sortedItems = clientService.getAll();

        sortedItems.sort(Comparator.comparing(AbstractUserDTO::getName, String.CASE_INSENSITIVE_ORDER));

        System.out.println(CLIENTS);
        for (ClientDTO client : sortedItems) {
            System.out.println("\t" + client);
        }
        System.out.println(OK_CLIENT_LIST);
    }
}
