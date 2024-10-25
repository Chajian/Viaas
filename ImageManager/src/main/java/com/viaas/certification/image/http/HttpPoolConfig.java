package com.viaas.certification.image.http;

import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Http request pool
 */

public class HttpPoolConfig {
    //max pool size
    private Integer httpMaxConnect = 1000;
    private Integer httpConnectTimeout = 30000;
    private Integer httpRequestTimeout = 30000;
    private Integer httpDefaultMaxPerRouter = 1000;
    private Integer httpSocketTimeout = 30000;

    public static SSLContext createIngoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        // implement a X509TrustManager interface, to skip verifycation
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        sc.init(null,new TrustManager[]{trustManager},null);
        return sc;
    }

    @SneakyThrows
    @Bean
    public HttpClient httpClient(){
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https",new SSLConnectionSocketFactory(createIngoreVerifySSL())).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(httpMaxConnect);
        connectionManager.setDefaultMaxPerRoute(httpDefaultMaxPerRouter);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(httpSocketTimeout)
                .setConnectTimeout(httpConnectTimeout)
                .setConnectionRequestTimeout(httpRequestTimeout)
                .build();

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3,true))
                .build();
        return closeableHttpClient;
    }
}
