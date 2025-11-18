package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

public class TicketRemoveCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_REMOVE = "Incorrect use: ticket remove <ticketId> <cashId> <prodId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",

            TICKET_REMOVAL_OK = "ticket remove: ok";

    private final TicketManager ticketManager;
    private final ProductManager productManager;
    private final CashierManager cashierManager;

    public TicketRemoveCommandHandler(TicketManager ticketManager, ProductManager productManager, CashierManager cashierManager) {
        this.ticketManager = ticketManager;
        this.productManager = productManager;
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        //ticketId
        Integer ticketId = getTicketId(tokens);
        if (ticketId == null) return;

        //cashierId
        String cashierId = getCashierId(tokens);
        if (cashierId == null) return;

        //productId
        Integer productId = getProductId(tokens);
        if (productId == null) return;

        Product product = productManager.get(productId);
        Ticket ticket = ticketManager.get(ticketId);

        ticket.removeProduct(product);

        System.out.println(ticket);
        System.out.println(product);
        System.out.println(TICKET_REMOVAL_OK);
    }

    private String getCashierId(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
            return null;
        }

        String cashierId = tokens.next();
        if (!cashierManager.existId(cashierId)) {
            System.out.println(ERROR_CASHIER_NOT_FOUND);
            return null;
        }
        return cashierId;
    }

    private Integer getTicketId(CommandTokens tokens) {
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
            return null;
        }

        int ticketId = tokens.nextInt();
        if (!ticketManager.existId(ticketId)) {
            System.out.println(ERROR_TICKET_NOT_FOUND);
            return null;
        }
        return ticketId;
    }

    private Integer getProductId(CommandTokens tokens) {
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
            return null;
        }

        int productId = tokens.nextInt();
        if (!productManager.existId(productId)) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return null;
        }
        return productId;
    }
}
