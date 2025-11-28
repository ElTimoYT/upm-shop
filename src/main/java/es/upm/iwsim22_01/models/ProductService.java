package es.upm.iwsim22_01.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ProductService extends Product{
        private int maxParticipant;
        private LocalDateTime expirationDate;
        private int personasApuntadas;

        public ProductService(int id, String name, double price, int maxParticipant, LocalDateTime expirationDate){
            super(id, name,price);
            this.maxParticipant = maxParticipant;
            this.expirationDate = expirationDate;
            this.personasApuntadas = 0;
        }

        public LocalDateTime getExpirationDate() {
            return expirationDate;
        }

        public int getPersonasApuntadas() {
            return personasApuntadas;
        }

        public void setPersonasApuntadas(int personasApuntadas) {
            this.personasApuntadas = personasApuntadas;
        }

        public boolean checkTime() {
            LocalDateTime now = LocalDateTime.now();

            return !getExpirationDate().isBefore(now);
        }

        @Override
        public String toString() {
            return "Product{" +
                    "class:" + this.getClass().getSimpleName() +
                    ",id:" + getId() +
                    ",name:'" + getName() + '\'' +
                    ",price:" + (getPrice() * personasApuntadas) +
                    ",max_participant:" + maxParticipant +
                    ",expiration:" + expirationDate +
                    '}';
        }

        public String printTicketWithPeople(){
            return "Product{" +
                "class:" + this.getClass().getSimpleName() +
                " ,id:" + getId() +
                " ,name:'" + getName() + '\'' +
                " ,price:" + (getPrice() * personasApuntadas) +
                    " ,date of event: " + expirationDate +
                " ,max people allowed:" + maxParticipant +
                    ",actual people in event; "+personasApuntadas+"}";
        }
    }
