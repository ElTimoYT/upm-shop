package es.upm.iwsim22_01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import es.upm.iwsim22_01.commands.CommandDispatcher;
import es.upm.iwsim22_01.commands.handlers.EchoCommandHandler;
import es.upm.iwsim22_01.commands.handlers.ExitCommandHandler;
import es.upm.iwsim22_01.commands.handlers.HelpCommandHandler;
import es.upm.iwsim22_01.commands.handlers.cashier.CashierCommandHandler;
import es.upm.iwsim22_01.commands.handlers.client.ClientCommandHandler;
import es.upm.iwsim22_01.commands.handlers.prod.ProdCommandHandler;
import es.upm.iwsim22_01.commands.handlers.ticket.TicketCommandHandler;
import es.upm.iwsim22_01.service.service.CashierService;
import es.upm.iwsim22_01.service.service.ClientService;
import es.upm.iwsim22_01.service.service.ProductService;
import es.upm.iwsim22_01.service.service.TicketService;

/**
 * Clase principal de la aplicación de gestión de tickets.
 * Esta clase inicializa los gestores y el despachador de comandos,
 * y proporciona un bucle interactivo para procesar comandos de usuario.
 */
public class App {
    private static boolean menu = true;
    private static CommandDispatcher dispatcher = new CommandDispatcher();

    private static ProductService productManager = new ProductService();
    private static TicketService ticketManager = new TicketService();
    private static CashierService cashierManager = new CashierService(ticketManager);
    private static ClientService clientManager = new ClientService(cashierManager);

    /**
     * Método principal de la aplicación.
     * Registra los manejadores de comandos y procesa las entradas del usuario.
     *
     * @param args Argumentos de línea de comandos. Si se proporciona un archivo, se ejecutarán los comandos desde él.
     */
    public static void main(String[] args) {
        // Registrar manejadores de comandos
        dispatcher.addCommand("exit", new ExitCommandHandler());
        dispatcher.addCommand("help", new HelpCommandHandler());
        dispatcher.addCommand("echo", new EchoCommandHandler());
        dispatcher.addCommand("prod", new ProdCommandHandler(productManager));
        dispatcher.addCommand("client", new ClientCommandHandler(clientManager, cashierManager));
        dispatcher.addCommand("cash", new CashierCommandHandler(cashierManager, ticketManager));
        dispatcher.addCommand("ticket", new TicketCommandHandler(ticketManager, productManager, cashierManager, clientManager));

        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");

        // Procesar comandos desde archivo si se proporciona
        Scanner scanner;
        if (args.length >= 1) {
            try {
                scanner = new Scanner(new FileReader(args[0]));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("tUPM> " + line);
                    dispatcher.processCommand(line);
                    System.out.println();
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("Fichero " + args[0] + " no encontrado.");
            }
        }

        // Procesar comandos desde consola
        scanner = new Scanner(System.in);
        while (menu) {
            System.out.print("tUPM> ");
            dispatcher.processCommand(scanner.nextLine());

            System.out.println();
        }

        System.out.println("""
                Closing application.
                Goodbye!
                """);
    }

    /**
     * Finaliza el bucle principal del menú.
     */
    public static void exitMenu() {
        menu = false;
    }
}

