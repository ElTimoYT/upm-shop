package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.PersonalizableProduct;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

public class TicketAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_ADD = "Incorrect use: ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]",
            ERROR_PRODUCT_IS_NO_PERSONALIZABLE = "Product is not personalizable",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_INVALID_AMOUNT = "Invalid amount",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",

            TICKET_ADD_OK = "ticket add: ok";

    private final TicketManager ticketManager;
    private final ProductManager productManager;
    private final CashierManager cashierManager;

    public TicketAddCommandHandler(TicketManager ticketManager, ProductManager productManager, CashierManager cashierManager) {
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

        //amount
        Integer amount = getAmount(tokens);
        if (amount == null) return;

        Product product = productManager.get(productId);
        Ticket ticket = ticketManager.get(ticketId);

        //personalizableTexts
        if (tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            if (product instanceof PersonalizableProduct personalizableProduct) {
                ticket.addProduct(personalizableProduct, amount, tokens.next().split("--p"));
            } else {
                System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                return;
            }
        } else {
            ticket.addProduct(product, amount);
        }

        System.out.println(TICKET_ADD_OK);
    }

    private String getCashierId(CommandTokens tokens) {
        if (!tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
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
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
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
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return null;
        }

        int productId = tokens.nextInt();
        if (!productManager.existId(productId)) {
            System.out.println(ERROR_PRODUCT_NOT_FOUND);
            return null;
        }
        return productId;
    }

    private static Integer getAmount(CommandTokens tokens) {
        if (!tokens.hasNextInt()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            return null;
        }

        int amount = tokens.nextInt();
        if (amount < 0 || amount > Ticket.MAX_PRODUCTS) {
            System.out.println(ERROR_INVALID_AMOUNT);
            return null;
        }
        return amount;
    }
}
