package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

import es.upm.iwsim22_01.models.Category;

public class HelpCommandHandler implements CommandHandler {
    private static final String HELP_MESSAGE = """
                Commands:
                 prod add <id> "<name>" <category> <price>
                 prod list
                 prod update <id> NAME|CATEGORY|PRICE <value>
                 prod remove <id>
                 ticket new
                 ticket add <prodId> <quantity>
                 ticket remove <prodId>
                 ticket print
                 echo "<texto>"
                 help
                 exit
                
                Categories:\s""",
        DISCOUNT_MESSAGE = "\nDiscounts if there are ≥2 units in the category: ";

    @Override
    public void runCommand(Iterator<String> tokens) {
        System.out.print(HELP_MESSAGE);

        Category[] categories = Category.values();
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
