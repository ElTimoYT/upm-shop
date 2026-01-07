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
    
    public ProdCommandHandler(ProductService productService) {
        productCommandDispatcher.addCommand(ADD, new ProdAddCommandHandler(productService));
        productCommandDispatcher.addCommand(LIST, new ProdListCommandHandler(productService));
        productCommandDispatcher.addCommand(REMOVE, new ProdRemoveCommandHandler(productService));
        productCommandDispatcher.addCommand(ADD_FOOD, new ProdAddFoodCommandHandler(productService));
        productCommandDispatcher.addCommand(ADD_MEETING, new ProdAddMeetingCommandHandler(productService));
        productCommandDispatcher.addCommand(UPDATE, new ProdUpdateCommandHandler(productService));
    }
    
    @Override
    public void runCommand(CommandTokens tokens) {
        productCommandDispatcher.processCommand(tokens);
    }
}
