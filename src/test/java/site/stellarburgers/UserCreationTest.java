package site.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserCreationTest {
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Проверка,что пользователя можно создать.")
    @Description("Тест /api/auth/register")
    public void checkUserCanBeCreated(){
        // Arrange - Подготовка данных
        User user = User.getRandom();

        // Act - Создать клиента
        ValidatableResponse validatableResponse = userClient.create(user);

        // Assert - Проверка
        validatableResponse.assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверьте, что нельзя создать пользователя, который уже зарегистрирован.")
    @Description("Тест /api/auth/register")
    public void checkUserCannotTwoIdenticalCreated(){
        // Arrange
        User user = User.getRandom();

        // Act
        userClient.create(user);
        ValidatableResponse validatableResponse = userClient.create(user);

        // Assert
        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя без пароля.")
    @Description("Тест /api/auth/register")
    public void checkCreatUserWithoutPassword() {
        User user = User.getRandom(true,false, true);

        ValidatableResponse validatableResponse = userClient.create(user);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя без имени.")
    @Description("Тест /api/auth/register")
    public void checkCreatUserWithoutName() {
        User user = User.getRandom(true,true, false);

        ValidatableResponse validatableResponse = userClient.create(user);

        validatableResponse.assertThat().statusCode(403);
        validatableResponse.assertThat().body("success", equalTo(false));
        validatableResponse.assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}
