package com.sodastream.automationscripts.registration;

import com.sodastream.automationscripts.reportgenerators.CSVReportGenerator;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.*;
import static org.junit.jupiter.api.Assertions.*;

class RegisterFlowTest {

    private final List<String> sodaStreamModels = List.of("Jet","Genesis","Spirit","Spirit One Touch","Crystal","Source","Power");
    private final List<String> purchaseLocations = List.of("Power","Netonnet","Mediamarkt","Webhallen","Elgiganten","Hemmy","CDON","Whiteaway","Tretti","Kjell &amp; Company","Komplett","Proshop","Clas Ohlson","Bagaren och Kocken","Kökets favoriter","Elon","ICA","City Gross","COOP","Ellos","Proshop","Hemköp","Willys","Tempo","Sodastream.se");
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

        var date = DateTimeFormatter.ofPattern("YYYY-mm-dd")
                .format(LocalDateTime.now().minusDays(generateRandomNumber(1, 7300)));

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

    private int generateRandomNumber(int low, int high) {
        var rand = new Random();
        return rand.nextInt(high - low) + low;
    }

    private String generateUuidOfRandomLength(int max) {
        return UUID.randomUUID().toString().substring(0, generateRandomNumber(1, max));
    }

    @Test
    void registerWithAllPossibleCombinationOfInputs() throws IOException {
        var csvReport = new CSVReportGenerator("register-flow-test-report.csv");
        var successMessage = "Tack för att du registrerade din maskin. Din kupongkod på 100:- på ditt nästa köp ska snart vara i din inkorg";
        var count = new AtomicInteger(1);
        csvReport.addRecord(List.of("#", "First Name", "Surname", "Email", "Soda Stream Model", "Purchase Location", "Date", "Is Successful?", "Remarks/Comments"));
        purchaseLocations.forEach(pl -> {
            sodaStreamModels.forEach(ssm -> {
                var date = DateTimeFormatter.ofPattern("YYYY-mm-dd")
                        .format(LocalDateTime.now().minusDays(generateRandomNumber(1, 7300)));
                var registerForm = new RegisterForm.RegisterFormBuilder()
                        .email(format("%s@%s.%s", generateUuidOfRandomLength(8), generateUuidOfRandomLength(10), generateUuidOfRandomLength(3)))
                        .firstName(generateUuidOfRandomLength(35)).lastName(generateUuidOfRandomLength(35))
                        .purchaseLocation(pl)
                        .sodaStreamModel(ssm)
                        .purchaseDate(date)
                        .build();

                try {
                    register.navigateToRegisterPageFromHomePage();
                    String result = register.fillFormInfoAndSubmit(registerForm);
                    Thread.sleep(1000);
                    if (result.equals(successMessage)) {
                        // success
                        csvReport.addRecord(List.of(
                                String.valueOf(count.getAndIncrement()),
                                registerForm.getFirstName(), registerForm.getLastName(), registerForm.getEmail(),
                                registerForm.getSodaStreamModel(), registerForm.getPurchaseLocation(),
                                registerForm.getPurchaseDate(), "YES"
                        ));
                    } else {
                        // not success
                        csvReport.addRecord(List.of(
                                String.valueOf(count.getAndIncrement()),
                                registerForm.getFirstName(), registerForm.getLastName(), registerForm.getEmail(),
                                registerForm.getSodaStreamModel(), registerForm.getPurchaseLocation(),
                                registerForm.getPurchaseDate(), "FAILED"
                        ));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    csvReport.addRecord(List.of(
                            String.valueOf(count.getAndIncrement()),
                            registerForm.getFirstName(), registerForm.getLastName(), registerForm.getEmail(),
                            registerForm.getSodaStreamModel(), registerForm.getPurchaseLocation(),
                            registerForm.getPurchaseDate(), "FAILED"
                    ));
                } catch (Exception e) {
                    csvReport.addRecord(List.of(
                            String.valueOf(count.getAndIncrement()),
                            registerForm.getFirstName(), registerForm.getLastName(), registerForm.getEmail(),
                            registerForm.getSodaStreamModel(), registerForm.getPurchaseLocation(),
                            registerForm.getPurchaseDate(), "FAILED",
                            e.getMessage()
                    ));
                }
            });
        });
    }

    @AfterAll
    static void postAction() {
        RegisterFlowTest.driver.close();
    }

}