package com.ken.service.hasacount;

import com.ken.service.hasacount.dto.PostDto;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class IgCrawlerUserPostsService {

    private final WebDriver driver;

    private final IgCrawlerService igCrawlerService;

    private final IgLittlePostService igLittlePostService;

    public IgCrawlerUserPostsService(WebDriver driver) {
        this.driver = driver;
        this.igCrawlerService = new IgCrawlerService(driver);
        this.igLittlePostService = new IgLittlePostService(driver);
    }

    public List<PostDto> getLimitPostsByUrl(String url, Integer limitPostsNum) {
        List<PostDto> results = new ArrayList<>();
        igCrawlerService.start(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //點第一篇貼文
        PostDto firstPost = igLittlePostService.start(true);

        if (firstPost == null) {
            return null;
        }

        results.add(firstPost);

        WebElement nextPostsButton;
        JavascriptExecutor executor = (JavascriptExecutor) driver;

        try {
            nextPostsButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div._aaqg")))
                    .findElement(By.cssSelector("button._abl-"));

        } catch (Exception e) {
            e.printStackTrace();
            nextPostsButton = null;
        }

        executor.executeScript("arguments[0].click();", nextPostsButton);


        while (results.size() < limitPostsNum) {
            PostDto postDto = igLittlePostService.start(false);
            results.add(postDto);
            if(nextPostsButton==null){
                break;
            }
            executor.executeScript("arguments[0].click();", nextPostsButton);
            try {
                nextPostsButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div._aaqg")))
                        .findElement(By.cssSelector("button._abl-"));

            } catch (Exception e) {
                e.printStackTrace();
                nextPostsButton = null;
            }

            if(nextPostsButton==null){
                break;
            }
        }

        return results;
    }

}
