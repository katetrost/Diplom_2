package site.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {
    private static final String USER_PATH = "/api/auth";

    @Step("Создание пользователя.")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("Авторизация пользователя.")
    public ValidatableResponse login (UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "/login")
                .then()
                .log().body();
    }

    @Step("Выход из системы.")
    public ValidatableResponse exit (String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(USER_PATH + "/logout")
                .then();
    }

    @Step ("Удалить пользователя.")
    public void delete() {
        if (Tokens.getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete(USER_PATH)
                .then()
                .statusCode(202);
    }

    @Step
    public ValidatableResponse editData(UserDataJson userData, String bearerToken) {
        return given()
                .headers("Authorization", bearerToken)
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .patch(USER_PATH + "/user")
                .then()
                .log().body();
    }

    @Step
    public ValidatableResponse editData(UserDataJson userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .patch(USER_PATH + "/user")
                .then()
                .log().body();
    }
}
