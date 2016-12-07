package co.tpaga.tpagasdk.Network;

import java.io.IOException;
import java.net.URI;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FakeInterceptor implements Interceptor {
    private MockService mockService;

    public FakeInterceptor(MockService mockService) {
        this.mockService = mockService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;

        String responseString;
        // Get Request URI.
        final URI uri = chain.request().url().uri();

        responseString = getResponse(uri);

        response = new Response.Builder()
                .code(mockService.getCodeResponse())
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }

    private String getResponse(URI uri) {
        return "{}";
    }
}
