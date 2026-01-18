    package es.upm.iwsim22_01.service.service;

    import es.upm.iwsim22_01.data.models.*;
    import es.upm.iwsim22_01.data.repository.ProductRepository;
    import es.upm.iwsim22_01.service.dto.product.*;
    import es.upm.iwsim22_01.service.dto.product.category.ProductCategoryDTO;
    import es.upm.iwsim22_01.service.dto.product.category.ServiceCategoryDTO;
    import es.upm.iwsim22_01.service.dto.product.service.ServiceDTO;

    import java.time.LocalDateTime;

    /**
     * Gestor de productos, encargado de la creación, validación y registro de instancias de {@link AbstractProductDTO}.
     * Permite añadir diferentes tipos de productos al sistema, validando sus parámetros y restricciones.
     */
    public class ProductService extends AbstractService<Product, AbstractProductDTO, String> {
        private final static int MAX_PRODUCTS = 200, MAX_NAME_LENGTH = 100, PRODUCT_RANDOM_ID_LENGTH = 3;

        private final ProductRepository productRepository;

        public ProductService() {
            super(new ProductRepository());
            this.productRepository = (ProductRepository) super.repository;
        }

        @Override
        protected AbstractProductDTO toDto(Product model) {
            return switch (model.getType()) {
                case UNIT_PRODUCT -> new ProductDTO(model.getId(), model.getName(), ProductCategoryDTO.valueOf(model.getCategory()), model.getPrice(), model.getAmount());
                case PERSONALIZABLE_PRODUCT -> new PersonalizableDTO(model.getId(), model.getName(), ProductCategoryDTO.valueOf(model.getCategory()), model.getPrice(), model.getAmount(), model.getLines());
                case CATERING -> new FoodDTO(model.getId(), model.getName(), model.getPrice(), model.getAmount(), model.getMaxParticipant(), model.getExpirationDate(), model.getParticipantsAmount());
                case MEETING -> new MeetingDTO(model.getId(), model.getName(), model.getPrice(), model.getAmount(), model.getMaxParticipant(), model.getExpirationDate(), model.getParticipantsAmount());
                case SERVICE -> new ServiceDTO(model.getId(), model.getAmount(), model.getExpirationDate().toLocalDate(), ServiceCategoryDTO.valueOf(model.getCategory()));
            };
        }

        @Override
        protected Product toModel(AbstractProductDTO dto) {
            return switch (dto) {
                case PersonalizableDTO personalizableDTO -> Product.createPersonalizable(personalizableDTO.getId(), personalizableDTO.getName(), personalizableDTO.getCategory().toString(), personalizableDTO.getPrice(), personalizableDTO.getAmount(), personalizableDTO.getLines());
                case ProductDTO productDTO -> Product.createUnit(productDTO.getId(), productDTO.getName(), productDTO.getCategory().toString(), productDTO.getPrice(), productDTO.getAmount());
                case FoodDTO foodDTO -> Product.createCatering(foodDTO.getId(), foodDTO.getName(), foodDTO.getPrice(), foodDTO.getAmount(), foodDTO.getMaxParticipant(), foodDTO.getExpirationDate(), foodDTO.getParticipantsAmount());
                case MeetingDTO meetingDTO -> Product.createMeeting(meetingDTO.getId(), meetingDTO.getName(), meetingDTO.getPrice(), meetingDTO.getAmount(), meetingDTO.getMaxParticipant(), meetingDTO.getExpirationDate(), meetingDTO.getParticipantsAmount());
                case ServiceDTO serviceDTO -> Product.createService(serviceDTO.getId(), serviceDTO.getAmount(), serviceDTO.getCategory().toString(), serviceDTO.getExpirationDate().atStartOfDay());
                default -> throw new IllegalStateException("Unexpected product type: " + dto);
            };
        }

        /**
         * Añade un producto unitario al sistema.
         *
         * @param id       Identificador único del producto.
         * @param name     Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
         * @param category Categoría del producto.
         * @param price    Precio del producto (debe ser mayor que 0).
         * @return La instancia de {@link AbstractProductDTO} creada.
         * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido o el producto ya existe.
         * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
         */
        public AbstractProductDTO addProduct(int id, String name, ProductCategoryDTO category, double price) {
            if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");

            return add(new ProductDTO(String.valueOf(id), name, category, price));
        }

        public AbstractProductDTO addProduct(String name, ProductCategoryDTO category, double price) {
            return addProduct(createNewId(), name, category, price);
        }

        /**
         * Añade un producto personalizable al sistema.
         *
         * @param id           Identificador único del producto.
         * @param name         Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
         * @param category     Categoría del producto.
         * @param price        Precio del producto (debe ser mayor que 0).
         * @param maxText      Número máximo de caracteres personalizables (debe ser mayor o igual a 1).
         * @return La instancia de {@link AbstractProductDTO} creada.
         * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido, el producto ya existe o maxText es menor que 1.
         * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
         */
        public AbstractProductDTO addPersonalizable(int id, String name, ProductCategoryDTO category, double price, int maxText){
            if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (maxText < 1) throw new IllegalArgumentException("Max pers " + maxText + " cannot be less than 1.");

            return add(new PersonalizableDTO(String.valueOf(id), name, category, price, maxText));
        }

        public AbstractProductDTO addPersonalizable(String name, ProductCategoryDTO category, double price, int maxText) {
            return addPersonalizable(createNewId(), name, category, price, maxText);
        }

        /**
         * Añade un producto de catering al sistema.
         *
         * @param id               Identificador único del producto.
         * @param name             Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
         * @param price            Precio del producto (debe ser mayor que 0).
         * @param expirationDate   Fecha de caducidad del producto (no puede ser nula).
         * @param maxParticipants   Número máximo de participantes (debe ser mayor o igual a 1).
         * @return La instancia de {@link AbstractProductDTO} creada.
         * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido, la fecha de caducidad es nula, maxParticipants es menor que 1 o el producto ya existe.
         * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
         */
        public AbstractProductDTO addFood(int id, String name, double price, LocalDateTime expirationDate, int maxParticipants){
            if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
            if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");

            return add(new FoodDTO(String.valueOf(id), name, price, maxParticipants, expirationDate, 0));
        }

        public AbstractProductDTO addFood(String name, double price, LocalDateTime expirationDate, int maxParticipants){
            return addFood(name, price, expirationDate, maxParticipants);
        }

        /**
         * Añade un producto de reunión al sistema.
         *
         * @param id               Identificador único del producto.
         * @param name             Nombre del producto (no puede ser nulo, vacío o superar {@value #MAX_NAME_LENGTH} caracteres).
         * @param pricePerPerson   Precio por persona (debe ser mayor que 0).
         * @param expirationDate   Fecha de caducidad del producto (no puede ser nula).
         * @param maxParticipants   Número máximo de participantes (debe ser mayor o igual a 1).
         * @return La instancia de {@link AbstractProductDTO} creada.
         * @throws IllegalArgumentException Si el precio es negativo, el nombre no es válido, la fecha de caducidad es nula, maxParticipants es menor que 1 o el producto ya existe.
         * @throws RuntimeException Si se ha alcanzado el número máximo de productos permitidos.
         */
        public AbstractProductDTO addMeeting(int id, String name, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants){
            if (!isPriceValid(pricePerPerson)) throw new IllegalArgumentException("Product price " + pricePerPerson + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
            if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");

            return add(new MeetingDTO(String.valueOf(id), name, pricePerPerson, maxParticipants, expirationDate, 0));
        }

        public AbstractProductDTO addMeeting(String name, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants){
            return addMeeting(name, pricePerPerson, expirationDate, maxParticipants);
        }

        public AbstractProductDTO addService(ServiceCategoryDTO category, LocalDateTime expirationDate) {
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");

            return add(new ServiceDTO(productRepository.nextAutoincrementId() + "S", 0, expirationDate.toLocalDate(), category));
        }

        /**
         * Valida el nombre de un producto.
         *
         * @param name Nombre del producto a validar.
         * @return {@code true} si el nombre es válido (no nulo, no vacío y no supera {@value #MAX_NAME_LENGTH} caracteres).
         */
        public boolean isNameValid(String name) {
            return name != null && !name.isBlank() && name.length() < MAX_NAME_LENGTH;
        }

        /**
         * Valida el precio de un producto.
         *
         * @param price Precio del producto a validar.
         * @return {@code true} si el precio es mayor que 0.
         */
        public boolean isPriceValid(double price) {
            return price > 0;
        }

        /**
         * Comprueba si se ha alcanzado el número máximo de productos permitidos.
         *
         * @return {@code true} si el número de productos es mayor o igual a {@value #MAX_PRODUCTS}.
         */
        public boolean isProductListFull() {
            return getSize() >= ProductService.MAX_PRODUCTS;
        }

        public int createNewId() {
            int id;

            do {
                id = (int) (Math.random() * Math.pow(10, PRODUCT_RANDOM_ID_LENGTH));
            } while (existsId(String.valueOf(id)));

            return id;
        }
}
