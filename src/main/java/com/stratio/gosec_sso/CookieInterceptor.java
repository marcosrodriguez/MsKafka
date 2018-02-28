package com.stratio.gosec_sso;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CookieInterceptor implements Interceptor {

    private List<String> cookieHistory = new ArrayList<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (!response.headers("Set-Cookie").isEmpty()) {
            cookieHistory.addAll(response.headers("Set-Cookie"));
        }

        return response;
    }

    public List<String> getCookies() {
        return cookieHistory;
    }

    public void clearCookies() {
        cookieHistory.clear();
    }
}
