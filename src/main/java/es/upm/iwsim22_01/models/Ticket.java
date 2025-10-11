package es.upm.iwsim22_01.models;

import java.util.*;
import java.util.Map.Entry;
/**
 * Representa un ticket de compra que contiene un conjunto de productos
 * y sus cantidades asociadas. Permite calcular el precio total,
 * los descuentos aplicables por categoría y generar una representación
 * textual detallada del ticket.
 *
 * <p>El ticket almacena los productos en un {@link LinkedHashMap}, donde la clave
 * es el {@link Product} y el valor es la cantidad de unidades de ese producto.
 * Si se añaden varios productos de la misma categoría, se aplican descuentos
 * según las reglas definidas en la clase {@link Category}.</p>
 */

public class Ticket {
    /** Número máximo total de unidades permitidas en el ticket. */
    private static final int MAX_PRODUCTS = 100;
    /** Mapa que almacena los productos y sus cantidades correspondientes. */
    private final Map<Product, Integer> items = new LinkedHashMap<>();

    /**
     * Constructor del ticket, creo uno nuevo vacio.
     */

    public Ticket() {}

    /**
     * Calcula la cantidad total de productos que hay en el ticket.
     * @return devuelve la cantidad total.
     */

    private int totalUnits() {
        //cantidad totales de productos en el ticket
        int n = 0;
        for (int q : items.values()) n += q;
        return n;
    }

    /**
     * Genera un mapa con la cantidad de productos que tienen la misma categoria
     * iniciando cada categoria a 0.
     *
     * @return devuelve mapa con la caniridad de productos por categoria correspondientes.
     */
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

    /**
     * Calcula el descuento por producto solamente si se cumple que hay mas de 2 productos por
     * categoria.
     *
     * @param p producto para el cual se evalua si hay descuento o no.
     * @param counts mapa que contiene el número total de unidades por categoría
     * @return devuelve el importe del decuento por unidad de producto.
     */
    private double perItemDiscount(Product p, Map<Category, Integer> counts) {
        int n = counts.getOrDefault(p.getCategory(), 0);
        double resultado = 0;
        if (n >= 2) {
            double rate = p.getCategory().getDiscount();
            resultado= p.getPrice() * rate;
        }
        return resultado;
    }

    /**
     * devuelve numero redondeado
     *
     * @param v valor a redondear
     * @return valor redondeado con una cifra decimal
     */

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    /**
     * Calcula el precio total de todos los productos sin aplicar descuento.
     *
     * @return devuelve precio total.
     */

    private double totalPrice() {
        double total = 0.0;
        for (Entry<Product, Integer> e : items.entrySet()) {
            total += e.getKey().getPrice() * e.getValue();
        }
        return total;
    }

    /**
     * Calcula el descuento total aplicable al ticket sumando los descuentos
     * individuales de cada producto.
     *
     * @return devuelve descuento total redondeado.
     */

    private double discountPrice() {
        Map<Category, Integer> counts = countCategory();
        double discount = 0.0;
        for (Entry<Product, Integer> e : items.entrySet()) {
            double perItem = perItemDiscount(e.getKey(), counts);
            discount += perItem * e.getValue(); // multiplicamos por cantidad de ese producto
        }
        return round1(discount);
    }

    /**
     * Añade una cantidad específica de un producto al ticket.
     * Si el producto ya existe, se acumula su cantidad.
     * No se añaden productos si se supera el límite máximo total.
     *
     * @param product producto a añadir.
     * @param quantity cantidad del producto a añador.
     * @return devuelve estado del proceso, true si se ha podido añadir false si no
     */



    public boolean addProduct(Product product, int quantity) {
        boolean result = true;
        if (product == null || quantity <= 0) result = false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) result = false;

        items.put(product, items.getOrDefault(product, 0) + quantity);
        return result;
    }

    /**
     ** Elimina un producto del ticket en función de su identificador único.
     *  Si el producto se encuentra, se elimina su entrada del mapa.
     *
     * @param id identificador del producto
     * @return devuelve si se pudo eleminar o no ese producto.
     */

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
    /**
     * Devuelve una representación textual del ticket,
     * mostrando los productos ordenados alfabéticamente por nombre.
     * Cada producto aparece tantas veces como unidades existan en el ticket.
     * Si un producto tiene descuento, se muestra junto a su importe de descuento.
     *
     * <p>Incluye también el precio total, el descuento total y el precio final del ticket.</p>
     *
     * @return una cadena de texto que representa el contenido completo del ticket.
     */

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
