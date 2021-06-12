package com.sodastream.automationscripts.registration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RegisterFlowTest {

    private static WebDriver driver;
    private RegisterFlow register;

    @BeforeAll
    static void pre() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeEach
    void setup() {
        RegisterFlowTest.driver = new ChromeDriver();
        register = new RegisterFlow(driver);
    }

    @Test
    void itShouldNavigateToTheRegistrationPage() throws InterruptedException {
        register.navigateToRegisterPageFromHomePage();
        assertEquals("Registrera din produkt – Sodastream Sweden", driver.getTitle());
    }

    @Test
    void itShouldSubmitRegistrationFormSuccessfully() throws InterruptedException {
        var sodaStreamModels = List.of("Jet",
                "Genesis",
                "Spirit",
                "Spirit One Touch",
                "Crystal",
                "Source",
                "Power"
        );

        var purchaseLocations = List.of(
                "Power",
                "Netonnet",
                "Mediamarkt",
                "Webhallen",
                "Elgiganten",
                "Hemmy",
                "CDON",
                "Whiteaway",
                "Tretti",
                "Kjell &amp; Company",
                "Komplett",
                "Proshop",
                "Clas Ohlson",
                "Bagaren och Kocken",
                "Kökets favoriter",
                "Elon",
                "ICA",
                "City Gross",
                "COOP",
                "Ellos",
                "Proshop",
                "Hemköp",
                "Willys",
                "Tempo",
                "Sodastream.se"
        );

        var date = "1950-01-01";

        var registerForm = new RegisterForm.RegisterFormBuilder()
                .email("varun@gmail.com").firstName("Varun").lastName("Shrivastava")
                .purchaseLocation(purchaseLocations.get(0))
                .sodaStreamModel(sodaStreamModels.get(0))
                .purchaseDate(date)
                .build();

        register.navigateToRegisterPageFromHomePage();
        var result = register.fillFormInfoAndSubmit(registerForm);

        assertEquals("Tack för att du registrerade din maskin. Din kupongkod på 100:- på ditt nästa köp ska snart vara i din inkorg", result);
    }

    @AfterAll
    static void postAction() {
        RegisterFlowTest.driver.close();
    }

}