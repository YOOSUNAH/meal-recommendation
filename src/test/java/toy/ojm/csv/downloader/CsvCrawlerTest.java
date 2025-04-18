package toy.ojm.csv.downloader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toy.ojm.csv.CsvCrawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@Import({CsvCrawler.class})
@ExtendWith(SpringExtension.class)
class CsvCrawlerTest {

    @Autowired
    CsvCrawler sut;

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