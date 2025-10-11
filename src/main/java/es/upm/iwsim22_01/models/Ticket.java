package es.upm.iwsim22_01.models;

import java.util.*;

/**
 * Representa un ticket de compra en el sistema de tienda.
 * <p>
 * Un ticket contiene una lista de productos y calcula automáticamente
 * los descuentos aplicables según las categorías de los productos.
 * Los descuentos se aplican cuando hay 2 o más productos de la misma categoría.
 * </p>
 */
public class Ticket {
    private static final int MAX_PRODUCTS = 100;
    private ArrayList<Product> products;

    /**
     * Constructor de la clase Ticket.
     * Inicializa una lista vacía de productos.
     */
    public Ticket() {
        this.products = new ArrayList<>();
    }

    /**
     * Cuenta el número de productos por cada categoría en el ticket.
     * 
     * @return mapa con el conteo de productos por categoría
     */
    private Map<Category, Integer> countCategory() {
        Map<Category, Integer> num = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            num.put(category, 0); //inicializamos a 0
        }
        for (Product p : products) { //contamos cuantos productos de que categoria hay en cada uno.
            num.put(p.getCategory(), num.get(p.getCategory()) + 1);
        }
        return num;
    }

    /**
     * Calcula el descuento aplicable a un producto específico.
     * El descuento se aplica solo si hay 2 o más productos de la misma categoría.
     * 
     * @param product producto al que calcular el descuento
     * @param counts mapa con el conteo de productos por categoría
     * @return descuento aplicable al producto
     */
    private double perItemDiscount(Product product, Map<Category, Integer> counts) {
        int n = counts.getOrDefault(product.getCategory(), 0);
        double resultDiscount = 0.0;
        if (n >= 2) {
            double discount = product.getCategory().getDiscount();
            resultDiscount= product.getPrice() * discount;
        }
        return resultDiscount;
    }

    /**
     * Redondea un valor a un decimal.
     * 
     * @param value valor a redondear
     * @return valor redondeado a un decimal
     */
    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0; // si quieres un decimal (ej: -3.0)
    }

    /**
     * Calcula el precio total del ticket sin descuentos.
     * 
     * @return precio total de todos los productos
     */
    private double totalPrice() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    /**
     * Calcula el descuento total aplicable al ticket.
     * 
     * @return descuento total redondeado a un decimal
     */
    private double discountPrice() {
        double discount = 0;
        Map<Category, Integer> counts = countCategory();
        for (Product p : products) {
            discount += perItemDiscount(p, counts);
        }
        return round(discount);
    }

    /**
     * Añade productos al ticket.
     *
     * @param product producto a añadir
     * @param quantity cantidad de productos a añadir
     * @return true si se añadieron todos los productos solicitados, false si no se añadió ninguno
     */
    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }

        int remainingSpace = MAX_PRODUCTS - products.size();
        if (quantity > remainingSpace) {
            // No hay espacio suficiente para todos -> no añadir nada
            return false;
        }

        for (int i = 0; i < quantity; i++) {
            products.add(product);
        }

        return true;
    }

    /**
     * Elimina todos los productos con el identificador especificado del ticket.
     * 
     * @param id identificador del producto a eliminar
     * @return true si se eliminó al menos un producto, false en caso contrario
     */
    public boolean removeProductById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    /**
     * Devuelve una representación detallada del ticket.
     * Incluye todos los productos con sus descuentos aplicables y los totales.
     * 
     * @return cadena que representa el ticket completo
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Map<Category, Integer> counts = countCategory();
        double totalPrice = totalPrice();
        double discountPrice = discountPrice();
        products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        for (Product p : products) {
            str.append(p.toString());
            double d = perItemDiscount(p, counts);
            if (d > 0) {
                str.append(" **discount -").append(round(d));
            }
            str.append("\n");
        }
        str.append("Total price: ").append(totalPrice);
        str.append("\n");
        str.append("Total discount: ").append(discountPrice);
        str.append("\n");
        str.append("Final Price: ").append(totalPrice() - discountPrice);
        return str.toString();
    }
}
