package com.ken.service.hasacount;

import com.ken.service.hasacount.dto.PostDto;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IgLittlePostService {

    private final WebDriver driver;

    public IgLittlePostService(WebDriver driver) {
        this.driver = driver;
    }

    public PostDto start(Boolean isFirstPost) {
        PostDto postDto = new PostDto();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if(isFirstPost){
            WebElement firstPostElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("_aagw")));
            firstPostElement.click();
        }

        WebElement articleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
        String userName = getUserName(articleElement);
        postDto.setUserName(userName);
        postDto.setContent(getContent(articleElement));
        postDto.setUserImage(getUserImageUrl(articleElement, userName));
        postDto.setImageUrls(getImageUrls(articleElement));
        postDto.setLink(driver.getCurrentUrl());
        return postDto;
    }



    private String getUserName(WebElement articleElement) {
        WebElement userName = articleElement.findElements(By.cssSelector("span.xt0psk2")).getFirst();
        return userName.getText();
    }

    private String getUserImageUrl(WebElement articleElement, String userName) {
        String cssSelector = String.format("img[alt=\"%s的大頭貼照\"]", userName);
        WebElement userImage = articleElement.findElement(By.cssSelector(cssSelector));
        return userImage.getAttribute("src");
    }

    private String getContent(WebElement articleElement) {
        List<WebElement> contents = articleElement.findElements(By.tagName("h1"));
        return contents.stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(""));
    }

    private Set<String> getImageUrls(WebElement articleElement) {
        WebElement nextImagesButton;
        Set<String> imagesUrls = new LinkedHashSet<>();

        JavascriptExecutor executor = (JavascriptExecutor) driver;


        try {
            nextImagesButton = articleElement.findElement(By.xpath("//button[@aria-label='下一步']"));
        } catch (Exception e) {
            nextImagesButton = null;
        }

        while (true) {
            List<WebElement> imagesUrlsElements = articleElement.findElements(By.cssSelector("div._aagv"));
            if (imagesUrlsElements.isEmpty()) {
                break;
            }

            for (WebElement imagesUrlsElement : imagesUrlsElements) {
                String imagesUrl = imagesUrlsElement.findElement(By.tagName("img")).getAttribute("src");
                imagesUrls.add(imagesUrl);
            }
            if(nextImagesButton==null){
                break;
            }
            executor.executeScript("arguments[0].click();", nextImagesButton);
            try {
                nextImagesButton = articleElement.findElement(By.xpath("//button[@aria-label='下一步']"));
            } catch (Exception e) {
                nextImagesButton = null;
            }

            if(nextImagesButton==null){
                break;
            }

        }
        return imagesUrls;
    }

    private Set<String> getVideoUrls(WebDriverWait wait){
        List<WebElement> imageElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//meta[@property='og:video']")));
        return imageElements.stream().map(element -> {
            return element.getAttribute("content");
        }).collect(Collectors.toSet());
    }


}
