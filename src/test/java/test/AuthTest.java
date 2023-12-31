package test;

import com.codeborne.selenide.Condition;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.Registration.getRegisteredUser; // При импортировании статических методов, к ним можно обращаться как будто они определены в этом же классе
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;


class AuthTest {

    /*@BeforeEach
    void setup() {
        open("http://localhost:9999");
    }*/

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        open("http://localhost:9999");
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        // или var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] input").sendKeys(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").sendKeys(registeredUser.getPassword());
        $("button[role = 'button']").click();
        $(By.className("heading")).shouldHave(Condition.text("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(10));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        open("http://localhost:9999");
        var notRegisteredUser = getUser("active");
        $("[data-test-id=\"login\"] input").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] input").sendKeys(notRegisteredUser.getPassword());
        $("button[role = 'button']").click();
        $(By.className("notification__content"))
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        open("http://localhost:9999");
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] input").sendKeys(blockedUser.getLogin());
        $("[data-test-id=\"password\"] input").sendKeys(blockedUser.getPassword());
        $("button[role = 'button']").click();
        $(By.className("notification__content"))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        open("http://localhost:9999");
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=\"login\"] input").sendKeys(wrongLogin);
        $("[data-test-id=\"password\"] input").sendKeys(registeredUser.getPassword());
        $("button[role = 'button']").click();
        $(By.className("notification__content"))
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        open("http://localhost:9999");
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=\"login\"] input").sendKeys(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").sendKeys(wrongPassword);
        $("button[role = 'button']").click();
        $(By.className("notification__content"))
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}