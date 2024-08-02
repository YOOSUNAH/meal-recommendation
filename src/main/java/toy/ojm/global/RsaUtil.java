package toy.ojm.global;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RsaUtil {

    static Logger log = LoggerFactory.getLogger(RsaUtil.class);

    private KeyPairGenerator generator;
    private KeyFactory keyFactory;
    private Cipher cipher;

    // 1024 비트 RSA 키쌍을 생성
    public RsaUtil() {
        try {
            generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            keyFactory = KeyFactory.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
        } catch (Exception e) {
            log.debug("");
        }
    }
}
