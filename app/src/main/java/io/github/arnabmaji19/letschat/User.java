package io.github.arnabmaji19.letschat;

public class User {
    private String username;
    private String userId;
    private String email;
    private String password;

    User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        userId = email.split("@")[0];
    }

    User(){}

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
