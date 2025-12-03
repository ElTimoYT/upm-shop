package es.upm.iwsim22_01.models;

import es.upm.iwsim22_01.models.product.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase que representa un ticket de compra en el sistema.
 * Un ticket puede contener múltiples productos, aplicar descuentos por categoría,
 * y gestionar su estado (abierto, cerrado, vacío).
 */
public class Ticket {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm");
    public static final int MAX_PRODUCTS = 100;

    private final int id;
    private final Date initialDate;
    private Date finalDate;
    private TicketState ticketState = TicketState.EMPTY;
    private final List<TicketLine> items = new ArrayList<>();

    /**
     * Constructor de la clase Ticket.
     *
     * @param id Identificador único del ticket.
     */
    public Ticket(int id) {
        this.id = id;

        initialDate = new Date();
    }

    /**
     * Obtiene el identificador del ticket.
     *
     * @return El identificador del ticket.
     */
    public int getId() {
        return id;
    }

    /**
     * Calcula el número total de unidades en el ticket.
     *
     * @return Número total de unidades.
     */
    private int totalUnits() {
        int totalUnits = 0;

        for (TicketLine item : items) {
            totalUnits += item.amount;
        }

        return totalUnits;
    }

    /**
     * Cuenta la cantidad de productos por categoría.
     *
     * @return Mapa con la cantidad de productos por categoría.
     */
    private Map<Category, Integer> countCategory() {
        Map<Category, Integer> amounts = new EnumMap<>(Category.class);
        for (Category category : Category.values()) amounts.put(category, 0);

        for (TicketLine item : items) if (item.product instanceof UnitProduct unitProduct) {
            Category category = unitProduct.getCategory();

            amounts.put(category, amounts.get(category) + item.amount);
        }

        return amounts;
    }

    /**
     * Calcula el descuento aplicable a un producto según su categoría y cantidad.
     *
     * @param product Producto del que calcular el descuento.
     * @param counts Mapa con la cantidad de productos por categoría.
     * @return Descuento aplicable al producto.
     */
    private double perItemDiscount(AbstractProduct product, Map<Category, Integer> counts) {
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

    /**
     * Redondea un valor a dos decimales.
     *
     * @param v Valor a redondear.
     * @return Valor redondeado.
     */
    private static double round1(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    /**
     * Calcula el precio total del ticket sin descuentos.
     *
     * @return Precio total sin descuentos.
     */
    private double totalPrice() {
        double total = 0.0;
        for (TicketLine item : items) {
            total += item.amount * item.product.getPrice();
        }

        return total;
    }

    /**
     * Calcula el descuento total aplicable al ticket.
     *
     * @return Descuento total.
     */

    private double discountPrice() {
        Map<Category, Integer> counts = countCategory();
        double discount = 0.0;
        for (TicketLine item : items) {
            double perItem = perItemDiscount(item.product, counts);
            discount += perItem * item.amount;
        }
        return round1(discount);
    }

    /**
     * Añade un producto al ticket.
     *
     * @param product Producto a añadir.
     * @param quantity Cantidad del producto.
     * @return true si el producto se añadió correctamente, false en caso contrario.
     */
    public boolean addProduct(AbstractProduct product, int quantity) {
        if (product == null || quantity <= 0) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;

        TicketLine item = new TicketLine(product, quantity);
        if (items.contains(item)) {
            items.get(items.indexOf(item)).amount += item.amount;
        } else {
            items.add(item);
        }

        ticketState = TicketState.OPEN;
        return true;
    }

    /**
     * Añade un producto personalizable al ticket.
     *
     * @param personalizableProduct Producto personalizable a añadir.
     * @param quantity Cantidad del producto.
     * @param lines Líneas de personalización.
     * @return true si el producto se añadió correctamente, false en caso contrario.
     */
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
        ticketState = TicketState.OPEN;
        return true;
    }

    /**
     * Elimina un producto del ticket.
     *
     * @param product Producto a eliminar.
     */
    public void removeProduct(AbstractProduct product) {
        items.remove(new TicketLine(product, 0));
    }

    /**
     * Cierra el ticket, estableciendo la fecha de cierre y cambiando su estado a CLOSED.
     */
    public void closeTicket() {
        if (ticketState.equals(TicketState.CLOSED)) return;

        finalDate = new Date();
        ticketState = TicketState.CLOSED;
    }

    /**
     * Obtiene el estado actual del ticket.
     *
     * @return Estado del ticket.
     */
    public TicketState getState() {
        return ticketState;
    }

    /**
     * Obtiene la fecha de creación del ticket.
     *
     * @return Fecha de creación.
     */
    public Date getInitialDate(){
        return initialDate;
    }

    /**
     * Devuelve el identificador del ticket formateado.
     *
     * @return Identificador formateado del ticket.
     */
    public String getFormattedId() {
        StringBuilder formattedId = new StringBuilder();

        if (ticketState.equals(TicketState.EMPTY)) {
            formattedId
                .append(DATE_FORMAT.format(initialDate))
                .append("-");
        }

        formattedId.append(getId());

        if (ticketState.equals(TicketState.CLOSED)) {
            if (finalDate != null) {
                formattedId.append("-")
                        .append(DATE_FORMAT.format(finalDate));
            }
        }

        return formattedId.toString();
    }

    /**
     * Imprime el ticket con todos sus detalles.
     *
     * @return Cadena con la representación completa del ticket.
     */
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
            AbstractProduct product = line.product;
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
                    sb.append(" **discount -").append(round1(discountPerItem));
                }
                sb.append("\n");
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

    /**
     * Verifica si todos los productos de servicio en el ticket son válidos.
     *
     * @return true si todos los productos de servicio son válidos, false en caso contrario.
     */
    public boolean areAllServiceProductsValid() {
        for (TicketLine line : items) {
            AbstractProduct product = line.product;

            if (product instanceof ProductService ps) {
                if (!ps.checkTime()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Devuelve una representación en cadena del ticket.
     *
     * @return Identificador formateado del ticket.
     */
    @Override
    public String toString() {
        return getFormattedId();
    }

    /**
     * Clase interna que representa una línea de ticket (producto y cantidad).
     */
    private static class TicketLine {
        private final AbstractProduct product;
        private int amount;

        private TicketLine(AbstractProduct product, int amount) {
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

    /**
     * Clase interna que representa una línea de ticket para productos personalizables.
     */
    private static class PersonalizableProductTicketLine extends TicketLine {
        private final String[] lines;

        private PersonalizableProductTicketLine(PersonalizableProduct product, int amount, String[] lines) {
            super(product, amount);
            this.lines = lines;
        }
    }

    /**
     * Enumeración que representa los posibles estados de un ticket.
     */
    public enum TicketState {
        EMPTY,
        OPEN,
        CLOSED;
    }
}
