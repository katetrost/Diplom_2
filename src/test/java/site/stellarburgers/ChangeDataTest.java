package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class ChangeDataTest {

    private static User user;
    private UserClient userClient;
    String bearerToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Редактирование данных у авторизованного пользователя")
    @Description("Изменение данных пользователя. Смена пароля")
    public void userInfoCanBeChangePasswordTest() {
        userClient.create(user); // Создание пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // сохраняем токен

        // Изменение информации о пользователе
        ValidatableResponse info = userClient.userInfoChange(bearerToken, UserCredentials.getUserWithRandomPassword());

        // Проверка тела сообщения
        info.assertThat().statusCode(200);
        info.assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Редактирование данных у авторизованного пользователя")
    @Description("Изменение данных пользователя. Смена email")
    public void userInfoCanBeChangeEmailTest() {
        userClient.create(user); // Создание пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // сохраняем токен

        // Изменение информации о пользователе
        ValidatableResponse info = userClient.userInfoChange(bearerToken, UserCredentials.getUserWithRandomEmail());

        // Проверка тела сообщения
        info.assertThat().statusCode(200);
        info.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Редактирование данных на емайл, которяй уже есть в базе, у авторизованного пользователя")
    @Description("Изменение данных пользователя. Одинаковый емаил")
    public void userInfoCanNotBeChangeWithSameEmailTest (){
        userClient.create(user); // Создание пользователя
        ValidatableResponse login = userClient.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // сохраняем токен

        // Получение информации о пользователе
        ValidatableResponse info = userClient.userInfoChange(bearerToken, UserCredentials.getUserWithEmail(user));

        // Проверка тела сообщения
        info.assertThat().statusCode(403);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("Редактирование данных у неавторизованного пользователя")
    @Description("Изменение данных неавторизованного пользователя. Смена пароля")
    public void userInfoCanNotBeChangePasswordTest() {
        bearerToken = "";

        // Изменение информации о пользователе
        ValidatableResponse info = userClient.userInfoChange(bearerToken, UserCredentials.getUserWithRandomPassword());

        // Проверка тела сообщения
        info.assertThat().statusCode(401);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Редактирование данных у неавторизованного пользователя")
    @Description("Изменение данных неавторизованного пользователя. Смена email")
    public void userInfoCanNotBeChangeEmailTest() {
        bearerToken = "";

        // Изменение информации о пользователе
        ValidatableResponse info = userClient.userInfoChange(bearerToken, UserCredentials.getUserWithRandomEmail());

        // Проверка тела сообщения
        info.assertThat().statusCode(401);
        info.assertThat().body("success", equalTo(false));
        info.assertThat().body("message", equalTo("You should be authorised"));
    }
}
