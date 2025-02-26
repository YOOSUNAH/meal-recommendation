package toy.ojm.global.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTaskExecutor {

    @Async("taskExecutor")
    public void foo(){

        try {
            System.out.println("START!, i am " + Thread.currentThread().getName());
            Thread.sleep(3000);
            System.out.println("FINISH!, i am " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
