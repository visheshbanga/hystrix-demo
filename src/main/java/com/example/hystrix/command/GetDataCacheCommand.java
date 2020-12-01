package com.example.hystrix.command;

import com.example.hystrix.service.DataService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class GetDataCacheCommand extends HystrixCommand<String> {

  private String param;

  public GetDataCacheCommand(String param) {
    super(HystrixCommandGroupKey.Factory.asKey("default"));
    this.param = param;
  }

  @Override
  protected String run() throws Exception {
    return new DataService().getData(param);
  }

  @Override
  protected String getCacheKey() {
    return String.valueOf(param);
  }
}
