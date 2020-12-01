package com.example.hystrix.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DataService {

  public String getData(String key) throws InterruptedException {

    log.info("Start getData");

    // wait for 5 seconds
//    Thread.sleep(5000);

    log.info("End getData");
    return key;
  }
}
