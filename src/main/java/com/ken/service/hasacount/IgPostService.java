package com.ken.service.hasacount;

import com.ken.service.hasacount.dto.PostDto;
import com.ken.util.UrlParseUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class IgPostService {

    private final WebDriver driver;

    public IgPostService(WebDriver driver) {
        this.driver = driver;
    }

    public PostDto parse(String url) {
        PostDto postDto = new PostDto();

        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("footer")));
            String pageSource = driver.getPageSource();
            String userName = UrlParseUtil.getUserName(url);
            Files.write(Paths.get(String.format("file/html/%s.html", UrlParseUtil.postParseUrlToFileName(url, userName))), pageSource.getBytes());

            //userName
            WebElement spanElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.xt0psk2")));
            assert spanElement != null;
            postDto.setUserName(spanElement.getText());

            //userImage
            WebElement userImageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"bluebottle的大頭貼照\"]")));
            assert userImageElement != null;
            postDto.setUserImage(userImageElement.getAttribute("src"));

            //內文
            List<WebElement> postElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("h1")));
            String content = postElements.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(""));
            postDto.setContent(content);

            //內文images
            WebElement nextImagesButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='下一步']")));
            Set<String> imagesUrls = new LinkedHashSet<>();
            while (nextImagesButton != null) {
                List<WebElement> imagesUrlsElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div._aagu _aato.div._aagv")));

                if (imagesUrlsElements.isEmpty()) {
                    break;
                }

                for (WebElement imagesUrlsElement : imagesUrlsElements) {
                    String imagesUrl = imagesUrlsElement.findElement(By.tagName("img")).getAttribute("src");
                    imagesUrls.add(imagesUrl);
                    System.out.println("imagesUrl:" + imagesUrl);
                }
                nextImagesButton.click();
                try {
                    nextImagesButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='下一步']")));
                } catch (Exception e) {
                    nextImagesButton = null;
                }

            }

            postDto.setImageUrls(imagesUrls);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postDto;
    }


}
