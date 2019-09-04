package org.wlld.web.WebIo;

import java.util.HashMap;
import java.util.Map;

public class WebUrl {
    private static WebUrl webUrl = new WebUrl();
    private static Map<String, byte[]> urlMap = new HashMap<>();

    private WebUrl() {
    }

    public Map<String, byte[]> getRes() {
        return urlMap;
    }

    public static WebUrl getWebUrl() {
        return webUrl;
    }
}
