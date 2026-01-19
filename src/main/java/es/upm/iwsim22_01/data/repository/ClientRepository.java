package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Client;

import java.util.List;

/**
 * Repositorio concreto para la gestión y persistencia de objetos Client.
 *
 * Utiliza como identificador único el DNI del cliente y almacena los datos
 * en un fichero JSON específico para este tipo de entidad.
 */
public class ClientRepository extends AbstractRepository<Client, String> {
    @Override
    protected String getFilePath() {
        return "data/users/clients.json";
    }

    @Override
    protected String getId(Client client) {
        return client.getDNI();
    }

    @Override
    protected TypeToken<List<Client>> getTypeToken() {
        return new TypeToken<>() {
        };
    }
}
