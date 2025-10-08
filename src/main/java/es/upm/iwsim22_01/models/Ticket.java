package es.upm.iwsim22_01.models;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Ticket {
    private ArrayList<Product> products;

    public Ticket() {
        this.products = new ArrayList<>();
    }
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

    private double perItemDiscount(Product p, Map<Category, Integer> counts) {
        int n = counts.getOrDefault(p.getCategory(), 0);
        double resultDiscount = 0.0;
        if (n >= 2) {
            double discount = p.getCategory().getDiscount();
            resultDiscount= p.getPrice() * discount;
        }
        return resultDiscount;
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0; // si quieres un decimal (ej: -3.0)
    }

    public double TotalPrice() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public double DiscountPrice() {
        double discount = 0;
        Map<Category, Integer> counts = countCategory();
        for (Product p : products) {
            discount += perItemDiscount(p, counts);
        }
        return round1(discount);
    }

    public boolean addProduct(Product product, int quantity) {
        if (products.size() >= 100 || product == null || quantity <= 0) {
            return false;
        }
        int remainingSpaceT = 100 - products.size();
        int Insert = Math.min(quantity, remainingSpaceT);
        for (int i = 0; i < Insert; i++) {
            products.add(product);
        }
       return quantity != remainingSpaceT;
    }

    public boolean removeProductById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Map<Category, Integer> counts = countCategory();
        double totalPrice = TotalPrice();
        double discountPrice = DiscountPrice();
        for (Product p : products) {
            str.append(p.toString());
            double d = perItemDiscount(p, counts);
            if (d > 0) {
                str.append(" **discount -").append(round1(d));
            }
            str.append("\n");
        }
        str.append("Total Price: ").append(totalPrice);
        str.append("\n");
        str.append("Total Discount: ").append(discountPrice);
        str.append("\n");
        str.append("Final price: ").append(TotalPrice() - discountPrice);
        return str.toString();
    }





}
