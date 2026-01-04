package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Client;

import java.util.List;

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
