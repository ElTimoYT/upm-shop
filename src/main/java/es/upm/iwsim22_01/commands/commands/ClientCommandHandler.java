package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.commands.Converter;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;
import es.upm.iwsim22_01.models.Client;

import java.util.Iterator;
import java.util.OptionalInt;

public class ClientCommandHandler implements CommandHandler{

    private ClientManager clientManager;
    private CashierManager cashierManager;


    private final String
    REMOVE_SUBCOMMAND = "remove",
            ERROR_FAILED_REMOVAL = "Client found, but removal of client failed",
    ADD_SUBCOMMAND = "add",

    ERROR_INCORRECT_USE =  "Incorrect formatting of the command, please try again",
    ERROR_ID_NOT_FOUND = "Client ID not found in database",
    ERROR_ID_ALREADY_FOUND = "Client ID already exists within database"


    ;



    public ClientCommandHandler(ClientManager clientManager, CashierManager cashierManager) {
        this.clientManager = clientManager;
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
        }


        switch (tokens.next()) { //TODO: DECIDE ON WHICH COMMAND
            case REMOVE_SUBCOMMAND -> clientRemoveCommand(tokens);
            case ADD_SUBCOMMAND -> clientAddCommand(tokens);


            default -> System.out.println(ERROR_INCORRECT_USE);
        }


    }

    private void clientAddCommand(Iterator<String> tokens) {

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

        if (!clientManager.checkEmail(tokens.next())) {
            System.out.println(ERROR_ID_ALREADY_FOUND);
        }

        





    }

    private void clientRemoveCommand(Iterator<String> tokens) {

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



}
