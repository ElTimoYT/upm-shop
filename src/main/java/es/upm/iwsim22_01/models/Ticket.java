package es.upm.iwsim22_01.models;

import es.upm.iwsim22_01.manager.ProductManager;

import java.util.ArrayList;
import java.util.Map;

public class Ticket {
    private Map<Integer, Product> products;
    private ProductManager productManager;

    public Ticket(ProductManager productManager) {
        this.products = productManager;
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

    public boolean addProduct(int id, int quantity) {
        ArrayList newProducts = pro



        products.add(product);
    }

    public boolean removeProduct(Product product) {
        removeProduct(product);
    }
}
