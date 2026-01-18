package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Cashier;

import java.util.List;

/**
 * Repositorio concreto para la gestión y persistencia de objetos Cashier.
 *
 * Utiliza como identificador único el DNI del cajero y almacena los datos
 * en un fichero JSON específico para este tipo de entidad.
 */
public class CashierRepository extends AbstractRepository<Cashier, String> {
    @Override
    protected String getFilePath() {
        return "data/users/cashiers.json";
    }

    @Override
    protected String getId(Cashier cashier) {
        return cashier.getDNI();
    }

    @Override
    protected TypeToken<List<Cashier>> getTypeToken() {
        return new TypeToken<>() {
        };
    }
}
