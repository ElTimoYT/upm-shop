package es.upm.iwsim22_01.models;

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

    // Conteo por categoría (cuenta unidades, no productos distintos)
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

    // Descuento POR ÍTEM para un producto dado (no multiplica por cantidad)
    private double perItemDiscount(Product p, Map<Category, Integer> counts) {
        int n = counts.getOrDefault(p.getCategory(), 0);
        if (n >= 2) {
            double rate = p.getCategory().getDiscount();
            return p.getPrice() * rate;
        }
        return 0.0;
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


    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;

        items.put(product, items.getOrDefault(product, 0) + quantity);
        return true;
    }

    public boolean removeProductById(int id) {
        Product productToRemove = null;
        boolean removed = false;
        // Recorremos mapa buscando id para guardar el producto
        for (Product p : items.keySet()) {
            if (p.getId() == id) {
                productToRemove = p; 
            }
        }

        // Al salir del bucle, comprobamos si encontramos algo
        if (productToRemove != null) {
            items.remove(productToRemove); // eliminamos la entrada del mapa
            removed = true;                 // devolvemos true si se eliminó
        }
        // Si no se encontró ningún producto con ese id, devolvemos false
        return removed;
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
                sb.append(p.toString());
                if (dPerItem > 0) {
                    sb.append(" **discount -").append(round1(dPerItem));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        double total = totalPrice();
        double discount = discountPrice();
        sb.append("Total price: ").append(total).append("\n");
        sb.append("Total discount: ").append(discount).append("\n");
        sb.append("Final Price: ").append(round1(total - discount));

        return sb.toString();
    }
}
