package lab8.common.models;

public class User {
    private long id;
    private String userName;
    private String passwordHash;

    public User(long id, String userName, String passwordHash){
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public User() {}

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}