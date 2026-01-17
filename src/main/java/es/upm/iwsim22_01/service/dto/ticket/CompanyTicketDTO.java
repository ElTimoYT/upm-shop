package es.upm.iwsim22_01.service.dto.ticket;

import es.upm.iwsim22_01.service.dto.product.AbstractProductDTO;
import es.upm.iwsim22_01.service.dto.product.AbstractTypeDTO;
import es.upm.iwsim22_01.service.dto.product.ProductService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyTicketDTO extends TicketDTO {


    private static final double EXTRA_DISCOUNT_PER_SERVICE = 0.15;

    public CompanyTicketDTO(int id, Date initialDate, Date finalDate, TicketState state, List<AbstractProductDTO> products) {
        super(id, initialDate, finalDate, state, products, TicketType.COMPANY);
    }

    public CompanyTicketDTO(int id) {
        super(id, new Date(), null, TicketState.EMPTY, new ArrayList<>(), TicketType.COMPANY);
    }

    private boolean isService(AbstractProductDTO p) {
        return p instanceof AbstractTypeDTO;
    }

    private boolean isProduct(AbstractProductDTO p) {
        return !isService(p);
    }

    public int countServices() {
        int n = 0;
        for (AbstractProductDTO p : super.getProducts()) {
            if (isService(p)) n += p.getAmount();
        }
        return n;
    }

    public boolean hasAnyService() {
        for (AbstractProductDTO p : super.getProducts()) {
            if (isService(p) && p.getAmount() > 0) return true;
        }
        return false;
    }

    public boolean hasAnyProduct() {
        for (AbstractProductDTO p : super.getProducts()) {
            if (isProduct(p) && p.getAmount() > 0) return true;
        }
        return false;
    }

    public boolean isOnlyServicesTicket() {
        return hasAnyService() && !hasAnyProduct();
    }

    public boolean isCombinedTicket() {
        return hasAnyService() && hasAnyProduct();
    }

    /**
     * Para tickets de empresa:
     * - Puedes añadir servicios y/o productos (el comando -s/-c de E3 decide el tipo final).
     * - La validación fuerte se aplica al cierre: si es combinado, debe haber al menos 1 y 1.
     */
    public boolean addService(AbstractProductDTO service) {
        return this.addProduct(service, 1);
    }

    @Override
    public boolean addProduct(AbstractProductDTO product, int amount) {
        return super.addProduct(product, amount);
    }

    /**
     * Total “facturable” del ticket (E3):
     * - Los servicios no tienen precio al imprimir.
     * - Solo sumamos precio de PRODUCTOS.
     */
    public double totalProductsPrice() {
        double total = 0.0;
        for (AbstractProductDTO p : super.getProducts()) {
            if (isProduct(p)) {
                total += p.getAmount() * p.getPrice();
            }
        }
        return total;
    }

    /**
     * Descuento base SOLO sobre productos.
     * Reutilizamos la lógica de TicketDTO, pero filtrando servicios.
     */
    public double baseProductDiscount() {
        var counts = super.countCategory();
        double discount = 0.0;

        for (AbstractProductDTO p : super.getProducts()) {
            if (isProduct(p)) {
                double perItem = super.perItemDiscount(p, counts);
                discount += perItem * p.getAmount();
            }
        }
        return round1(discount);
    }

    /**
     * Descuento extra E3 (solo si es ticket combinado):
     * “plus de 15% de descuento en los productos por cada servicio contratado”.
     */
    public double extraServiceDiscount() {
        if (!isCombinedTicket()) return 0.0;

        int services = countServices();
        if (services <= 0) return 0.0;

        double extraRate = EXTRA_DISCOUNT_PER_SERVICE * services;
        double extra = totalProductsPrice() * extraRate;

        return round1(extra);
    }

    public double totalDiscount() {
        if (isOnlyServicesTicket()) return 0.0;
        return round1(baseProductDiscount() + extraServiceDiscount());
    }

    public double finalPrice() {
        if (isOnlyServicesTicket()) return 0.0;
        return round1(totalProductsPrice() - totalDiscount());
    }

    @Override
    public void closeTicket() {
        if (hasAnyProduct() ^ hasAnyService()) {
            super.closeTicket();
            return;
        }
        if (isCombinedTicket()) {
            super.closeTicket();
        }
    }
}
