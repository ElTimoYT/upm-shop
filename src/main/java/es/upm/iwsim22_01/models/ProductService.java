package es.upm.iwsim22_01.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum ServiceType {
    FOOD,
    MEETING;
}

    public abstract class ProductService extends Product{

        private int MAX_PARTICIPANT;
        private LocalDateTime expirationDate;

        public ProductService(int id, String name, double price,int MAX_PARTICIPANT,LocalDateTime expirationDate){
            super(id, name,price);
            this.MAX_PARTICIPANT =MAX_PARTICIPANT;
            this.expirationDate = expirationDate;
        }

        public int getMAX_PARTICIPANT(){return MAX_PARTICIPANT;}
        public LocalDateTime getExpirationDate(){return expirationDate;}
        public abstract ServiceType getServiceType();

        @Override
        public String toString() {
            return "Product{" +
                    "class:" + this.getClass().getSimpleName() +
                    ",id:" + getId() +
                    ",name:'" + getName() + '\'' +
                    ",price:" + getPrice() +
                    ",max_participant:" + MAX_PARTICIPANT +
                    ",expiration:" + expirationDate +
                    '}';
        }
    }
