package site.stellarburgers;

public class UserDataJson {
    private String name;
    private String email;
    private String password;

    public UserDataJson(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public UserDataJson() {
    }

    public String getKey(String key) {
        switch (key) {
            case ("name"):
                return name;
            case ("email"):
                return email;
            case ("password"):
                return password;
        }
        return key;
    }
}
