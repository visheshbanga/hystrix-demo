package com.example.hystrix.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DataService {

  public String getData(String key) throws InterruptedException {

    log.info("Inside getData");

    return key;
  }
}
