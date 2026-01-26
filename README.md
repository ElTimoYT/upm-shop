
---

# UPM-Shop

## Brief description

UPM-Shop is a console-based Java application that simulates a point-of-sale (POS) system to manage cashiers, clients, products and tickets. It is designed as a learning and demonstration project with a layered architecture (commands, services, repositories, DTOs) and file-based JSON persistence.

## Project objective

Provide a compact but realistic application to practice and demonstrate software design concepts: domain modeling, design patterns, layered architecture, persistence, unit testing and a command-line interface.

## Key features

- CRUD operations for cashiers, clients and products.
- Creation, listing, printing and removal of tickets.
- Support for different product/service types and category-based discounts.
- Pluggable ticket printers (strategy pattern) and date adapters.
- Input validation and business rules implemented in the service layer.

## Architecture and design patterns

The application follows a layered structure and uses several design patterns:

- Command: CLI commands are handled by dedicated handlers.
- Service: business logic is encapsulated in service classes.
- Repository: JSON file-based repositories for persistence.
- DTO / Validable: data transfer objects and validation for input.
- Strategy: interchangeable ticket printers.
- Adapter: date serialization/adaptation.

## Main technologies

- Java 21+ (Maven)
- JUnit for tests
- JSON files for persistence (folder: data/)

## Project structure (high level)

- src/main/java/es/upm/iwsim22_01/commands: CLI commands and handlers.
- src/main/java/es/upm/iwsim22_01/data/models: domain entities (Cashier, Client, Product, Ticket).
- src/main/java/es/upm/iwsim22_01/data/repository: file-based repositories.
- src/main/java/es/upm/iwsim22_01/service: services, DTOs and ticket printers.
- data/: storage for JSON files (products, tickets, users).

## Important business rules

- Cashiers are registered only with a name and a corporate email.
- A cashier cannot be a registered application user at the same time. If they want to use the application, they must register separately with a personal email.
- Service-layer validations enforce these and other rules; failures throw controlled exceptions.

## CLI commands (summary)

- cash add [<id>] "<name>" <email>
  - Adds a new cashier. If id is provided, an attempt is made to use it. Only name and corporate email are stored.

- cash remove <id>
  - Removes the cashier with the given id.

- cash list
  - Lists cashiers ordered by name. Tickets are not displayed.

- cash tickets <id>
  - Shows the tickets associated with the cashier ordered by ticket id. Only ticket ID and status are shown.

- client add|remove|list ...
- prod add|remove|list|update ...
- ticket new|add|print|list|remove ...

See command handlers in src/main/java/.../commands/handlers/ for parameter and validation details.

## Services overview

- ProductService: product creation, listing, category handling and discounts.
- TicketService: ticket creation, status management, and totals calculation.
- CashierService and ClientService: specific operations and registration rules for actors.
- TicketPrinter implementations: ProductTicketPrinter, ServiceTicketPrinter, CombinedTicketPrinter.

## Exception handling and verification

- Business exceptions are raised by services when operations cannot be completed (validation errors, missing resources, business rule violations).
- To verify correct behavior:
  - Run invalid operations via the CLI and confirm readable error messages.
  - Inspect JSON files in the data/ folder to check persistent state after operations.
  - Run unit tests (mvn test) to validate critical paths: validation, repository operations and services.

## Build and run

Build the project with Maven and run the JAR:

mvn clean package
java -jar target/upm-shop-1.0-SNAPSHOT.jar

Run unit tests:

mvn test

## Notes for developers

- Extending the application is straightforward: add new printers (implement TicketPrinter), new repositories (e.g., database-backed), or new commands under commands/handlers.
- Respect the cashier registration rule to avoid inconsistencies between users and cashiers.

## Contributing

1. Fork the repository and create a branch feature/your-change.
2. Add unit tests covering your changes.
3. Open a pull request describing the motivation and changes.

## Authors and credits

Project created as a learning exercise. Developed by the project author together with a team of 3 developers.

---
Enjoy shopping with UPM SHOP! 🛍️
