package es.upm.iwsim22_01.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ProductService extends Product{


        private int MAX_PARTICIPANT;
        private LocalDateTime expirationDate;
        private int personasApuntadas;

        public ProductService(int id, String name, double price,int MAX_PARTICIPANT,LocalDateTime expirationDate){
            super(id, name,price);
            this.MAX_PARTICIPANT =MAX_PARTICIPANT;
            this.expirationDate = expirationDate;
            this.personasApuntadas = 0;
        }

        public int getMAX_PARTICIPANT(){return MAX_PARTICIPANT;}
        public LocalDateTime getExpirationDate(){return expirationDate;}
        public abstract ServiceType getServiceType();
        public int getPersonasApuntadas(){return personasApuntadas;}
        public void setPersonasApuntadas(int personasApuntadas){this.personasApuntadas = personasApuntadas;}


        @Override
        public String toString() {
            return "Product{" +
                    "class:" + this.getClass().getSimpleName() +
                    ",id:" + getId() +
                    ",name:'" + getName() + '\'' +
                    ",price:" + (getPrice() * personasApuntadas) +
                    ",max_participant:" + MAX_PARTICIPANT +
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
                " ,max people allowed:" + MAX_PARTICIPANT +
                    ",actual people in event; "+personasApuntadas+"}";
        }
    }
