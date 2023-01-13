package com.project.popflix;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.javafaker.Faker;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SignUpTest {

    WebDriver driver;
    Faker faker;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void successfulSignUpRedirectsToSignIn() {
        driver.get("http://localhost:8080/users/new");
        driver.findElement(By.id("email")).sendKeys("fakeemail@gmail.com");
        driver.findElement(By.id("username")).sendKeys("fakename");
        driver.findElement(By.id("password")).sendKeys("Password1!");
        driver.findElement(By.className("primary-btn")).click();
        String title = driver.getTitle();
        Assert.assertEquals("Log in", title);
    }

    @Test
    public void noEmailDoesntGoToLogin() {
        String RN = "RandomName";
        String RP = "RandomPassword";
        driver.get("http://localhost:8080/users/new");
        driver.findElement(By.id("username")).sendKeys(RN);
        driver.findElement(By.id("password")).sendKeys(RP);
        driver.findElement(By.className("primary-btn")).click();
        String error = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/users/new", error);
    }
     @Test
    public void badEmailDoesntGoToLogin() {
        String RE = "randomBadEmail";
        String RN = "RandomName";
        String RP = "RandomPassword";
        driver.get("http://localhost:8080/users/new");
        driver.findElement(By.id("email")).sendKeys(RE);
        driver.findElement(By.id("username")).sendKeys(RN);
        driver.findElement(By.id("password")).sendKeys(RP);
        driver.findElement(By.className("primary-btn")).click();
        String error = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/users/new", error);
    }
      @Test
    public void noUsernameDoesntGoToLogin() {
        String RE = "randomBadEmail";
        String RN = "";
        String RP = "RandomPassword";
        driver.get("http://localhost:8080/users/new");
        driver.findElement(By.id("email")).sendKeys(RE);
        driver.findElement(By.id("username")).sendKeys(RN);
        driver.findElement(By.id("password")).sendKeys(RP);
        driver.findElement(By.className("primary-btn")).click();
        String error = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/users/new", error);
    }
     @Test
    public void noPasswordDoesntGoToLogin() {
        String RE = "randomBadEmail";
        String RN = "RandomName";
        String RP = "";
        driver.get("http://localhost:8080/users/new");
        driver.findElement(By.id("email")).sendKeys(RE);
        driver.findElement(By.id("username")).sendKeys(RN);
        driver.findElement(By.id("password")).sendKeys(RP);
        driver.findElement(By.className("primary-btn")).click();
        String error = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/users/new", error);
    }
     @Test
    public void badPasswordDoesntGoToLogin() {
        String RE = "randomBadEmail";
        String RN = "RandomName";
        String RP = "qweafd";
        driver.get("http://localhost:8080/users/new");
        driver.findElement(By.id("email")).sendKeys(RE);
        driver.findElement(By.id("username")).sendKeys(RN);
        driver.findElement(By.id("password")).sendKeys(RP);
        driver.findElement(By.className("primary-btn")).click();
        String error = driver.getCurrentUrl();
        Assert.assertEquals("http://localhost:8080/users/new", error);
    }
}