package es.upm.iwsim22_01.commands.predicates;

import es.upm.iwsim22_01.manager.AbstractManager;

import java.util.function.Predicate;

public class CheckIdInManagerPredicate<T> implements Predicate<T> {
    private final AbstractManager<?, T> manager;

    public CheckIdInManagerPredicate(AbstractManager<?, T> manager) {
        this.manager = manager;
    }

    @Override
    public boolean test(T value) {
        return manager.existId(value);
    }
}
