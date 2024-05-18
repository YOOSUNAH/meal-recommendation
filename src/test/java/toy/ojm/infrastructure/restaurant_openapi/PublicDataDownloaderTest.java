package toy.ojm.infrastructure.restaurant_openapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@Import({PublicDataDownloader.class})
@ExtendWith(SpringExtension.class)
class PublicDataDownloaderTest {

    @Autowired
    PublicDataDownloader sut;

    @Test
    void test() throws IOException {
        // given

        // when
        Path filePath = sut.downloadCsvFile();

        // then
        assertThat(Files.exists(filePath)).isTrue();
        Files.deleteIfExists(filePath);
    }

}