package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ClientManager;

import javax.print.DocFlavor;
import java.util.Iterator;

public class ClientCommandHandler implements CommandHandler{

    private ClientManager clientManager;
    private CashierManager cashierManager;

    private final String
    REMOVE_SUBCOMMAND = "remove",
            ERROR_FAILED_REMOVAL = "Client found, but removal of client failed",


    ERROR_INCORRECT_USE =  "Incorrect formatting of the command, please try again",
    ERROR_ID_NOT_FOUND = "Client ID not found in database"



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
            case REMOVE_SUBCOMMAND -> removeClientCommand(tokens);

            default -> System.out.println(ERROR_INCORRECT_USE);
        }


    }

    private void clientAddCommand(Iterator<String> tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }



    }

    private void removeClientCommand(Iterator<String> tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE);
            return;
        }

        String id = tokens.next();

        if (!clientManager.existId(id)) {
            System.out.println(ERROR_ID_NOT_FOUND);
            return;
        }

        if (!clientManager.remove(id)) {
            System.out.println(ERROR_FAILED_REMOVAL);
        }

    }



}
