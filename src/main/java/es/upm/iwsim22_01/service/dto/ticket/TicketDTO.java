package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.category.Categorizable;
import es.upm.iwsim22_01.service.dto.product.category.Category;
import es.upm.iwsim22_01.service.dto.Validable;
import es.upm.iwsim22_01.service.dto.product.*;
import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;

import java.nio.file.attribute.AttributeView;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase que representa un ticket de compra en el sistema.
 * Un ticket puede contener múltiples productos, aplicar descuentos por categoría,
 * y gestionar su estado (abierto, cerrado, vacío).
 */
public class TicketDTO {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm");
    public static final int MAX_PRODUCTS = 100;

    private final int id;
    private final Date initialDate;
    private Date finalDate;
    private TicketState state;
    private final List<AbstractProductDTO> products;
    private TicketType ticketType;

    public enum TicketType {
        COMMON,
        COMPANY
    }

    public TicketDTO(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products, TicketType ticketType) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.state = state;
        this.products = products;
        this.ticketType = ticketType;
    }

    public TicketDTO(int id, Date initialDate, Date finalDate) {
        this(id, initialDate, finalDate, TicketState.EMPTY, new ArrayList<>(), TicketType.COMMON);
    }

    public TicketDTO(int id) {
        this(id, new Date(), null);
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public TicketState getTicketState() {
        return state;
    }

    protected void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    /**
     * Obtiene el identificador del ticket.
     *
     * @return El identificador del ticket.
     */
    public int getId() {
        return id;
    }

    public List<AbstractProductDTO> getProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Calcula el número total de unidades en el ticket.
     *
     * @return Número total de unidades.
     */
    private int totalUnits() {
        int totalUnits = 0;

        for (AbstractProductDTO product : products) {
            totalUnits += product.getAmount();
        }

        return totalUnits;
    }

    /**
     * Cuenta la cantidad de productos por categoría.
     *
     * @return Mapa con la cantidad de productos por categoría.
     */
    private Map<Category, Integer> countCategories() {
        Map<Category, Integer> amounts = new HashMap<>();

        for (AbstractProductDTO product : products) if (product instanceof Categorizable hasCategory) {
            Category category = hasCategory.getCategory();

            amounts.put(category, amounts.getOrDefault(category, 0) + product.getAmount());
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
    private double perItemDiscount(AbstractProductDTO product, Map<Category, Integer> counts) {
        if (product instanceof ProductDTO unitProduct) {
            ProductCategoryDTO category = unitProduct.getCategory();
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
        for (AbstractProductDTO product : products) {
            total += product.getAmount() * product.getPrice();
        }

        return total;
    }

    /**
     * Calcula el descuento total aplicable al ticket.
     *
     * @return Descuento total.
     */

    private double discountPrice() {
        Map<Category, Integer> counts = countCategories();
        double discount = 0.0;
        for (AbstractProductDTO product : products) {
            double perItem = perItemDiscount(product, counts);
            discount += perItem * product.getAmount();
        }
        return round1(discount);
    }

    /**
     * Añade un producto al ticket.
     *
     * @param productToAdd Producto a añadir.
     * @param quantity Cantidad del producto.
     * @return true si el producto se añadió correctamente, false en caso contrario.
     */
    public boolean addProduct(AbstractProductDTO productToAdd, int quantity) {
        if (productToAdd == null || quantity <= 0) return false;

        int remaining = MAX_PRODUCTS - totalUnits();
        if (quantity > remaining) return false;

        if (products.contains(productToAdd)) {
            products.get(products.indexOf(productToAdd)).addAmount(quantity);
        } else {
            productToAdd.addAmount(quantity);
            products.add(productToAdd);
        }

        if (productToAdd instanceof PersonalizableDTO personalizableDTO) {
            double basePrice = productToAdd.getPrice();
            double newPrice = basePrice * (1 + 0.10 * personalizableDTO.getLines().length);
            productToAdd.setPrice(newPrice);
        }

        state = TicketState.OPEN;
        return true;
    }

    /**
     * Elimina un producto del ticket.
     *
     * @param product Producto a eliminar.
     */
    public void removeProduct(AbstractProductDTO product) {
        products.remove(product);
    }

    /**
     * Cierra el ticket, estableciendo la fecha de cierre y cambiando su estado a CLOSED.
     */
    public void closeTicket() {
        if (state.equals(TicketState.CLOSED)) return;

        finalDate = new Date();
        state = TicketState.CLOSED;
    }

    /**
     * Obtiene el estado actual del ticket.
     *
     * @return Estado del ticket.
     */
    public TicketState getState() {
        return state;
    }

    /**
     * Obtiene la fecha de creación del ticket.
     *
     * @return Fecha de creación.
     */
    public Date getInitialDate(){
        return initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    /**
     * Devuelve el identificador del ticket formateado.
     *
     * @return Identificador formateado del ticket.
     */
    public String getFormattedId() {
        StringBuilder formattedId = new StringBuilder();

        if (state.equals(TicketState.EMPTY)) {
            formattedId
                .append(DATE_FORMAT.format(initialDate))
                .append("-");
        }

        formattedId.append(getId());

        if (state.equals(TicketState.CLOSED)) {
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

        Map<Category, Integer> counts = countCategories();
        List<AbstractProductDTO> sortedItems = new ArrayList<>(products);
        sortedItems.sort(Comparator.comparing(
                AbstractProductDTO::getName,
                String.CASE_INSENSITIVE_ORDER
        ));

        for (AbstractProductDTO product : sortedItems) {
            double discountPerItem = perItemDiscount(product, counts);


            if (product instanceof AbstractPeopleProductDTO service) {
                sb.append(service.printTicketWithPeople()).append("\n");
                continue;
            }


            for (int i = 0; i < product.getAmount(); i++) {
                String productText = product.toString();
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
        return products.stream()
                .filter(p -> p instanceof Validable)
                .map(p -> (Validable) p)
                .allMatch(Validable::isValid);
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

    @Override
    public boolean equals(Object o) {
        if (o == null) return  false;
        if (o == this) return true;
        if (!(o instanceof TicketDTO ticketDTO)) return false;

        return id == ticketDTO.id;
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
