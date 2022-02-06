package site.stellarburgers;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class GetOrdersIndividualUsers {
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
    @Description("Получение списка заказов авторизованного пользователя")
    public void orderUserInfoCanBeGetAuthUser (){
        // Создание пользователя
        userClient.create(user);
        ValidatableResponse login = userClient.login(UserCredentials.from(user)); // Авторизация пользователя
        bearerToken = login.extract().path("accessToken"); // Получение токена

        // Информация о заказах пользователя
        ValidatableResponse orderInfo = orderClient.userOrderInfo(bearerToken);
        // Получение тела списка заказов
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");

        // Проверка тела ответа запроса
        orderInfo.assertThat().statusCode(200);
        orderInfo.assertThat().body("success", equalTo(true));
        assertThat("Orders list empty", ordersList, is(not(0))); // Проверка что список заказов не пустой
    }

    @Test
    @Description("Получение списка заказов не авторизованного пользователя")
    public void orderUserInfoCantBeGetNonAuthUser (){
        bearerToken = "";

        // Информация о заказах пользователя
        ValidatableResponse orderInfo = orderClient.userOrderInfo(bearerToken);

        // Проверка тела ответа запроса
        orderInfo.assertThat().statusCode(401);
        orderInfo.assertThat().body("success", equalTo(false));
        orderInfo.assertThat().body("message", equalTo("You should be authorised"));
    }
}
