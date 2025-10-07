# 🛒 UPM SHOP - Product & Ticket CLI

Welcome to **UPM SHOP**, your simple and fast command-line tool to manage products and create tickets with automatic discounts! 🚀

## ✨ What can you do?
- **Add, list, update, and remove products** from your in-memory catalog.
- **Create a ticket (cart)**, add or remove products, and see your total with discounts applied.
- **Enjoy automatic discounts** by category when you buy 2 or more items from the same category.
- **All in-memory**: No installation, no database, just run and go!

## 🏷️ Main Concepts
- **Product**: Each has an id, name, category, and price.
- **Category discounts** (if you add 2 or more items from the same category):
  - 🛍️ MERCH: 0%
  - ✏️ STATIONARY: 5%
  - 👕 CLOTHES: 7%
  - 📚 BOOK: 10%
  - 💻 ELECTRONICS: 3%
- **Ticket**: Your shopping cart, up to 100 items.
- **Catalog**: Store up to 200 products.

## ⚡ Quick Commands
```
prod add <id> "<name>" <category> <price>
prod list
prod update <id> name|category|price <value>
prod remove <id>
ticket new
ticket add <prodId> <quantity>
ticket remove <prodId>
ticket print
help
exit
```

## 🚀 Example Session
```
prod add 1 "Blue T-Shirt" clothes 15.90
prod add 2 "Sci-Fi Book" book 12.50
ticket new
ticket add 1 2
ticket add 2 2
ticket print
```

## 🖥️ How to Run
1. Build with Maven:
   ```
   mvn clean package
   ```
2. Run the app:
   ```
   java -jar target/<artifact>.jar
   ```

## 🧾 What you'll see (ticket print)
- Each product in your ticket
- Total price
- Total discount
- Final price to pay

## ℹ️ Good to Know
- Product IDs must be unique.
- Price must be greater than 0.
- Product names can't be empty.
- All data is in memory (no persistence).
- Ticket stores repeated products (no quantity aggregation).

## 💡 Ideas for the Future
- Use `BigDecimal` for prices.
- Save your catalog and tickets (JSON or database).
- Aggregate product quantities in the ticket.
- Customizable discounts.

---
Enjoy shopping with UPM SHOP! 🛍️
