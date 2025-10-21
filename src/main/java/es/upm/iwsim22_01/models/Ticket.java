package es.upm.iwsim22_01.models;

import es.upm.iwsim22_01.manager.ProductManager;

import java.util.*;
import java.util.Map.Entry;

public class Ticket {
    private static final int MAX_PRODUCTS = 100;

    private final Map<Product, Integer> items = new LinkedHashMap<>();

    public Ticket() {}

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

    public boolean addProduct(int id, int quantity) {
        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(id);
        if (optionalProduct.isEmpty()) {
            return false;
        }

        Product product = optionalProduct.get();

        boolean result = true;
        if (product == null || quantity <= 0) result = false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) result = false;

        items.put(product, items.getOrDefault(product, 0) + quantity);
        return result;
    }

    public boolean removeProductById(int id) {
        Optional<Product> optionalProduct = ProductManager.getProductManager().getProduct(id);

        if (optionalProduct.isEmpty()) {
            return false;
        }

        items.remove(optionalProduct.get());
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Precomputamos conteo por categoría
        Map<Category, Integer> counts = countCategory();

        // Orden por nombre (ignorando mayúsculas)
        List<Entry<Product, Integer>> ordenados = new ArrayList<>(items.entrySet());
        ordenados.sort(Comparator.comparing(e -> e.getKey().getName(),
                String.CASE_INSENSITIVE_ORDER
        ));

        // Línea por producto (sin repetirlo N veces)
        for (Entry<Product, Integer> e : ordenados) {
            Product p = e.getKey();
            int qty = e.getValue();
            double dPerItem = perItemDiscount(p, counts);
            for (int i = 0; i < qty; i++) {
                sb.append(p);
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
}
