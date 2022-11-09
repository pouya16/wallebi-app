package com.example.wallebi_app.api;

import android.content.Context;


import com.example.wallebi_app.R;
import com.example.wallebi_app.database.LoginData;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    Retrofit retrofit;
    Context context;
    private static String verified_token = "";
    private static String token = "";
    private static RetrofitBuilder retrofitBuilder = null;

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private RetrofitBuilder(Context context){
        this.context = context;
        HttpsTrustManager.allowAllSSL();

        if(verified_token.length()<5){
            verified_token = LoginData.access_token;
        }
        retrofit = new Retrofit.Builder()
                .client(getUnsafeOkHttpClient().build())
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public static RetrofitBuilder getRetrofitAuthSingleton(Context context){

        if (retrofitBuilder!=null){
            retrofitBuilder.context = context;
            return retrofitBuilder;
        }
        return new RetrofitBuilder(context);
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };




            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();



            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + verified_token)
                            .addHeader("M2M","Basic MlFYeTRBSGpYcDVGODFvZ2o5ZVpJUnpoOXhRZU9VZVQ4b2VqQkhlWTp0OVhIZ0M5WG5CZmhhZVBwb2M2VlZNUWpGNTcycUtOTlNkSWp1VldScHZreWZLWHBzV3JINVZRdGpreFlodGlLYmluU09MeFhyYkZNdDNSQWdMVG5EbVFVTElIVHBtcHNzMGFFYnBDWU52VWdsUm9DNlYyWFFIbXhrb0VYVEtNYw==")
                            .build();
                    return chain.proceed(newRequest);
                }
            });
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
