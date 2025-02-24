package toy.ojm.global.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer{

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // thread pool 에서 기본적으로 유지되는 스레드의 갯수 (기본값: 1 (SpringBoot 에서는 AutoConfiguration 으로 인해 8로 설정됨))
        executor.setMaxPoolSize(100); // thread pool 에서 사용할 수 있는 최대 thread 갯수
        executor.setQueueCapacity(200); // thread pool 작업 큐의 사이즈
        executor.setThreadNamePrefix("custom-"); // 생성되는 스레드 이름에 사용될 접두사
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 더 이상 작업을 처리할 수 없어서 , 예외를 발생
        // AbortPolicy(Default): RejectedExecutionException 예외를 발생시킴.
        // CallerRunsPolicy: ThreadPoolTaskExecutor 에 작업을 요청한 스레드에서 직접 실행.
        // DiscardPolicy: 해당 작업들을 skip.
        // DiscardOldestPolicy: Queue 에 있는 오래된 작업들을 삭제하고 새로운 작업을 추가.
        executor.initialize();
        return executor;
    }

    @Bean(name = "errorMessengerExecutor")
    public Executor errorMessengerExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("error-messenger-");
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor(){
        return taskExecutor();
    }

}
