package toy.ojm.global.error;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackErrorAlertService {
  private final Executor errorMessengerExecutor;
  private final SlackMessageSender slackMessageSender;
  private final ErrorAlertThrottler errorAlertThrottler;

  public void alarm(Throwable e) {
    if (errorAlertThrottler.shouldSendAlert(e.getClass().getSimpleName())) {
      errorMessengerExecutor.execute(() -> sendErrorMessageToSlack(e));
    }
  }

  private void sendErrorMessageToSlack(Throwable e) {
    List<String> list =
        Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList().subList(0, 30);
    final String joined = String.join("\n", list);
    slackMessageSender.sendToSimpleText(
        """
                        ------------------------------------------------------------------------------------------------------------------------------------------------
                        %s : %s

                        %s
                        ------------------------------------------------------------------------------------------------------------------------------------------------
                        """
            .formatted(e.getClass().getSimpleName(), e.getMessage(), joined));
  }
}
