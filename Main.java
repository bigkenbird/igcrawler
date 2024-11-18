package com.ken;

import com.google.gson.Gson;
import com.ken.config.WebDriverConfig;
import com.ken.service.hasacount.IgCrawlerUserPostsService;
import com.ken.service.hasacount.IgLoginService;
import com.ken.service.hasacount.dto.PostDto;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        WebDriver driver = WebDriverConfig.create();
        IgLoginService igLoginService = new IgLoginService(driver);
        IgCrawlerUserPostsService igCrawlerUserPostsService = new IgCrawlerUserPostsService(driver);

        String igLogin = "https://www.instagram.com/";
        String account = ""; //your ig account
        String password = ""; //your ig password
        //igLoginService.login(igLogin, account, password);

        String bluebottleUrl = "https://www.instagram.com/bluebottle/";

        List<PostDto> postDtos = igCrawlerUserPostsService.getLimitPostsByUrl(bluebottleUrl,10);
        postDtos.forEach(
                postDto ->{
                    System.out.println(gson.toJson(postDto));
                }
        );
    }
}