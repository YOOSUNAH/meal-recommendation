package toy.ojm.asynch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncService {

    // test용
//    @Async
//    public void AsyncMethod() {
//        try {
//            Thread.sleep(2000);
//            log.info("thread name: {}", Thread.currentThread().getName());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }




}
