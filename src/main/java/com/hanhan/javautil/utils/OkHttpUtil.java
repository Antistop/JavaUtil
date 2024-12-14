package com.hanhan.javautil.utils;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pl
 * @since 2024/3/11 10:19
 */
public class OkHttpUtil {

    static OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static final MediaType XML = MediaType.get("application/xml; charset=utf-8");

    public static MediaType buildMediaType(String reqFormatType, String reqCharset) {
        return MediaType.parse(reqFormatType + "; " +reqCharset);
    }

    public static Headers buildHeaders(Map<String, String> headerMap) {
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headerMap != null && headerMap.size() != 0) {
            for (Map.Entry<String, String> headerEntry : headerMap.entrySet()) {
                headersbuilder.add(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        return headersbuilder.build();
    }

    public static String syncGet(String url) {
        return syncGet(url, null);
    }

    /**
     * 同步get请求
     */
    public static String syncGet(String url, Map<String, String> headerMap) {
        Headers headers = buildHeaders(headerMap);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();
        return synchronizedCall(request);
    }

    public static String syncPost(String url, String body) {
        return syncPost(url, JSON, null, body);
    }

    public static String syncPost(String url, MediaType mediaType, String body) {
        return syncPost(url, mediaType, null, body);
    }

    public static String syncPost(String url, Map<String, String> headerMap, String body) {
        return syncPost(url, JSON, headerMap, body);
    }

    /**
     * 同步post请求
     */
    public static String syncPost(String url, MediaType mediaType, Map<String, String> headerMap, String body) {
        Headers headers = buildHeaders(headerMap);
        if (isEmpty(body)) {
            body = "";
        }
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();
        return synchronizedCall(request);
    }

    public static String syncDelete(String url) {
        return syncDelete(url, null);
    }

    /**
     * 同步delete请求
     */
    public static String syncDelete(String url, Map<String, String> headerMap) {
        Headers headers = buildHeaders(headerMap);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build();
        return synchronizedCall(request);
    }

    private static String synchronizedCall(Request request) {
        try(Response response = HTTP_CLIENT.newCall(request).execute()){
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    return body.string();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void asyncGet(String url, Callback callback) {
        asyncGet(url, null, callback);
    }

    /**
     * 异步get请求
     */
    public static void asyncGet(String url, Map<String, String> headerMap, Callback callback) {
        Headers headers = buildHeaders(headerMap);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build();
        asynchronousCall(request, callback);
    }

    public static void asyncPost(String url, String body, Callback callback) {
        asyncPost(url, JSON, null, body, callback);
    }

    public static void asyncPost(String url, MediaType mediaType, String body, Callback callback) {
        asyncPost(url, mediaType, null, body, callback);
    }

    public static void asyncPost(String url, Map<String, String> headerMap, String body, Callback callback) {
        asyncPost(url, JSON, headerMap, body, callback);
    }

    /**
     * 异步post请求
     */
    public static void asyncPost(String url, MediaType mediaType, Map<String, String> headerMap, String body, Callback callback) {
        Headers headers = buildHeaders(headerMap);
        if (isEmpty(body)) {
            body = "";
        }
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();
        asynchronousCall(request, callback);
    }

    private static void asynchronousCall(Request request, Callback callback) {
        HTTP_CLIENT.newCall(request).enqueue(callback);
    }


    private static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static void main(String[] args) {
        OkHttpUtil.syncGet("www.baidu.com", new HashMap<>());
        OkHttpUtil.syncPost("www.baidu.com", OkHttpUtil.buildMediaType("application/json", "charset=utf-8"), new HashMap<>(), "data");

        OkHttpUtil.asyncGet("www.baidu.com", new HashMap<>(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
        OkHttpUtil.asyncPost("www.baidu.com", OkHttpUtil.JSON , new HashMap<>(), "data", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

}
