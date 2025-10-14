package es.upm.iwsim22_01.commands.commands;

import java.util.Iterator;

public class EchoCommandHandler implements CommandHandler {

    @Override
    public void runCommand(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            System.out.println("Incorrect use: echo \"<message>\"");
        }

        System.out.println("echo \"" + tokens.next() + "\"");
    }
}
