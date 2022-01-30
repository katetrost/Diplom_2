package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {
    private UserClient userClient;
    User user = User.getRandom();
    public String refreshToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.delete();
    }

    @Test
    @DisplayName("Проверить, что пользователь может авторизоваться")
    @Description("Тест /api/auth/login")
    public void checkUserLogin(){
        userClient.create(user);

        ValidatableResponse validatableResponse = userClient.login(UserCredentials.from(user));
        refreshToken = validatableResponse.extract().path("refreshToken");

        assertThat("Courier ID incorrect",refreshToken, is(not(0)));
        validatableResponse.assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться без email")
    @Description("Тест /api/auth/login")
    public void checkUserLoginWithoutUserName(){
        userClient.create(user);

        ValidatableResponse validatableResponse = userClient.login(new UserCredentials(null, user.password));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться без пароля")
    @Description("Тест /api/auth/login")
    public void checkUserLoginNull(){
        userClient.create(user);

        ValidatableResponse validatableResponse = userClient.login(new UserCredentials (user.email,null));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться с неверным логином")
    @Description("Тест /api/auth/login")
    public void checkLoginWithInvalidEmail(){
        userClient.create(user);

        ValidatableResponse validatableResponse = userClient.login(new UserCredentials("123", user.password));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверить, что пользователь не может авторизоваться с неверным паролем")
    @Description("Тест /api/auth/login")
    public void checkLoginWithInvalidPassword(){
        userClient.create(user);

        ValidatableResponse validatableResponse = userClient.login(new UserCredentials (user.email,"123"));

        validatableResponse.assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
