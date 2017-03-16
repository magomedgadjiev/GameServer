package user;

public class UserProfile {
    private String login;
    private String password;
    private String email;

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserProfile(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean isEmpty(){ return email == null; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
