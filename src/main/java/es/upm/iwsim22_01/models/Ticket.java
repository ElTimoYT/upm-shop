package es.upm.iwsim22_01.models;

import java.text.SimpleDateFormat;
import java.util.*;

public class Ticket {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm");
    public static final int MAX_PRODUCTS = 100;

    private final int id;
    private final Date initialDate;
    private Date finalDate;
    private TicketState ticketState = TicketState.EMPTY;
    private final List<TicketLine> items = new ArrayList<>();

    public Ticket(int id) {
        this.id = id;

        initialDate = new Date();
    }

    public int getId() {
        return id;
    }

    private int totalUnits() {
        int totalUnits = 0;

        for (TicketLine item : items) {
            totalUnits += item.amount;
        }

        return totalUnits;
    }

    private Map<Category, Integer> countCategory() {
        Map<Category, Integer> amounts = new EnumMap<>(Category.class);
        for (Category category : Category.values()) amounts.put(category, 0);

        for (TicketLine item : items) if (item.product instanceof UnitProduct unitProduct) {
            Category category = unitProduct.getCategory();

            amounts.put(category, amounts.get(category) + item.amount);
        }

        return amounts;
    }

    private double perItemDiscount(Product product, Map<Category, Integer> counts) {
        if (product instanceof UnitProduct unitProduct) {
            Category category = unitProduct.getCategory();
            int n = counts.getOrDefault(category, 0);
            if (n < 2) {
                return 0.0;
            }
            double rate = category.getDiscount();
            return product.getPrice() * rate;
        }
        return 0.0;
    }

    private static double round1(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private double totalPrice() {
        double total = 0.0;
        for (TicketLine item : items) {
            total += item.amount * item.product.getPrice();
        }

        return total;
    }

    private double discountPrice() {
        Map<Category, Integer> counts = countCategory();
        double discount = 0.0;
        for (TicketLine item : items) {
            double perItem = perItemDiscount(item.product, counts);
            discount += perItem * item.amount;
        }
        return round1(discount);
    }

    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;

        TicketLine item = new TicketLine(product, quantity);
        if (items.contains(item)) {
            items.get(items.indexOf(item)).amount += item.amount;
        } else {
            items.add(item);
        }

        ticketState = TicketState.ACTIVE;
        return true;
    }


    public boolean addProduct(PersonalizableProduct personalizableProduct, int quantity, String[] lines) {
        if (personalizableProduct == null || quantity <= 0) return false;
        if (lines == null) lines = new String[0];
        if (personalizableProduct.getMaxPers() < lines.length) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;


        int numTexts = 0;
        for (String t : lines) {
            if (t != null && !t.isBlank()) {
                numTexts++;
            }
        }

        double basePrice = personalizableProduct.getPrice();
        double recargado = basePrice * (1 + 0.10 * numTexts);

        PersonalizableProduct ticketProduct = new PersonalizableProduct(
                personalizableProduct.getId(),
                personalizableProduct.getName(),
                personalizableProduct.getCategory(),
                recargado,
                personalizableProduct.getMaxPers()
        );

        TicketLine item = new PersonalizableProductTicketLine(ticketProduct, quantity, lines);
        items.add(item);
        ticketState = TicketState.ACTIVE;
        return true;
    }

    public void removeProduct(Product product) {
        items.remove(new TicketLine(product, 0));
    }

    public void closeTicket() {
        if (ticketState.equals(TicketState.CLOSED)) return;

        finalDate = new Date();
        ticketState = TicketState.CLOSED;
    }

    public TicketState getState() {
        return ticketState;
    }

    public Date getInitialDate(){
        return initialDate;
    }

    public String getFormattedId() {
        StringBuilder formattedId = new StringBuilder()
                .append(DATE_FORMAT.format(initialDate))
                .append("-")
                .append(getId());

        if (finalDate != null) {
            formattedId.append("-")
                    .append(DATE_FORMAT.format(finalDate));
        }

        return formattedId.toString();
    }
    public String printTicket() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket: ").append(getFormattedId()).append("\n");

        Map<Category, Integer> counts = countCategory();
        List<TicketLine> sortedItems = new ArrayList<>(items);
        sortedItems.sort(Comparator.comparing(
                line -> line.product.getName(),
                String.CASE_INSENSITIVE_ORDER
        ));

        for (TicketLine line : sortedItems) {
            Product product = line.product;
            int quantity = line.amount;
            double discountPerItem = perItemDiscount(product, counts);


            if (product instanceof ProductService service) {
                sb.append(service.printTicketWithPeople()).append("\n");
                continue;
            }


            for (int i = 0; i < quantity; i++) {
                String productText = product.toString();
                if (line instanceof PersonalizableProductTicketLine persLine) {
                    List<String> pers = new ArrayList<>();
                    for (String text : persLine.lines) {
                        if (text != null && !text.isBlank()) {
                            pers.add(text.trim());
                        }
                    }
                    if (!pers.isEmpty()) {
                        String withPers = productText.substring(0, productText.length() - 1) +
                                ", personalizationList:" + pers + "}";
                        productText = withPers;
                    }
                }
                sb.append(productText);
                if (discountPerItem > 0) {
                    sb.append(" **discount -").append(round1(discountPerItem)).append("\n");
                }
            }
        }

        sb.append("\n");

        double total = totalPrice();
        double discount = discountPrice();

        sb.append("Total price: ").append(String.format("%.2f", total)).append("\n");
        sb.append("Total discount: ").append(String.format("%.2f", discount)).append("\n");
        sb.append("Final Price: ").append(String.format("%.2f", round1(total - discount)));

        return sb.toString();
    }


    @Override
    public String toString() {
        return getFormattedId();
    }

    private static class TicketLine {
        private final Product product;
        private int amount;

        private TicketLine(Product product, int amount) {
            this.product = product;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) return false;
            if (object == this) return true;

            if (object instanceof TicketLine ticketLine) {
                return ticketLine.product.getId() == this.product.getId();
            }

            return false;
        }
    }

    private static class PersonalizableProductTicketLine extends TicketLine {
        private final String[] lines;

        private PersonalizableProductTicketLine(PersonalizableProduct product, int amount, String[] lines) {
            super(product, amount);
            this.lines = lines;
        }
    }

    public enum TicketState {
        EMPTY,
        ACTIVE,
        CLOSED;
    }
    public boolean areAllServiceProductsValid() {
        for (TicketLine line : items) {
            Product product = line.product;

            if (product instanceof ProductService ps) {
                if (!ps.checkTime()) {
                    return false;
                }
            }
        }
        return true;
    }
}
