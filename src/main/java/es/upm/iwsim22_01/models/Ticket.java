package es.upm.iwsim22_01.models;

import java.util.ArrayList;

public class Ticket {
    private ArrayList <Product> products;

    public Ticket() {
        products = new ArrayList();
    }

    public int TotalPrice() {
        int total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public int DiscountPrice() {
        int discount = 0;
        for (Product product : products) {

        }
        return discount;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int totalPrice = TotalPrice();
        int discountPrice = DiscountPrice();
        for (Product product : products) {
            str.append(product.toString());
            str.append("\n");
        }
        str.append("Total Price: " + totalPrice);
        str.append("\n");
        str.append("Total Discount: " + discountPrice);
        str.append("\n");
        str.append("Final price: " + TotalPrice() + discountPrice);
        return str.toString();
    }

    public ArrayList <Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList <Product> products) {
        this.products = products;
    }

    public void addProduct(Product product, int quantity) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        removeProduct(product);
    }
}
