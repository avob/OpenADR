package com.avob.openadr.server.common.vtn;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@Profile(AsyncConfig.PUSH_PROFILE)
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	public static final String PUSH_PROFILE = "http_push";

	@Override
	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setCorePoolSize(2);
		taskExecutor.setAwaitTerminationSeconds(1);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setThreadNamePrefix("Oadr20DREventPushServiceExecutor-");
		taskExecutor.setDaemon(true);
		taskExecutor.initialize();
		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}

}
