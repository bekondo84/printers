package cm.pak.canon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class CanonShedulerConfig  {

    @Value("${thread.name.prefix}")
    private String threadNamePrefix;
    @Value("${thread.poolsize}")
    private int poolSize ;

    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix(threadNamePrefix);
        return scheduler;
    }
}
