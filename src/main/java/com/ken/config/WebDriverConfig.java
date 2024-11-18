package com.ken.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverConfig {

    public static WebDriver create() {
        System.setProperty("webdriver.chrome.driver", "file/driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-save-password-bubble");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return new ChromeDriver(options);
    }
}
