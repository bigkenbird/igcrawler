package com.ken.service.noaccount;

import com.ken.service.noaccount.dto.ProfileDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class IgProfileCrawlerService {

    public ProfileDto getProfileDto(Document document){
        ProfileDto profileDto = new ProfileDto();
        Element profileTitleElement = document.getElementsByAttributeValue("property", "og:title").get(0);
        profileDto.setName(profileTitleElement.attr("content"));
        Element profileDescriptionElement = document.getElementsByAttributeValue("property", "og:description").get(0);
        profileDto.setDescription(profileDescriptionElement.attr("content"));
        Element profileImageElement = document.getElementsByAttributeValue("property", "og:image").get(0);
        profileDto.setImage(profileImageElement.attr("content"));
        return profileDto;
    }
}
