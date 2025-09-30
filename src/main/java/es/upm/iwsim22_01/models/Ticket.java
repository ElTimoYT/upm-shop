package es.upm.iwsim22_01.models;

import java.util.ArrayList;

public class Ticket {
    private ArrayList<Product> products;

    public Ticket() {
        this.products = new ArrayList<>();
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
        int[] contador = new int[Category.values().length];

        for (Product product : products) {
            int pos  = product.getCategory().ordinal();
            contador[pos]++;
        }

        for (int i = 0; i < Category.values().length; i++) {
            if(contador[i] >= 2){
                Category category = Category.values()[i];
                double percentage = category.getDiscount();
                for (Product p : products) {
                    if (p.getCategory() == category) {
                        discount += (double) (p.getPrice() * percentage);
                    }
                }

            }

        }
        return discount;
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
       return quantity == remainingSpaceT;
    }

    public boolean removeProductById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        double totalPrice = TotalPrice();
        double discountPrice = DiscountPrice();
        for (Product product : products) {
            str.append(product.toString());
            str.append("\n");
        }
        str.append("Total Price: " + totalPrice);
        str.append("\n");
        str.append("Total Discount: " + discountPrice);
        str.append("\n");
        str.append("Final price: " + (TotalPrice() - discountPrice));
        return str.toString();
    }





}
