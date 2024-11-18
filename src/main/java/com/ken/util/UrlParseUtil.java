package com.ken.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParseUtil {

    private static final String PARSE_URL_TO_FILE_NAME_REGEX = "https://www\\.instagram\\.com/p/([a-zA-Z0-9]+)/";
    private static final String PARSE_URL_TO_USER_NAME_REGEX = "https://www\\.instagram\\.com/([a-zA-Z0-9]+)/";

    public static String postParseUrlToFileName(String url, String userName) {
        Pattern pattern = Pattern.compile(PARSE_URL_TO_FILE_NAME_REGEX);
        Matcher matcher = pattern.matcher(url);
        return (matcher.find()) ? String.format("%s-p-%s", userName, matcher.group(1)) : null;
    }

    public static String getUserName(String url) {
        Pattern pattern = Pattern.compile(PARSE_URL_TO_USER_NAME_REGEX);
        Matcher matcher = pattern.matcher(url);
        return (matcher.find()) ? matcher.group(1) : null;
    }

    public static String parseHtmlFileToString(String htmlFilePath){
        String htmlContent = null;
        try{
            htmlContent = Files.readString(Paths.get(htmlFilePath));
        }
        catch (Exception e){
            return null;
        }
        return htmlContent;
    }

}
