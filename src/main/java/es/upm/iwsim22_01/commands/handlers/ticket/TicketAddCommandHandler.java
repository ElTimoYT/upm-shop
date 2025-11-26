package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.Validators.ServiceProductTimeValidator;
import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TicketAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_ADD = "Incorrect use: ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]",
            ERROR_PRODUCT_IS_NO_PERSONALIZABLE = "Product is not personalizable",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_INVALID_AMOUNT = "Invalid amount",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",

            TICKET_CLOSED = "Ticket closed",
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
        try {
            int ticketId = tokens.nextInt();
            if (!ticketManager.existId(ticketId)) {
                System.out.println(ERROR_TICKET_NOT_FOUND);
                return;
            }

            String cashierId = tokens.next();
            if (!cashierManager.existId(cashierId)) {
                System.out.println(ERROR_CASHIER_NOT_FOUND);
                return;
            }

            int productId = tokens.nextInt();
            if (!productManager.existId(productId)) {
                System.out.println(ERROR_PRODUCT_NOT_FOUND);
                return;
            }

            int amount = tokens.nextInt();
            if (amount <= 0 || amount > Ticket.MAX_PRODUCTS) {
                System.out.println(ERROR_INVALID_AMOUNT);
                return;
            }

            Product product = productManager.get(productId);
            Ticket ticket = ticketManager.get(ticketId);

            if(ticket.getState() != Ticket.TicketState.CLOSED){
                if (product instanceof ProductService) {
                    ProductService productService = (ProductService) product;
                    if (!ServiceProductTimeValidator.isValid(productService)) {
                        return;
                    }
                    productService.setPersonasApuntadas(amount + productService.getPersonasApuntadas());
                    ticket.addProduct(productService, amount);

                }else{
                    if (tokens.hasNext()) {
                        if (product instanceof PersonalizableProduct personalizableProduct) {
                            List<String> personalization = new ArrayList<>();
                            while (tokens.hasNext()) {
                                String tok = tokens.next();
                                if (tok.startsWith("--p")) {
                                    String text = tok.substring(3).trim(); // quitar "--p"
                                    if (!text.isEmpty()) {
                                        personalization.add(text);
                                    }
                                }
                            }
                            String[] lines = personalization.toArray(new String[0]);
                            ticket.addProduct(personalizableProduct, amount, lines);
                        } else {
                            System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                            return;
                        }
                    } else {
                        ticket.addProduct(product, amount);
                    }
                }

                System.out.println(ticket.printTicket());
                System.out.println(TICKET_ADD_OK);
            } else{
                System.out.println(TICKET_CLOSED);
            }


        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
        }
    }


}
