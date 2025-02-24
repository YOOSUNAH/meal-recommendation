package toy.ojm.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorMessenger {

    private final Executor errorMessengerExecutor;
    private final SlackAPI slackAPI;
    private final ThrottlingWindow throttlingWindow;

    public void alarm(Throwable e) {
        if (!throttlingWindow.shouldSendAlert(e.getClass().getSimpleName())) {
            return;
        }
        errorMessengerExecutor.execute(() -> sendErrorMessageToSlack(e));
    }

    private void sendErrorMessageToSlack(Throwable e) {
        List<String> list = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList().subList(0, 30);
        final String joined = String.join("\n", list);
        slackAPI.sendToSimpleText(
                """
                ------------------------------------------------------------------------------------------------------------------------------------------------
                %s : %s
                
                %s
                ------------------------------------------------------------------------------------------------------------------------------------------------
                """.formatted(
                e.getClass().getSimpleName(),
                e.getMessage(),
                joined
            )
        );
    }
}
