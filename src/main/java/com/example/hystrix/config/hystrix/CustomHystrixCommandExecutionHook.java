package com.example.hystrix.config.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.Map;
import java.util.Objects;

public class CustomHystrixCommandExecutionHook extends HystrixCommandExecutionHook {

  private static final Logger LOGGER = LogManager.getLogger(CustomHystrixCommandExecutionHook.class);

  private HystrixRequestVariableDefault<Map<String, String>> threadContext = new HystrixRequestVariableDefault<>();

  @Override
  public <T> void onStart(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onStart] - {}", Thread.currentThread().getName());

    HystrixRequestContext.initializeContext();
    setHystrixRequestVariables();
  }

  @Override
  public <T> void onSuccess(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onSuccess] - {}", Thread.currentThread().getName());

    HystrixRequestContext.getContextForCurrentThread().shutdown();
    super.onSuccess(commandInstance);
  }

  @Override
  public <T> void onThreadStart(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onThreadStart] - {}", Thread.currentThread().getName());
  }

  @Override
  public <T> void onThreadComplete(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onThreadComplete] - {}", Thread.currentThread().getName());
  }

  @Override
  public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {

    LOGGER.debug("[onError] - {}", Thread.currentThread().getName());

    HystrixRequestContext.getContextForCurrentThread().shutdown();
    return super.onError(commandInstance, failureType, e);
  }

  @Override
  public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onExecutionStart] - {}", Thread.currentThread().getName());

    setThreadContext();
  }

  @Override
  public <T> void onExecutionSuccess(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onExecutionSuccess] - {}", Thread.currentThread().getName());

    if (Objects.nonNull(commandInstance)) {
      LOGGER.info(((HystrixCommand<T>) commandInstance).getCommandKey().toString() + " hit successfully");
    }
  }

  @Override
  public <T> void onFallbackStart(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onFallbackStart] - {}", Thread.currentThread().getName());

    setThreadContext();
  }

  @Override
  public <T> void onFallbackSuccess(HystrixInvokable<T> commandInstance) {

    LOGGER.debug("[onFallbackSuccess] - {}", Thread.currentThread().getName());
  }

  private void setHystrixRequestVariables() {
    threadContext.set(ThreadContext.getContext());
  }

  private void setThreadContext() {

    ThreadContext.putAll(threadContext.get());
  }
}
