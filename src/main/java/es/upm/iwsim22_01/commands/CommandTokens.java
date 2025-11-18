package es.upm.iwsim22_01.commands;

import java.util.Iterator;
import java.util.List;

public class CommandTokens {
    private Iterator<String> iterator;
    private int remainingSize;

    public CommandTokens(List<String> tokens) {
        this.iterator = tokens.iterator();
        this.remainingSize = tokens.size();
    }

    public String next() {
        String nextToken = iterator.next();
        remainingSize--;

        return nextToken;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }
}
