package Model;

/** Holds all User object variables */
public class User {
    private int userID;
    private String userName;
    private String password;

    public User() {}

    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "(" + this.userID + ") " + this.userName;
    }
}
