package com.example.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HystrixDataService {

  @CacheResult(cacheKeyMethod = "getCacheKey")
  @HystrixCommand(commandKey = "custom-hystrix-command", fallbackMethod = "customFallback", threadPoolKey = "customPool")
  public String getData(String key) throws InterruptedException {

    log.info("Inside getData");

    // wait 5 seconds
    Thread.sleep(5000);

    return key;
  }

  private String customFallback(String key, Throwable e) {
    log.error("Inside Fallback method. Exception - {}", e.getMessage());

    return "Fallback Triggered. Value - " + key;
  }

  private String getCacheKey(String key) {
    return key;
  }
}
