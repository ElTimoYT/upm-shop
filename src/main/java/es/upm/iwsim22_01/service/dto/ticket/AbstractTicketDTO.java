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

    protected AbstractTicketDTO(int id, Date initialDate, Date finalDate, TicketState state,
                                List<AbstractProductDTO> products, TicketType ticketType) {
        this.id = id;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.state = state;
        this.products = products;
        this.ticketType = ticketType;
    }

    protected AbstractTicketDTO(int id, Date initialDate, Date finalDate, TicketType ticketType) {
        this(id, initialDate, finalDate, TicketState.EMPTY, new ArrayList<>(), ticketType);
    }

    public int getId() { return id; }

    public TicketState getState() { return state; }

    public TicketType getTicketType() { return ticketType; }

    public Date getInitialDate() { return initialDate; }

    public Date getFinalDate() { return finalDate; }

    public List<AbstractProductDTO> getProducts() { return new ArrayList<>(products); }

    public static double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    private boolean isService(AbstractProductDTO p) { return p instanceof ServiceDTO; }

    private boolean isProduct(AbstractProductDTO p) { return !isService(p); }

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

    private double perItemDiscount(AbstractProductDTO product, Map<Category, Integer> counts) {
        if (product instanceof ProductDTO p) {
            ProductCategoryDTO category = p.getCategory();
            int units = counts.getOrDefault(category, 0);
            if (units >= 2) return product.getPrice() * category.getDiscount();
        }
        return 0.0;
    }

    private int totalUnits() {
        return products.stream().mapToInt(AbstractProductDTO::getAmount).sum();
    }

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

    public void removeProduct(AbstractProductDTO product) { products.remove(product); }

    public void closeTicket() { if (state != TicketState.CLOSED) { finalDate = new Date(); state = TicketState.CLOSED; } }

    public boolean areAllServiceProductsValid() {
        return products.stream()
                .filter(p -> p instanceof Validable)
                .map(p -> (Validable) p)
                .allMatch(Validable::isValid);
    }

    public double totalProductsPrice() {
        return products.stream()
                .filter(this::isProduct)
                .mapToDouble(p -> p.getPrice() * p.getAmount())
                .sum();
    }

    public double baseProductDiscount() {
        Map<Category, Integer> counts = countCategories();
        return round2(products.stream()
                .filter(this::isProduct)
                .mapToDouble(p -> perItemDiscount(p, counts) * p.getAmount())
                .sum());
    }

    public int countServices() {
        return products.stream()
                .filter(this::isService)
                .mapToInt(AbstractProductDTO::getAmount)
                .sum();
    }

    public boolean isOnlyServicesTicket() { return products.stream().allMatch(this::isService); }

    public boolean isCombinedTicket() { return products.stream().anyMatch(this::isService) && products.stream().anyMatch(this::isProduct); }

    public double extraServiceDiscount() {
        if (!isCombinedTicket()) return 0.0;
        return round2(totalProductsPrice() * EXTRA_DISCOUNT_PER_SERVICE * countServices());
    }

    public double totalDiscount() { return isOnlyServicesTicket() ? 0.0 : round2(baseProductDiscount() + extraServiceDiscount()); }

    public double finalPrice() { return isOnlyServicesTicket() ? 0.0 : round2(totalProductsPrice() - totalDiscount()); }

    public String getFormattedId() {
        StringBuilder sb = new StringBuilder();
        if (state == TicketState.EMPTY) sb.append(DATE_FORMAT.format(initialDate)).append("-");
        sb.append(id);
        if (state == TicketState.CLOSED && finalDate != null)
            sb.append("-").append(DATE_FORMAT.format(finalDate));
        return sb.toString();
    }

    abstract public String printTicket();

    public String printTicket(TicketPrinter ticketPrinter) {
        return ticketPrinter.print(this);
    }

    @Override
    public String toString() { return getFormattedId(); }

    @Override
    public boolean equals(Object o) { return (o instanceof AbstractTicketDTO t) && t.id == id; }
}
