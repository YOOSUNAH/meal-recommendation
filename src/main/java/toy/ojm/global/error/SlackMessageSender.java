package toy.ojm.global.error;

import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Component
public class SlackMessageSender {

    @Value("${webhook}")
    private String WEBHOOK_URL;

    private final Slack slack = Slack.getInstance();

    /**
     * 간단한 Slack 메시지 전송 서비스
     *
     * @param paramText 전송 메시지
     * @return Slack 응답 값
     */
    public WebhookResponse sendToSimpleText(String paramText) {
        try {
            return this.slack.send(
                WEBHOOK_URL,
                payload(p -> p.text(paramText)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}