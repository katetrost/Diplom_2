package site.stellarburgers;

public class UserCredentials {
    public final String email;
    public final String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.email, user.password);
    }

    @Override
    public String toString() {
        return String.format("Пользователь { Email:%s, Пароль:%s }", this.email, this.password);
    }
}
