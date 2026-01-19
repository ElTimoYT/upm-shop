package es.upm.iwsim22_01.data.repository;

import com.google.gson.reflect.TypeToken;
import es.upm.iwsim22_01.data.models.Client;
import es.upm.iwsim22_01.data.models.Ticket;

import java.util.List;

/**
 * Repositorio concreto para la gestión y persistencia de objetos Ticket.
 *
 * Utiliza como identificador único el identificador numérico del ticket y almacena los datos en un fichero JSON específico.
 */
public class TicketRepository extends AbstractRepository<Ticket, Integer> {
    @Override
    protected String getFilePath() {
        return "data/tickets/ticket.json";
    }

    @Override
    protected Integer getId(Ticket ticket) {
        return ticket.getId();
    }

    @Override
    protected TypeToken<List<Ticket>> getTypeToken() {
        return new TypeToken<>() {
        };
    }
}
