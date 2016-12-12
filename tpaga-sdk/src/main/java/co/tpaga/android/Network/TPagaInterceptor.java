package co.tpaga.android.Network;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TPaga on 8/30/16.
 */
public class TPagaInterceptor implements Interceptor {
    private String tpagaPublicApiKey;

    public TPagaInterceptor(String tpagaPublicApiKey) {
        this.tpagaPublicApiKey = tpagaPublicApiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String credentials = tpagaPublicApiKey + ":";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Request.Builder requestBuilder = original.newBuilder()
                .addHeader("Authorization", basic)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
