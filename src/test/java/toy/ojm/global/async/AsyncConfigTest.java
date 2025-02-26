package toy.ojm.global.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(AsyncTaskExecutor.class)
@SpringBootTest
class AsyncConfigTest {

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @Test
    void taskExecutor() throws InterruptedException {

        for (int i = 0; i < 5; i++) {
            asyncTaskExecutor.foo();
        }

        Thread.sleep(10000);
    }


}