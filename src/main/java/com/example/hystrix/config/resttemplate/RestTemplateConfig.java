package com.example.hystrix.config.resttemplate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "restconfig")
@Data
public class RestTemplateConfig {

  private AbstractConfig config;

  @Data
  public static class AbstractConfig {

    private int maxConnection;

    private int readTimeout;

    private int connectTimeout;

    private int connectionRequestTimeout;
  }

  @Bean("restTemplate")
  RestTemplate restTemplate() throws Exception {

    return new RestTemplateBuilder()
        .readTimeoutInMS(config.readTimeout)
        .connectionRequestTimeoutInMS(config.connectionRequestTimeout)
        .connectTimeoutInMS(config.connectTimeout)
        .maxConnection(config.maxConnection)
        .build();
  }
}
