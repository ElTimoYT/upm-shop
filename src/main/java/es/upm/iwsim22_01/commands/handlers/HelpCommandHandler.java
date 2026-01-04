package es.upm.iwsim22_01.commands.handlers;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.service.dto.product.CategoryDTO;

public class HelpCommandHandler implements CommandHandler {
    private static final String HELP_MESSAGE = """
            Commands:
                  client add "<nombre>" <DNI> <email> <cashId>
                  client remove <DNI>
                  client list
                  cash add [<id>] "<nombre>"<email>
                  cash remove <id>
                  cash list
                  cash tickets <id>
                  ticket new [<id>] <cashId> <userId>
                  ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]
                  ticket remove <ticketId><cashId> <prodId>
                  ticket print <ticketId> <cashId>
                  ticket list
                  prod add <id> "<name>" <category> <price>
                  prod update <id> NAME|CATEGORY|PRICE <value>
                  prod addFood [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                  prod addMeeting [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                  prod list
                  prod remove <id>
                  help
                  echo “<text>”
                  exit
            
            Categories:\s""",
        DISCOUNT_MESSAGE = "\nDiscounts if there are ≥2 units in the category: ";

    @Override
    public void runCommand(CommandTokens tokens) {
        System.out.print(HELP_MESSAGE);

        CategoryDTO[] categories = CategoryDTO.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.print(categories[i]);
            if (i < categories.length - 1) System.out.print(", ");

        }

        System.out.print(DISCOUNT_MESSAGE);

        for (int i = 0; i < categories.length; i++) {
            System.out.print(categories[i] + " " + (int)(categories[i].getDiscount() * 100) + "%");
            if (i < categories.length - 1) System.out.print(", ");
            else System.out.print(".");
        }
    }
}
