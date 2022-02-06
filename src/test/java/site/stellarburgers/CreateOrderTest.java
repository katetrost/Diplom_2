package site.stellarburgers;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private User user;
    private UserClient userClient;
    private Ingredients ingredients;
    public OrderClient orderClient;
    String bearerToken;

    // Создание рандомного пользователя и бургера
    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        ingredients = Ingredients.getRandomBurger();
        orderClient = new OrderClient();
    }

    @Test
    @Description("Создание заказа. Зарегистрированный пользователь")
    public void orderCanBeCreatedRegisteredUser (){
        // Создание пользователя
        ValidatableResponse userResponse = userClient.create(user);
        bearerToken = userResponse.extract().path("accessToken");

        // Создание заказа
        ValidatableResponse orderResponse = orderClient.create(bearerToken,ingredients);
        int orderNumber = orderResponse.extract().path("order.number"); // Получение номера созданого заказа

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(200);
        orderResponse.assertThat().body("success", equalTo(true));
        assertThat("The order number is missing", orderNumber, is(not(0))); // Проверка что присвоен номер заказа
    }

    @Test
    @Description ("Создание заказа. Не зарегистрированный пользователь")
    public void orderCanBeCreatedNonRegisteredUser (){
        bearerToken = "";

        // Создание заказа
        ValidatableResponse orderResponse = orderClient.create(bearerToken,ingredients);
        int orderNumber = orderResponse.extract().path("order.number"); // Получение номера созданого заказа

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(200);
        orderResponse.assertThat().body("success", equalTo(true));
        assertThat("The order number is missing", orderNumber, is(not(0))); // Проверка что присвоен номер заказа
    }

    @Test
    @Description ("Создание заказа без ингредиентов")
    public void orderCanNotBeCreatedWithOutIngredients (){
        // Создание пользователя
        ValidatableResponse userResponse = userClient.create(user);
        bearerToken = userResponse.extract().path("accessToken");

        // Создание заказа без ингредиентов
        ValidatableResponse orderResponse = orderClient.create(bearerToken,Ingredients.getNullIngredients());

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(400);
        orderResponse.assertThat().body("success", equalTo(false));
        orderResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description ("Создание заказа с невалидными ингридиентами")
    public void orderCanNotBeCreatedWithIncorrectIngredients (){
        // Создание пользователя
        ValidatableResponse userResponse = userClient.create(user);
        bearerToken = userResponse.extract().path("accessToken");

        // Создание заказа
        ValidatableResponse orderResponse = orderClient.create(bearerToken,Ingredients.getIncorrectIngredients());

        // Проверка тела ответа запроса
        orderResponse.assertThat().statusCode(500);
    }
}
