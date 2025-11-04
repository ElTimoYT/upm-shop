package es.upm.iwsim22_01.models;

public abstract class User {

    private String name;
    private String email;
    private String id;

    public User(String name, String email, String id) {
        this.name= name;
        this.id=id;
        this.email= email;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    protected abstract boolean checkEmail(String email);
}
