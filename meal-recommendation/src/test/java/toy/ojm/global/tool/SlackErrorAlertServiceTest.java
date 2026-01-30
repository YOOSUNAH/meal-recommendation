package toy.ojm.global.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toy.ojm.global.error.SlackErrorAlertService;

@SpringBootTest
class SlackErrorAlertServiceTest {

  @Autowired private SlackErrorAlertService sut;

  @Test
  void test() throws InterruptedException {
    try {
      String asdf = null;
      asdf.startsWith("a???");
    } catch (Exception e) {
      for (int i = 0; i < 5; i++) {
        sut.alarm(e);
      }
      Thread.sleep(1000);
    }
  }
}
