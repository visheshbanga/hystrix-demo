package com.example.hystrix.config.resttemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RestTemplateBuilder {

  private int connectTimeoutInMS;
  private int connectionRequestTimeoutInMS;
  private int maxConnection;
  private int readTimeoutInMS;
  private boolean enableSSLConnection;

  public RestTemplateBuilder() {
    // do nothing
  }

  public RestTemplateBuilder connectTimeoutInMS(int connectTimeoutInMS) {
    this.connectTimeoutInMS = connectTimeoutInMS;
    return this;
  }

  public RestTemplateBuilder connectionRequestTimeoutInMS(int connectionRequestTimeoutInMS) {
    this.connectionRequestTimeoutInMS = connectionRequestTimeoutInMS;
    return this;
  }

  public RestTemplateBuilder readTimeoutInMS(int readTimeoutInMS) {
    this.readTimeoutInMS = readTimeoutInMS;
    return this;
  }

  public RestTemplateBuilder maxConnection(int maxConnection) {
    this.maxConnection = maxConnection;
    return this;
  }

  public RestTemplateBuilder enableSSLConnection(boolean enableSSLConnection) {
    this.enableSSLConnection = enableSSLConnection;
    return this;
  }

  public RestTemplate build() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    RestTemplate restTemplate = new RestTemplate(getCustomHttpRequestFactory(this));
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();

    for (HttpMessageConverter<?> httpMessageConverter : converters) {
      if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
        ObjectMapper objectMapper = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        jsonConverter.setSupportedMediaTypes(Collections.singletonList(new MediaType("application", "json")));
      }
    }

    return restTemplate;
  }

  private static ClientHttpRequestFactory getCustomHttpRequestFactory(RestTemplateBuilder builder) throws KeyStoreException,
      NoSuchAlgorithmException, KeyManagementException {

    HttpComponentsClientHttpRequestFactory customFactory = new HttpComponentsClientHttpRequestFactory();
    customFactory.setConnectionRequestTimeout(builder.connectionRequestTimeoutInMS);
    customFactory.setConnectTimeout(builder.connectTimeoutInMS);
    customFactory.setReadTimeout(builder.readTimeoutInMS);

    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
        .setMaxConnTotal(builder.maxConnection)
        .setMaxConnPerRoute(builder.maxConnection)
        .setConnectionTimeToLive(5L, TimeUnit.MINUTES);

    if (builder.enableSSLConnection) {
      SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
      sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
      SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
          NoopHostnameVerifier.INSTANCE);

      httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
    }

    customFactory.setHttpClient(httpClientBuilder.build());

    return new BufferingClientHttpRequestFactory(customFactory);
  }
}
