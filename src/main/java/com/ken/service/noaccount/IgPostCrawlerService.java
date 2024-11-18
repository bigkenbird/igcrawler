package com.ken.service.noaccount;

import com.google.gson.*;
import com.ken.service.noaccount.dto.PostDto;
import com.ken.util.CommonUtil;
import io.opentelemetry.api.internal.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IgPostCrawlerService {

   public List<PostDto> getPostDto(Document document) throws IOException {
       List<PostDto> postDtos = null;
       try {
           postDtos = new ArrayList<>();
           Elements elements = document.getElementsByAttributeValueContaining("class", "x9i3mqj");
           for (Element element : elements) {

               PostDto postDto = new PostDto();

               //content
               Elements h2Elements = element.getElementsByTag("h2");

               String content = h2Elements.stream()
                       .map(Element::text)
                       .collect(Collectors.joining(""));
               postDto.setContent(content);


               Elements aHrefs = element.getElementsByTag("a");
               //link
               if (CommonUtil.collectIsNotEmpty(aHrefs)) {
                   Element aHref = aHrefs.getFirst();
                   postDto.setLink(aHref.attributes().get("href"));
               }

               Elements imagesElements = element.getElementsByTag("img");
               //imageUrl
               if (CommonUtil.collectIsNotEmpty(imagesElements)) {
                   Element imagesElement = imagesElements.getFirst();
                   postDto.setImageUrl(imagesElement.attributes().get("src"));
               }


               postDtos.add(postDto);
           }


       } catch (Exception e) {
           e.printStackTrace();
       }
       return postDtos;
   }




}
