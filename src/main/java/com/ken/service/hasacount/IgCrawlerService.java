package com.ken.service.hasacount;

import com.google.gson.Gson;
import com.ken.service.hasacount.dto.ProfileDto;
import com.ken.service.hasacount.dto.UrlDto;
import com.ken.util.UrlParseUtil;
import io.opentelemetry.api.internal.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class IgCrawlerService {

    private final WebDriver driver;

    public IgCrawlerService(WebDriver driver) {
        this.driver = driver;
    }


    public UrlDto start(String url) {
        Gson gson = new Gson();
        UrlDto urlDto = new UrlDto();
        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("footer")));
            String pageSource = driver.getPageSource();
            String userName = UrlParseUtil.getUserName(url);
            Files.write(Paths.get("file/html/" + UrlParseUtil.postParseUrlToFileName(url, userName) + ".html"), pageSource.getBytes());
            //profile
            ProfileDto profileDto = new ProfileDto();
            WebElement profileTitleElement = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//meta[@property='og:title']"))).getFirst();
            profileDto.setName(profileTitleElement.getAttribute("content"));

            WebElement profileDescriptionElement = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//meta[@property='og:description']"))).getFirst();
            profileDto.setDescription(profileDescriptionElement.getAttribute("content"));

            WebElement profileImageElement = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//meta[@property='og:image']"))).getFirst();
            profileDto.setImage(profileImageElement.getAttribute("content"));

            System.out.println("profile: " + gson.toJson(profileDto));

            List<WebElement> links = driver.findElements(By.tagName("a"));

            List<String> posts = links.stream()
                    .map(link -> link.getAttribute("href"))
                    .filter(href -> !StringUtils.isNullOrEmpty(href) && href.contains("/p/"))
                    .collect(Collectors.toList());

            urlDto.setPosts(posts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlDto;
    }
}
