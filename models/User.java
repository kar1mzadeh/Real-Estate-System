package models;

public abstract class User {
    protected String username;
    protected String role;
    protected String email;
    


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }
    
    public User ()
    {
        
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public abstract void showMenu();
}
