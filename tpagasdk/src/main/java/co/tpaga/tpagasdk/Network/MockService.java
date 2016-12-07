package co.tpaga.tpagasdk.Network;

import co.tpaga.tpagasdk.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MockService extends TpagaAPI {

    private final String tpagaPublicApiKey;
    private final int enviroment;
    private int codeResponse;

    public MockService(String tpagaPublicApiKey, int enviroment) {
        super(tpagaPublicApiKey, enviroment);
        this.tpagaPublicApiKey = tpagaPublicApiKey;
        this.enviroment = enviroment;
        codeResponse = 200;
    }

    public void setCodeResponse(int codeResponse) {
        this.codeResponse = codeResponse;
    }

    @Override
    protected Retrofit getAuthAdapter() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel((BuildConfig.DEBUG) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        httpClient.addInterceptor(logging);

        httpClient.addInterceptor(new FakeInterceptor(this));
//        httpClient.addInterceptor(new TPagaInterceptor(tpagaPublicApiKey));

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl((enviroment == PRODUCTION) ? PRODUCTION_TPAGA_API_HOST : SANDBOX_TPAGA_API_HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public int getCodeResponse() {
        return codeResponse;
    }
}
