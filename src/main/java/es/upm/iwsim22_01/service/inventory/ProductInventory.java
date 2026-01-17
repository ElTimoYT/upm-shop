    package es.upm.iwsim22_01.service.inventory;

    import es.upm.iwsim22_01.data.models.*;
    import es.upm.iwsim22_01.data.repository.ProductRepository;
    import es.upm.iwsim22_01.service.dto.product.*;

    import java.time.LocalDateTime;
    import java.util.LinkedHashMap;
    import java.util.Map;


    /**
     * Gestor de productos, encargado de la creación, validación y registro de instancias de {@link AbstractProductDTO}.
     * Permite añadir diferentes tipos de productos al sistema, validando sus parámetros y restricciones.
     */
    public class ProductInventory extends AbstractInventory<Product, AbstractProductDTO, Integer> {
        private final static int MAX_PRODUCTS = 200, MAX_NAME_LENGTH = 100;

        private final ProductRepository productRepository;

        public ProductInventory() {
            super(new ProductRepository());
            this.productRepository = (ProductRepository) super.repository;
        }

        @Override
        protected AbstractProductDTO toDto(Product model) {
            return switch (model.getType()) {
                case UNIT_PRODUCT -> new UnitProductDTO(model.getId(), model.getName(), CategoryDTO.valueOf(model.getCategory()), model.getPrice(), model.getAmount());
                case PERSONALIZABLE_PRODUCT -> new PersonalizableProductDTO(model.getId(), model.getName(), CategoryDTO.valueOf(model.getCategory()), model.getPrice(), model.getAmount(), model.getLines());
                case CATERING -> new CateringDTO(model.getId(), model.getName(), model.getPrice(), model.getAmount(), model.getMaxParticipant(), model.getExpirationDate(), model.getParticipantsAmount());
                case MEETING -> new MeetingDTO(model.getId(), model.getName(), model.getPrice(), model.getAmount(), model.getMaxParticipant(), model.getExpirationDate(), model.getParticipantsAmount());
                case SERVICE -> new ProductService(model.getId(), model.getExpirationDate(), CategoryDTO.valueOf(model.getCategory()));
            };
        }

        @Override
        protected Product toModel(AbstractProductDTO dto) {
            return switch (dto) {
                case PersonalizableProductDTO personalizableProductDTO -> Product.createPersonalizable(personalizableProductDTO.getId(), personalizableProductDTO.getName(), personalizableProductDTO.getCategory().toString(), personalizableProductDTO.getPrice(), personalizableProductDTO.getAmount(), personalizableProductDTO.getLines());
                case UnitProductDTO unitProductDTO -> Product.createUnit(unitProductDTO.getId(), unitProductDTO.getName(), unitProductDTO.getCategory().toString(), unitProductDTO.getPrice(), unitProductDTO.getAmount());
                case CateringDTO cateringDTO -> Product.createCatering(cateringDTO.getId(), cateringDTO.getName(), cateringDTO.getPrice(), cateringDTO.getAmount(), cateringDTO.getMaxParticipant(), cateringDTO.getExpirationDate(), cateringDTO.getParticipantsAmount());
                case MeetingDTO meetingDTO -> Product.createMeeting(meetingDTO.getId(), meetingDTO.getName(), meetingDTO.getPrice(), meetingDTO.getAmount(), meetingDTO.getMaxParticipant(), meetingDTO.getExpirationDate(), meetingDTO.getParticipantsAmount());
                case ProductService productService -> Product.createService(productService.getId(), productService.getExpirationDate(), String.valueOf(productService.getCategory()));
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
        public AbstractProductDTO addUnitProduct(int id, String name, CategoryDTO category, double price) {
            if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");

            return add(new UnitProductDTO(id, name, category, price));
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
        public AbstractProductDTO addPersonalizableProduct(int id, String name, CategoryDTO category, double price, int maxText){
            if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (maxText < 1) throw new IllegalArgumentException("Max pers " + maxText + " cannot be less than 1.");

            return add(new PersonalizableProductDTO(id, name, category, price, maxText));
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
        public AbstractProductDTO addFoodProduct(int id, String name, double price, LocalDateTime expirationDate, int maxParticipants){
            if (!isPriceValid(price)) throw new IllegalArgumentException("Product price " + price + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
            if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");

            return add(new CateringDTO(id, name, price, maxParticipants, expirationDate, 0));
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
        public AbstractProductDTO addMeetingProduct(int id, String name, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants){
            if (!isPriceValid(pricePerPerson)) throw new IllegalArgumentException("Product price " + pricePerPerson + " cannot be negative.");
            if (!isNameValid(name)) throw new IllegalArgumentException("Product name \"" + name + "\" is invalid.");
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (maxParticipants < 1) throw new IllegalArgumentException("Max participants " + maxParticipants + " must be >= 1.");
            if (expirationDate == null) throw new IllegalArgumentException("Invalid expiration date.");

            return add(new MeetingDTO(id, name, pricePerPerson, maxParticipants, expirationDate, 0));
        }
        private int nextServiceId = 1;
        private final Map<Integer, ProductService> services = new LinkedHashMap<>();

        public AbstractProductDTO addProductService(LocalDateTime expiration, CategoryDTO category) {
            if (isProductListFull()) throw new IllegalArgumentException("Product cannot be added, there are " + MAX_PRODUCTS + " or more products.");
            if (expiration == null) throw new IllegalArgumentException("Invalid expiration date.");

            // Validar que category es de servicio
            if (!(category == CategoryDTO.INSURANCE || category == CategoryDTO.TRANSPORT || category == CategoryDTO.SHOW)) {
                throw new IllegalArgumentException("Category not valid");
            }

            int id = nextServiceId++;
            ProductService ps = new ProductService(id, expiration.toLocalDate().atStartOfDay(), category);

            services.put(id, ps);

            return ps;
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
            return getSize() >= ProductInventory.MAX_PRODUCTS;
        }
        public boolean existsServiceId(int id) {
            return services.containsKey(id);
        }
        public AbstractProductDTO getServiceDto(int id) {
            return services.get(id);
        }

}
