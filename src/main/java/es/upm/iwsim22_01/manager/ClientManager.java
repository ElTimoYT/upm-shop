package es.upm.iwsim22_01.manager;

import es.upm.iwsim22_01.models.Client;

public class ClientManager extends AbstractManager<Client, String> {
    @Override
    public void add(Client client) {
        add(client, client.getId()); //TODO: maxima cantidad de clientes?
    }



}
