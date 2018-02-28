package com.stratio.gosec_sso;

import okhttp3.Interceptor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedirectionInterceptor implements Interceptor {

    private List<String> locationHistory = new ArrayList<>();

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        locationHistory.add(chain.request().url().toString());

        return chain.proceed(chain.request());
    }

    public void clearLocationHistory() {
        locationHistory.clear();
    }

    public List<String> getLocationHistory() {
        return locationHistory;
    }

}
