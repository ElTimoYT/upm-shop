package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.Validable;
import es.upm.iwsim22_01.service.dto.product.*;
import es.upm.iwsim22_01.service.dto.product.category.Categorizable;
import es.upm.iwsim22_01.service.dto.product.category.Category;
import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;
import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;
import es.upm.iwsim22_01.service.printer.TicketPrinter;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase abstracta que representa un ticket con productos y servicios y aplica reglas de negocio de precios y descuentos.
 */
public abstract class AbstractTicketDTO {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH:mm");
    public static final int MAX_PRODUCTS = 100;
    private static final double EXTRA_DISCOUNT_PER_SERVICE = 0.15;

    private final int id;
    private final Date initialDate;
    private Date finalDate;
    private TicketState state;
    private final List<AbstractProductDTO> products;
    private TicketType ticketType;

    public enum TicketState {EMPTY, OPEN, CLOSED}
    public enum TicketType {ONLY_PRODUCTS, ONLY_SERVICES, SERVICES_AND_PRODUCTS}

    /**
     * Crea un ticket con todos sus atributos inicializados.
     *
     * @param id identificador del ticket
     * @param initialDate fecha de creación
     * @param finalDate fecha de cierre
     * @param state estado inicial del ticket
     * @param products lista inicial de productos
     * @param ticketType tipo del ticket
     */
    protected AbstractTicketDTO(int id, Date initialDate, Date finalDate, TicketState state,
                                List<AbstractProductDTO> products, TicketType ticketType) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.state = state;
        this.products = products;
        this.ticketType = ticketType;
    }

    /**
     * Crea un ticket vacío con tipo definido.
     *
     * @param id identificador del ticket
     * @param initialDate fecha de creación
     * @param finalDate fecha de cierre
     * @param ticketType tipo del ticket
     */
    protected AbstractTicketDTO(int id, Date initialDate, Date finalDate, TicketType ticketType) {
        this(id, initialDate, finalDate, TicketState.EMPTY, new ArrayList<>(), ticketType);
    }


    /**
     * Devuelve el identificador del ticket.
     *
     * @return identificador del ticket
     */
    public int getId() { return id; }

    /**
     * Devuelve el estado actual del ticket.
     *
     * @return estado del ticket
     */
    public TicketState getState() { return state; }

    /**
     * Devuelve el tipo del ticket.
     *
     * @return tipo del ticket
     */
    public TicketType getTicketType() { return ticketType; }

    /**
     * Devuelve la fecha de creación del ticket.
     *
     * @return fecha inicial del ticket
     */
    public Date getInitialDate() { return initialDate; }

    /**
     * Devuelve la fecha de cierre del ticket.
     *
     * @return fecha final del ticket
     */
    public Date getFinalDate() { return finalDate; }

    /**
     * Devuelve una copia de la lista de productos del ticket.
     *
     * @return lista de productos
     */
    public List<AbstractProductDTO> getProducts() { return new ArrayList<>(products); }

    /**
     * Indica si un producto es un servicio.
     *
     * @param p producto a comprobar
     * @return true si el producto es un servicio
     */
    private boolean isService(AbstractProductDTO p) { return p instanceof ServiceDTO; }

    /**
     * Indica si un producto es un producto no servicio.
     *
     * @param p producto a comprobar
     * @return true si el producto no es un servicio
     */
    private boolean isProduct(AbstractProductDTO p) { return !isService(p); }

    /**
     * Cuenta el número total de unidades por categoría.
     *
     * @return mapa con categorías y número de unidades
     */
    private Map<Category, Integer> countCategories() {
        Map<Category, Integer> counts = new HashMap<>();

        for (AbstractProductDTO p : products) {
            if (p instanceof Categorizable c) {
                Category category = c.getCategory();
                counts.put(category, counts.getOrDefault(category, 0) + p.getAmount());
            }
        }

        return counts;
    }


    /**
     * Calcula el descuento por unidad de un producto según su categoría.
     *
     * @param product producto evaluado
     * @param counts mapa de categorías y unidades
     * @return descuento por unidad
     */
    private double perItemDiscount(AbstractProductDTO product, Map<Category, Integer> counts) {
        if (product instanceof ProductDTO p) {
            ProductCategoryDTO category = p.getCategory();
            int units = counts.getOrDefault(category, 0);
            if (units >= 2) return product.getPrice() * category.getDiscount();
        }
        return 0.0;
    }

    /**
     * Devuelve el número total de unidades del ticket.
     *
     * @return número total de unidades
     */
    private int totalUnits() {
        return products.stream().mapToInt(AbstractProductDTO::getAmount).sum();
    }

    /**
     * Añade un producto al ticket aplicando las reglas de negocio.
     *
     * @param product producto a añadir
     * @param quantity cantidad a añadir
     * @return true si el producto se añade correctamente
     */
    public boolean addProduct(AbstractProductDTO product, int quantity) {
        if (product == null || quantity <= 0) return false;
        if (product instanceof Validable v && !v.isValid()) return false;
        if (quantity > MAX_PRODUCTS - totalUnits()) return false;

        if (product instanceof PersonalizableDTO p) {
            boolean hasPersonalization = Arrays.stream(p.getLines())
                    .anyMatch(l -> l != null && !l.isEmpty());

            if (hasPersonalization) {
                product.addAmount(quantity);
                products.add(product);

                long linesCount = Arrays.stream(p.getLines())
                        .filter(l -> l != null && !l.isEmpty())
                        .count();
                product.setPrice(product.getPrice() * (1 + 0.10 * linesCount));

                state = TicketState.OPEN;
                return true;
            }
        }

        int idx = products.indexOf(product);
        if (idx >= 0) {
            products.get(idx).addAmount(quantity);
        } else {
            product.addAmount(quantity);
            products.add(product);
        }

        state = TicketState.OPEN;
        return true;
    }

    /**
     * Elimina un producto del ticket.
     *
     * @param product producto a eliminar
     */
    public void removeProduct(AbstractProductDTO product) { products.remove(product); }

    /**
     * Cierra el ticket y establece la fecha de cierre.
     */
    public void closeTicket() { if (state != TicketState.CLOSED) { finalDate = new Date(); state = TicketState.CLOSED; } }

    /**
     * Comprueba que todos los servicios del ticket sean válidos.
     *
     * @return true si todos los servicios son válidos
     */
    public boolean areAllServiceProductsValid() {
        return products.stream()
                .filter(p -> p instanceof Validable)
                .map(p -> (Validable) p)
                .allMatch(Validable::isValid);
    }


    /**
     * Calcula el precio total de los productos no servicio.
     *
     * @return precio total de productos
     */
    public double totalProductsPrice() {
        return products.stream()
                .filter(this::isProduct)
                .mapToDouble(p -> p.getPrice() * p.getAmount())
                .sum();
    }

    /**
     * Calcula el descuento base por categorías de productos.
     *
     * @return descuento base
     */
    public double baseProductDiscount() {
        Map<Category, Integer> counts = countCategories();
        return round2(products.stream()
                .filter(this::isProduct)
                .mapToDouble(p -> perItemDiscount(p, counts) * p.getAmount())
                .sum());
    }


    /**
     * Devuelve el número total de servicios del ticket.
     *
     * @return número de servicios
     */
    public int countServices() {
        return products.stream()
                .filter(this::isService)
                .mapToInt(AbstractProductDTO::getAmount)
                .sum();
    }


    /**
     * Indica si el ticket contiene solo servicios.
     *
     * @return true si el ticket solo contiene servicios
     */
    public boolean isOnlyServicesTicket() { return products.stream().allMatch(this::isService); }


    /**
     * Indica si el ticket contiene servicios y productos.
     *
     * @return true si el ticket es combinado
     */
    public boolean isCombinedTicket() { return products.stream().anyMatch(this::isService) && products.stream().anyMatch(this::isProduct); }


    /**
     * Calcula el descuento adicional por servicios en tickets combinados.
     *
     * @return descuento adicional por servicios
     */
    public double extraServiceDiscount() {
        if (!isCombinedTicket()) return 0.0;
        return round2(totalProductsPrice() * EXTRA_DISCOUNT_PER_SERVICE * countServices());
    }

    /**
     * Calcula el descuento total aplicado al ticket.
     *
     * @return descuento total
     */
    public double totalDiscount() { return isOnlyServicesTicket() ? 0.0 : round2(baseProductDiscount() + extraServiceDiscount()); }

    /**
     * Calcula el precio final del ticket tras aplicar descuentos.
     *
     * @return precio final
     */
    public double finalPrice() { return isOnlyServicesTicket() ? 0.0 : round2(totalProductsPrice() - totalDiscount()); }

    /**
     * Devuelve el identificador formateado del ticket.
     *
     * @return identificador formateado
     */
    public String getFormattedId() {
        StringBuilder sb = new StringBuilder();
        if (state == TicketState.EMPTY) sb.append(DATE_FORMAT.format(initialDate)).append("-");
        sb.append(id);
        if (state == TicketState.CLOSED && finalDate != null)
            sb.append("-").append(DATE_FORMAT.format(finalDate));
        return sb.toString();
    }

    /**
     * Devuelve la impresión del ticket según la implementación concreta.
     *
     * @return representación del ticket
     */
    abstract public String printTicket();

    /**
     * Devuelve la impresión del ticket usando un impresor externo.
     *
     * @param ticketPrinter impresor del ticket
     * @return representación del ticket
     */
    public String printTicket(TicketPrinter ticketPrinter) {
        return ticketPrinter.print(this);
    }

    /**
     * Redondea un valor decimal a dos cifras decimales.
     *
     * @param v valor a redondear
     * @return valor redondeado
     */
    public static double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    /**
     * Devuelve la representación textual básica del ticket.
     *
     * @return identificador del ticket
     */
    @Override
    public String toString() { return getFormattedId(); }

    /**
     * Compara dos tickets basándose en su identificador.
     *
     * @param o objeto a comparar
     * @return true si los tickets tienen el mismo identificador
     */
    @Override
    public boolean equals(Object o) { return (o instanceof AbstractTicketDTO t) && t.id == id; }
}
