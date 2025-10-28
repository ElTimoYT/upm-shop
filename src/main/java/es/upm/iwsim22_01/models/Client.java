package es.upm.iwsim22_01.models;

import java.util.regex.Pattern;

public class Client extends User {
    private static final Pattern REGEX = Pattern.compile("^(?![\\w.-]+@upm\\.es$)[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @Override
    protected boolean checkEmail(String email) {
        return REGEX.matcher(email).find();
    }
}
