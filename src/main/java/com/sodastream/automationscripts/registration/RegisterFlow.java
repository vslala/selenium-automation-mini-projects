package com.sodastream.automationscripts.registration;

import com.sodastream.automationscripts.exceptions.InvalidWebsiteException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.*;

public class RegisterFlow {
    private static final String BASE_URL = "https://sodastream.se/?preview_theme_id=83892338778";
    private final WebDriver driver;
    private final WebDriverWait driverWait;
    private boolean popupClosed = false;

    public RegisterFlow(WebDriver driver) {
        this.driver = driver;
        this.driverWait = new WebDriverWait(driver, 15);
        this.driver.manage().window().maximize();
    }

    public void navigateToRegisterPageFromHomePage() throws InterruptedException {
        driver.get(BASE_URL);
        verifyQAEnv();
        if (!popupClosed)
            closePopup();
        driver.findElement(linkText("Registrera")).click();
    }

    private void verifyQAEnv() {
        driverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("preview-bar-iframe"));
        WebElement adminBarFooter = driver.findElement(className("admin-bar"));
        WebElement qaTestLabel = adminBarFooter.findElement(By.tagName("strong"));
        if (!qaTestLabel.getText().equals("BOA Version QA SWEDEN"))
            throw new InvalidWebsiteException("QA Label Not Found In The Footer!!!");
        driver.switchTo().defaultContent();
    }

    private void closePopup() {
        // close popup
        driverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ju_iframe_629286"));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
            driverWait.until(ExpectedConditions.visibilityOf(
                    driver.findElement(xpath("//div[@data-layername='Close Button']")))
            ).click();
        } catch (Exception ex) {
            System.out.println("No Element found to close the popup!");
        }
        popupClosed = true;
        driver.switchTo().defaultContent();
    }

    public String fillFormInfoAndSubmit(RegisterForm registerForm) {
        driver.findElement(xpath("//*[@id=\"register-machine-form\"]/div[1]/div[1]/div/input")).sendKeys(registerForm.getEmail());
        driver.findElement(xpath("//*[@id=\"register-machine-form\"]/div[1]/div[2]/div/input")).sendKeys(registerForm.getFirstName());
        driver.findElement(xpath("//*[@id=\"register-machine-form\"]/div[1]/div[3]/div/input")).sendKeys(registerForm.getLastName());
        new Select(driver.findElement(name("sodastream_model"))).selectByValue(registerForm.getSodaStreamModel());
        new Select(driver.findElement(name("purchase_location"))).selectByValue(registerForm.getPurchaseLocation());
        var jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript(String.format("document.getElementById(\"date\").setAttribute('value', '%s')", registerForm.getPurchaseDate()));
        driver.findElement(name("accept_terms")).click();
        driver.findElement(id("submit-btn")).click();
        return driverWait.until(ExpectedConditions.visibilityOf(driver.findElement(id("register-form-result")))).getText();
    }
}
