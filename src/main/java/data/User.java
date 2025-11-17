package data.users;

public class User {
    public String userId = "";
    public String email = "";
    public String userFirstName = "";
    public String userLastName = "";
    public String password = "";

    public User(String userId, String email, String userFirstName, String userLastName, String password) {
        this.userId = userId;
        this.email = email;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}