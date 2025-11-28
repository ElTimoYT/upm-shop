package es.upm.iwsim22_01.models.user;

public abstract class AbstractUser {

    private String name;
    private String email;
    private String id;

    public AbstractUser(String name, String email, String id) {
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
}
