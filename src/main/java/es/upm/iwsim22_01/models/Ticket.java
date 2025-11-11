package es.upm.iwsim22_01.models;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class Ticket {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm");
    public static final int MAX_PRODUCTS = 100;

    private final Map<Product, Integer> items = new LinkedHashMap<>();
    private final Map<PersonalizableProduct, String[]> personalizableTextsPerProduct = new HashMap<>();
    private final int id;
    private final Cashier cashier;
    private final Client client;
    private final Date initialDate;
    private Date finalDate;
    private TicketState ticketState = TicketState.EMPTY;

    public Ticket(int id, Cashier cashier, Client client) {
        this.id = id;
        this.cashier = cashier;
        this.client = client;

        initialDate = new Date();
    }

    public int getId() {
        return id;
    }

    private int totalUnits() {
        //cantidad totales de productos en el ticket
        int n = 0;
        for (int q : items.values()) n += q;
        return n;
    }

    private Map<Category, Integer> countCategory() {
        //creamos mapa para las categorias
        Map<Category, Integer> num = new EnumMap<>(Category.class);
        //inicializamos cada categoria a 0 para evitar los nulls
        for (Category c : Category.values()) num.put(c, 0);
        //recorremos mapa guardamos category y añadimos la cantidad de ese producto a su categoria
        for (Entry<Product, Integer> e : items.entrySet()) {
            Category cat = e.getKey().getCategory();
            num.put(cat, num.get(cat) + e.getValue());
        }
        return num;
    }

    private double perItemDiscount(Product p, Map<Category, Integer> counts) {
        int n = counts.getOrDefault(p.getCategory(), 0);
        double resultado = 0;
        if (n >= 2) {
            double rate = p.getCategory().getDiscount();
            resultado= p.getPrice() * rate;
        }
        return resultado;
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private double totalPrice() {
        double total = 0.0;
        for (Entry<Product, Integer> e : items.entrySet()) {
            total += e.getKey().getPrice() * e.getValue();
        }
        return total;
    }

    private double discountPrice() {
        Map<Category, Integer> counts = countCategory();
        double discount = 0.0;
        for (Entry<Product, Integer> e : items.entrySet()) {
            double perItem = perItemDiscount(e.getKey(), counts);
            discount += perItem * e.getValue(); // multiplicamos por cantidad de ese producto
        }
        return round1(discount);
    }

    public boolean addProduct(PersonalizableProduct product, int quantity, String[] personalizableTexts) {  //TODO: usar producto personaalizable
        if (!addProduct(product, quantity)) return false;

        personalizableTextsPerProduct.put(
                product,
                Arrays.copyOf(personalizableTexts, Math.min(personalizableTexts.length, product.getMaxPers()))
        );
        return true;
    }

    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;

        items.put(product, items.getOrDefault(product, 0) + quantity);
        ticketState = TicketState.ACTIVE;
        return true;
    }

    public boolean removeProduct(Product product) {
        items.remove(product);

        if (product instanceof PersonalizableProduct personalizableProduct) {
            personalizableTextsPerProduct.remove(personalizableProduct);
        }

        return true;
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
        closeTicket();

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

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getFormattedId() +
                '}';
    }

    public enum TicketState {
        EMPTY,
        ACTIVE,
        CLOSED;
    }
}
