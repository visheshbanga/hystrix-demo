package com.example.hystrix;

import com.example.hystrix.config.hystrix.CustomHystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.HystrixPlugins;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.example")
public class HystrixDemoApplication {

  public static void main(String[] args) {
    HystrixPlugins.getInstance().registerCommandExecutionHook(new CustomHystrixCommandExecutionHook());
    SpringApplication.run(HystrixDemoApplication.class, args);
  }

}
