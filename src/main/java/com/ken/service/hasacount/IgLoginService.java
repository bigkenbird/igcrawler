package com.ken.service.hasacount;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class IgLoginService {

    private final WebDriver driver;

    public IgLoginService(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String url, String account, String password) {
        try {
            // 開啟 Instagram 網頁
            driver.get(url);

            // 等待帳號欄位出現並定位
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(300));
            WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            WebElement passwordInput = driver.findElement(By.name("password"));

            System.out.println("inputing username and password...");

            // 輸入帳號和密碼
            usernameInput.sendKeys(account);
            passwordInput.sendKeys(password);

            // 等待登入按鈕可點擊
            WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='loginForm']/div/div[3]/button/div")));

            // 點擊登入按鈕
            loginButton.click();

            Thread.sleep(5000);

            String pageSource = driver.getPageSource();
            Files.write(Paths.get("file/html/saveLoginStatus.html"), pageSource.getBytes());

            System.out.println("saveLoginStatusButton...");


            WebElement noSaveLoginStatusButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']")));

            noSaveLoginStatusButton.click();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
