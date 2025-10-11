package es.upm.iwsim22_01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.CommandStatus;
import es.upm.iwsim22_01.commands.commands.EchoCommandHandler;
import es.upm.iwsim22_01.commands.commands.ExitCommandHandler;
import es.upm.iwsim22_01.commands.commands.HelpCommandHandler;
import es.upm.iwsim22_01.commands.commands.ProdCommandHandler;
import es.upm.iwsim22_01.commands.commands.TicketCommandHandler;
import es.upm.iwsim22_01.models.Ticket;

/**
 * Clase principal de la aplicación de sistema de tienda UPM.
 * <p>
 * Esta aplicación proporciona una interfaz de línea de comandos para gestionar
 * productos y tickets de compra. Soporta entrada desde archivo o consola.
 * </p>
 */
public class App {
    private static boolean menu = true;
    private static Ticket ticket = new Ticket();
    private static CommandDispatcher dispatcher = new CommandDispatcher();

    /**
     * Método principal de la aplicación.
     * <p>
     * Inicializa el sistema de comandos, configura la entrada (archivo o consola)
     * y ejecuta el bucle principal de procesamiento de comandos.
     * </p>
     * 
     * @param args argumentos de línea de comandos. Si se proporciona un archivo,
     *             se usará como entrada, sino se usará la consola
     */
    public static void main(String[] args) {
        dispatcher.addCommand("exit", new ExitCommandHandler());
        dispatcher.addCommand("help", new HelpCommandHandler());
        dispatcher.addCommand("echo", new EchoCommandHandler());
        dispatcher.addCommand("prod", new ProdCommandHandler());
        dispatcher.addCommand("ticket", new TicketCommandHandler());

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
            CommandStatus commandStatus = dispatcher.processCommand(scanner.nextLine());
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

    /**
     * Termina el bucle principal de la aplicación.
     */
    public static void exitMenu() {
        menu = false;
    }

    /**
     * Obtiene el ticket actual de la aplicación.
     * 
     * @return el ticket actual
     */
    public static Ticket getCurrentTicket() {
        return ticket;
    }

    /**
     * Crea un nuevo ticket vacío.
     */
    public static void resetTicket() {
        ticket = new Ticket();
    }

    /**
     * Verifica si existe un ticket actual.
     * 
     * @return true si existe un ticket, false en caso contrario
     */
    public static boolean existsTicket() {
        return ticket != null;
    }
}

