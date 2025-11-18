package es.upm.iwsim22_01.models;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

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
            int n = counts.getOrDefault(unitProduct.getCategory(), 0);
            double resultado = 0;
            if (n >= 2) {
                double rate = unitProduct.getCategory().getDiscount();
                resultado = unitProduct.getPrice() * rate;
            }
            return resultado;
        }

        return 0;
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
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
        if (personalizableProduct == null || quantity <= 0 || personalizableProduct.getMaxPers() < lines.length) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;

        TicketLine item = new PersonalizableProductTicketLine(personalizableProduct, quantity, lines);
        if (items.contains(item)) {
            items.get(items.indexOf(item)).amount += item.amount;
        } else {
            items.add(item);
        }

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

    public String getPrintedTicket() {
        /*closeTicket();

        StringBuilder sb = new StringBuilder();
        // Precomputamos conteo por categoría
        Map<Category, Integer> counts = countCategory();

        // Orden por nombre (ignorando mayúsculas)
        List<Entry<Product, Integer>> ordenados = new ArrayList<>(items.entrySet());
        ordenados.sort(Comparator.comparing(e -> e.getKey().getName(),
                String.CASE_INSENSITIVE_ORDER
        ));

        sb.append(getFormattedId());

        // Línea por producto (sin repetirlo N veces)
        for (Entry<Product, Integer> entry : ordenados) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double dPerItem = perItemDiscount(product, counts);

            for (int i = 0; i < quantity; i++) {
                sb.append(product);
                if (product instanceof PersonalizableProduct personalizableProduct) {
                    for (String text : personalizableTextsPerProduct.getOrDefault(personalizableProduct, new String[0])) {
                        sb.append("\n\t-")
                                .append(text);
                    }
                }
                if (dPerItem > 0) {
                    sb.append(" **discount -").append(round1(dPerItem));
                }
                sb.append("\n");
            }
        }

        double total = totalPrice();
        double discount = discountPrice();
        sb.append("Total price: ").append(total).append("\n");
        sb.append("Total discount: ").append(discount).append("\n");
        sb.append("Final Price: ").append(round1(total - discount));

        return sb.toString();*/

        return "";
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getFormattedId() +
                '}';
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
                return ticketLine.product.equals(product);
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
}
