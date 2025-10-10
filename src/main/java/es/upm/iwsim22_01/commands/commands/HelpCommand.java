package es.upm.iwsim22_01.commands.commands;

import es.upm.iwsim22_01.commands.CommandStatus;
import es.upm.iwsim22_01.models.Category;

import java.util.Iterator;

public class HelpCommand implements Command {

    @Override
    public CommandStatus execute(Iterator<String> tokens) {
        System.out.print("""
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
                
                Categories:\s""");



        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.print(categories[i]);
            if (i < categories.length - 1) System.out.print(", ");

        }

        System.out.print("\nDiscounts if there are ≥2 units in the category: ");

        for (int i = 0; i < categories.length; i++) {
            System.out.print(categories[i] + " " + (int)(categories[i].getDiscount() * 100) + "%");
            if (i < categories.length - 1) System.out.print(", ");
            else System.out.print(".");
        }

        System.out.println();

        return new CommandStatus(true);
    }
}
