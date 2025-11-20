package es.upm.iwsim22_01.commands.handlers.prod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.models.Category;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.UnitProduct;

public class ProdCommandHandler implements CommandHandler {
    private static final String
            ADD = "add",
        LIST = "list",
        REMOVE = "remove",
        UPDATE = "update",
        ADD_FOOD = "addfood",
        ADD_MEETING = "addmeeting",

        ERROR_INCORRECT_USE = "Incorrect use: prod add|list|update|remove|addFood|addMeeting";

    private final CommandDispatcher productCommandDispatcher = new  CommandDispatcher(ERROR_INCORRECT_USE, ERROR_INCORRECT_USE);
    
    public ProdCommandHandler(ProductManager productManager) {
        productCommandDispatcher.addCommand(ADD, new ProdAddCommandHandler(productManager));
        productCommandDispatcher.addCommand(LIST, new ProdListCommandHandler(productManager));
        productCommandDispatcher.addCommand(REMOVE, new ProdRemoveCommandHandler(productManager));
        productCommandDispatcher.addCommand(ADD_FOOD, new ProdAddFoodCommandHandler(productManager));
        productCommandDispatcher.addCommand(ADD_MEETING, new ProdAddMeetingCommandHandler(productManager));
        productCommandDispatcher.addCommand(UPDATE, new ProdUpdateCommandHnadler(productManager));
    }
    
    @Override
    public void runCommand(CommandTokens tokens) {
        productCommandDispatcher.processCommand(tokens);
    }
/**


    private void addFoodCommand(CommandTokens tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty() || productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if(!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        String name = tokens.next();

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        OptionalDouble optionalPrice = Converter.stringToDouble(tokens.next());
        if (optionalPrice.isEmpty() || !productManager.isPriceValid(optionalPrice.getAsDouble())) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        Optional<java.time.LocalDate> expiration = Converter.stringToLocal(tokens.next());
        if (expiration.isEmpty()) {
            System.out.println(ERROR_INVALID_DATE);
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate minAllowedDate = today.plusDays(3);
        LocalDate eventDate = expiration.get();

        if (eventDate.isBefore(minAllowedDate)) {
            System.out.println("Food must be scheduled at least 3 days in advance");
            return;
        }

        if  (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDFOOD);
            return;
        }
        OptionalInt optionalMaxPeople = Converter.stringToInt(tokens.next());
        if (optionalMaxPeople.isEmpty() || optionalMaxPeople.getAsInt() < 1) {
            System.out.println(ERROR_INVALID_MAX_PEOPLE);
            return;
        }

        Product food = productManager.addFoodProduct(optionalId.getAsInt(),name, optionalPrice.getAsDouble(), expiration.get(), optionalMaxPeople.getAsInt());
        System.out.println(food);
        System.out.println(PROD_ADD_OK);

    }
    private void addMeetingCommand (CommandTokens tokens) {

        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }
        OptionalInt optionalId = Converter.stringToInt(tokens.next());
        if (optionalId.isEmpty() || productManager.existId(optionalId.getAsInt())) {
            System.out.println(ERROR_INVALID_ID);
            return;
        }

        if(!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }

        String name = tokens.next();

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }

        OptionalDouble optionalPrice = Converter.stringToDouble(tokens.next());
        if (optionalPrice.isEmpty() || !productManager.isPriceValid(optionalPrice.getAsDouble())) {
            System.out.println(ERROR_INVALID_PRICE);
            return;
        }

        if (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }

        Optional<java.time.LocalDate> expiration = Converter.stringToLocal(tokens.next());
        if (expiration.isEmpty()) {
            System.out.println(ERROR_INVALID_DATE);
            return;
        }
        LocalDate date = expiration.get();          // fecha de la reunión
        LocalDateTime meetingDateTime = date.atStartOfDay(); // 00:00 de ese día
        LocalDateTime now = LocalDateTime.now();
        long hoursBetween = ChronoUnit.HOURS.between(now, meetingDateTime);

        if (hoursBetween < 12) {
            System.out.println("Meeting must be scheduled at least 12 hours in advance");
            return;
        }

        if  (!tokens.hasNext()){
            System.out.println(ERROR_INCORRECT_USE_ADDMEETING);
            return;
        }
        OptionalInt optionalMaxPeople = Converter.stringToInt(tokens.next());
        if (optionalMaxPeople.isEmpty() || optionalMaxPeople.getAsInt() < 1) {
            System.out.println(ERROR_INVALID_MAX_PEOPLE);
            return;
        }

        Product meeting = productManager.addMeetingProduct(optionalId.getAsInt(), name, optionalPrice.getAsDouble(), expiration.get()
        ,optionalMaxPeople.getAsInt());
        System.out.println(meeting);
        System.out.println(PROD_ADD_OK);
    }
 */


}
