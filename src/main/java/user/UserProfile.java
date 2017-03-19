package user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.jws.soap.SOAPBinding;

public class UserProfile implements Comparable<UserProfile> {
    private String login;
    private String password;
    private String email;

    @JsonIgnore
    private int rating = 0;

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


    public boolean isEmpty(){ return email == null; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int compareTo(UserProfile o) {
        if (o.getRating() == rating){
            return 0;
        }else if (o.getRating() > rating){
            return -1;
        } else{
            return 1;
        }
    }
}
