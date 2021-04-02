package DataModels;

public class User {
    public Integer ID;
    public String Username; // Database configured for max 255 characters
    public String Password; // Database configured for max 16kb of characters
    public String Salt;     // Database configured for max 16kb of characters
}
