package com.ken.service.hasacount.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private String id;
    private String userName;
    private String userImage;
    private String content;
    private Set<String> imageUrls;
    private String link;
}