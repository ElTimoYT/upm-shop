package es.upm.iwsim22_01;

import es.upm.iwsim22_01.command.CommandStatus;
import es.upm.iwsim22_01.command.handler.CommandHandler;

import es.upm.iwsim22_01.models.Ticket;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class App {
    private static boolean menu = true;
    private static Ticket ticket = null;

    public static void main(String[] args) {
        Scanner scanner;
        if (args.length >= 1) {
            try {
                scanner = new Scanner(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                System.err.println("Fichero " + args[0] + " no encontrado.");
                scanner = new Scanner(System.in);
            }
        } else {
            scanner = new Scanner(System.in);
        }

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        while (menu) {
            System.out.print("tUPM> ");
            CommandStatus commandStatus = CommandHandler.runCommand(scanner.nextLine());
            if (!commandStatus.getStatus()) {
                System.err.println(commandStatus.getMessage());
            } else if (commandStatus.getMessage() != null) {
                System.out.println(commandStatus.getMessage());
            }
        }

        System.out.println("""
                Closing application.
                Goodbye!
                """);
    }

    public static void exitMenu() {
        menu = false;
    }

    public static Ticket getCurrentTicket() {
        return ticket;
    }

    public static void resetTicket() {
        ticket = new Ticket();
    }

    public static boolean existsTicket() {
        return ticket != null;
    }
}

