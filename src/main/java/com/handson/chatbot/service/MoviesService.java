package com.handson.chatbot.service;

import org.springframework.stereotype.Service;
import okhttp3.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MoviesService {

    public static final Pattern PRODUCT_PATTERN = Pattern.compile(
            "<h3 class=\\\"ipc-title__text\\\">([^<]+)<[^<]+<[^<]+<[^<]+<[^>]+><[^>]+>([0-9]+)<[^>]+><[^>]+>([a-zA-Z 0-9]+)<[^<]+<[^<]+<[^<]+<[^<]+<[^<]+<[^<]+<[^:]+: ([0-9.]+)");

    public String searchMovies(String keyword) throws IOException {

        return parseProductHtml(getProductHtml(keyword), keyword);
    }

    private String parseProductHtml(String html, String keyword) {
        String res = "";
        Matcher matcher = PRODUCT_PATTERN.matcher(html);

        while (matcher.find()) {
            if (StringSubsetChecker(matcher.group(1), keyword)) {
                return matcher.group(1) + '\n' + matcher.group(2) + '\t' + matcher.group(3) + '\t' + matcher.group(4)
                        + '\n';
            }
            res += matcher.group(1) + '\n' + matcher.group(2) + '\t' + matcher.group(3) + '\t' + matcher.group(4)
                    + '\n';
        }
        return res;
    }

    private boolean StringSubsetChecker(String mainString, String keyword) {
        // Convert both strings to lowercase for case-insensitive comparison
        mainString = mainString.toLowerCase();
        keyword = keyword.toLowerCase();

        // Iterate through each character of the potentialSubset
        for (int i = 0; i < keyword.length(); i++) {
            char currentChar = keyword.charAt(i);
            // Check if the character exists in the mainString
            if (mainString.indexOf(currentChar) == -1) {
                // Character not found, so potentialSubset is not a subset of mainString
                return false;
            }
        }
        // All characters of potentialSubset found in mainString
        return true;
    }

    private String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.imdb.com/chart/top/")
                .method("GET", null)
                .addHeader("accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "en-US,en;q=0.9")
                .addHeader("cache-control", "max-age=0")
                .addHeader("cookie",
                        "session-id=137-1154568-4101235; session-id-time=2082787201l; ad-oo=0; csm-hit=tb:NCTFNQQ8H4176SGYSQZ3+s-NCTFNQQ8H4176SGYSQZ3|1712000084437&t:1712000084437&adb:adblk_no; ci=e30; session-id=137-1154568-4101235; session-id-time=2082787201l; session-token=Z05d6Cb3Z5DjQAslSXVX0tX8NC4CD53UTSgjPzcbyO45acO1ocXVFKQu7/PoSUIZtY809/TnkDMcx+m/dYAkW0bo6b3JdOB1RrDeKEagk7imqh+9OBkMpCPa8Dcynpo1aMBS5LC3KS61alJYe69BM98047k03ShVv/xqo1Y4ZbLnH95IHs0ln8JGFhQ7LKQeJk2BH8hnuHs/FTtgKamz0jPLutDcL//IKwQuhfuoWxOx+DcxKgZ4vX6GO7o3qa2Lit/9D+OYi8mEer1NU1xIgiXfEDLSqDyp2c3qNf/9vXXNut9P5J+CJXNETB7pZTGccZWh67+ZeGT2emyDQUGuZSzIKE5/Xphw; ubid-main=133-2607887-3481264")
                .addHeader("referer", "https://www.google.com/")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
