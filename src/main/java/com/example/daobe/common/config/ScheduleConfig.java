package com.example.daobe.common.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT10S")
public class ScheduleConfig implements SchedulingConfigurer, SmartLifecycle {

    private static final String SCHEDULER_EXECUTOR_NAME_PREFIX = "scheduler-task-";

    private final ThreadPoolTaskScheduler taskScheduler = createTaskSchedulerThreadPool();

    private ThreadPoolTaskScheduler createTaskSchedulerThreadPool() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setAwaitTerminationSeconds(5);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.setThreadNamePrefix(SCHEDULER_EXECUTOR_NAME_PREFIX);
        return taskScheduler;
    }

    @Bean
    public LockProvider lockProvider(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockProvider(redisConnectionFactory);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler);
    }

    @Override
    public void start() {
        taskScheduler.initialize();
    }

    @Override
    public void stop() {
        taskScheduler.shutdown();
    }

    @Override
    public boolean isRunning() {
        return taskScheduler.isRunning();
    }
}
