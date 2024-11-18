package com.ken.service.noaccount;

import com.google.gson.Gson;
import com.ken.config.WebDriverConfig;
import com.ken.service.noaccount.dto.ProfileDto;
import com.ken.service.noaccount.dto.PostDto;
import com.ken.util.UrlParseUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IgPostCrawlerExample {

    private IgProfileCrawlerService igProfileCrawlerService;

    private IgPostCrawlerService igPostCrawlerService;

    public IgPostCrawlerExample(){
        this.igProfileCrawlerService = new IgProfileCrawlerService();
        this.igPostCrawlerService = new IgPostCrawlerService();

    }

    public void start(String url) {
        //WebDriver driver = WebDriverConfig.create();
        Gson gson = new Gson();
        try {
//            driver.get(url);
//            Thread.sleep(10000);
//            String pageSource = driver.getPageSource();
//            Files.write(Paths.get("file/html/ig.html"), pageSource.getBytes());
            String pageSource = UrlParseUtil.parseHtmlFileToString("file/html/ig.html");
            Document document = Jsoup.parse(pageSource);
            //profile
            ProfileDto profileDto = igProfileCrawlerService.getProfileDto(document);
            System.out.println("profile: " + gson.toJson(profileDto));

            //post list
            List<PostDto> postList = igPostCrawlerService.getPostDto(document);
            System.out.println("postList Size:"+ postList.size());
            postList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            driver.quit();
        }
    }


    public static void main(String[] args) {
        IgPostCrawlerExample example = new IgPostCrawlerExample();
        String url = "https://www.instagram.com/bluebottle/";
        example.start(url);
    }
}
