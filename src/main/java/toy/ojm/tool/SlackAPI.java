package toy.ojm.tool;

import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Component
public class SlackAPI {

    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/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";
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