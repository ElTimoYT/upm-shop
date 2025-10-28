package es.upm.iwsim22_01.models;

public abstract class User {

    private String name;
    private String DNI;
    private String email;
    private String id;

    public User() {
        this.name=name;
        this.DNI= DNI;
        this.id=id;
        this.email=email;
    }

    public String getName(){
        return name;
    }

    public String getDNI(){
        return DNI;
    }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    protected abstract boolean checkEmail(String email);
}
