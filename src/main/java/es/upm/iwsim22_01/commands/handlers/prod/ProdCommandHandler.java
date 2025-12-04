package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;

public class ProdCommandHandler implements CommandHandler {
    private static final String
            ADD = "add",
        LIST = "list",
        REMOVE = "remove",
        UPDATE = "update",
        ADD_FOOD = "addFood",
        ADD_MEETING = "addMeeting",

        ERROR_INCORRECT_USE = "Incorrect use: prod add|list|update|remove|addFood|addMeeting";

    private final CommandDispatcher productCommandDispatcher = new  CommandDispatcher(ERROR_INCORRECT_USE, ERROR_INCORRECT_USE);
    
    public ProdCommandHandler(ProductManager productManager) {
        productCommandDispatcher.addCommand(ADD, new ProdAddCommandHandler(productManager));
        productCommandDispatcher.addCommand(LIST, new ProdListCommandHandler(productManager));
        productCommandDispatcher.addCommand(REMOVE, new ProdRemoveCommandHandler(productManager));
        productCommandDispatcher.addCommand(ADD_FOOD, new ProdAddFoodCommandHandler(productManager));
        productCommandDispatcher.addCommand(ADD_MEETING, new ProdAddMeetingCommandHandler(productManager));
        productCommandDispatcher.addCommand(UPDATE, new ProdUpdateCommandHandler(productManager));
    }
    
    @Override
    public void runCommand(CommandTokens tokens) {
        productCommandDispatcher.processCommand(tokens);
    }
}
