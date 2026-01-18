package es.upm.iwsim22_01.commands.handlers.prod;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.service.service.ProductService;

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
    
    public ProdCommandHandler(ProductService productInventory) {
        productCommandDispatcher.addCommand(ADD, new ProdAddCommandHandler(productInventory));
        productCommandDispatcher.addCommand(LIST, new ProdListCommandHandler(productInventory));
        productCommandDispatcher.addCommand(REMOVE, new ProdRemoveCommandHandler(productInventory));
        productCommandDispatcher.addCommand(ADD_FOOD, new ProdAddFoodCommandHandler(productInventory));
        productCommandDispatcher.addCommand(ADD_MEETING, new ProdAddMeetingCommandHandler(productInventory));
        productCommandDispatcher.addCommand(UPDATE, new ProdUpdateCommandHandler(productInventory));
    }
    
    @Override
    public void runCommand(CommandTokens tokens) {
        productCommandDispatcher.processCommand(tokens);
    }
}
