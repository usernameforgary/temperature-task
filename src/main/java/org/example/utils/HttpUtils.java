package org.example.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.exception.NeedRetryException;

import java.io.IOException;

public class HttpUtils {
    public static Response get(String url) throws NeedRetryException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if(response.code() != 200) {
                response = null;
                throw new NeedRetryException("response code not 200");
            }
        } catch (IOException e) {
            throw new NeedRetryException(e.getMessage());
        }
        return response;
    }
}
