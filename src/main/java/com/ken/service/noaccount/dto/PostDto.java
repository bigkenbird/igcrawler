package com.ken.service.noaccount.dto;

import lombok.Data;

@Data
public class PostDto {
    private String id;
    private String userName;
    private String userImage;
    private String content;
    private String imageUrl;
    private String link;
}
