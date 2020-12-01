package com.example.hystrix.command;

import com.example.hystrix.service.DataService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class GetDataCommand extends HystrixCommand<String> {

  private String param;

  public GetDataCommand(String param) {
    super(HystrixCommandGroupKey.Factory.asKey("custom-hystrix-command"));
    this.param = param;
  }

  @Override
  protected String run() throws Exception {
    return new DataService().getData(param);
  }
}
