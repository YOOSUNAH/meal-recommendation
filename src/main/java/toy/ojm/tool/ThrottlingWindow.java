package toy.ojm.tool;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThrottlingWindow {
    private static final long WINDOW_MILLIS = 1 * 1000;
    private final Map<String, Long> lastAlertTimestamps = new ConcurrentHashMap<>();
//    private final Map<String, Long> lastAlertTimestamps = new HashMap<>();

    /**
     * 해당 에러 타입에 대해 알림을 보낼 수 있는지 체크한다.
     * 이전 알림 이후 windowMillis가 경과했으면 true 반환.
     */
    public boolean shouldSendAlert(String errorType) {
        long now = System.currentTimeMillis();
        Long lastTime = lastAlertTimestamps.get(errorType);
        if (lastTime == null || now - lastTime >= WINDOW_MILLIS) {
            lastAlertTimestamps.put(errorType, now);
            return true;
        }
        return false;
    }
}
