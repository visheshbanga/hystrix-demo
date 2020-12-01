package com.example.hystrix.controller;

import com.example.hystrix.command.GetDataCacheCommand;
import com.example.hystrix.command.GetDataCommand;
import com.example.hystrix.service.HystrixDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping(value = "/hystrix")
public class RestApiController {

  @Autowired
  private HystrixDataService hystrixDataService;

  @GetMapping(value = "runGetDataCommand")
  public String runGetDataCommand(@RequestParam String key) {
    return new GetDataCommand(key).execute();
  }

  @GetMapping(value = "runGetDataCacheCommand")
  public String runGetDataCacheCommand() {

    // Hystrix context is initialized in HystrixFilter class

    GetDataCacheCommand command1 = new GetDataCacheCommand("key1");
    GetDataCacheCommand command2 = new GetDataCacheCommand("key2");
    GetDataCacheCommand command3 = new GetDataCacheCommand("key2");

    String res = command1.execute();
    command2.execute();
    command3.execute();

    log.info("Response fetched from cache......?");
    log.info("Command1 - {}", command1.isResponseFromCache());
    log.info("Command2 - {}", command2.isResponseFromCache());
    log.info("Command3 - {}", command3.isResponseFromCache());

    return res;
  }

  @GetMapping(value = "runHystrixDataService")
  public String runHystrixDataService(@RequestParam String key) throws InterruptedException {
    return hystrixDataService.getData(key);
  }
}
