package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Cashier;

import java.util.List;

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
