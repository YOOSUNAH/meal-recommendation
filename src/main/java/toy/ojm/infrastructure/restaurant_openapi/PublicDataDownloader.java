package toy.ojm.infrastructure.restaurant_openapi;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
@RequiredArgsConstructor
public class PublicDataDownloader {

    private static final String SEOUL_PUBLIC_OPEN_DATA_URL = "https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do";
    private static final String DESTINATION_DIRECTORY = "csv-data";
    private static final String FILE_NAME = "data";
    private static final String FILE_EXTENSION = "csv";
    private static final String DOWNLOAD_FILE_NAME = "서울시 일반음식점 인허가 정보.csv";

    public Path downloadCsvFile() {
        Path downloadPath = resolveDownloadPath();
        Path destinationPath = resolveDestinationPath();

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        try {
            download(driver);
            waitForFileDownload(downloadPath);
            moveFile(downloadPath, destinationPath);
        } catch (IOException | InterruptedException e) {
            log.error("Error downloading CSV file: ", e);
        } finally {
            driver.close();
        }

        return destinationPath;
    }

    private void download(WebDriver driver) {
        driver.get(SEOUL_PUBLIC_OPEN_DATA_URL);
        WebElement csvButton = driver.findElement(By.id("btnCsv"));
        csvButton.click();
    }

    private Path resolveDownloadPath() {
        return Paths.get(System.getProperty("user.home"), "Downloads", DOWNLOAD_FILE_NAME);
    }

    private Path resolveDestinationPath()  {
        Path directoryPath;
        try {
            directoryPath = Paths.get(new ClassPathResource(DESTINATION_DIRECTORY).getFile().getAbsolutePath());
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return directoryPath.resolve(destinationFilenameWithTime());
    }

    private String destinationFilenameWithTime() {
        return FILE_NAME + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "." + FILE_EXTENSION;
    }

    private void waitForFileDownload(Path downloadPath) throws InterruptedException {
        int maxWaitTime = 60;
        int waitedTime = 0;
        while (!Files.exists(downloadPath) && waitedTime < maxWaitTime) {
            log.info("Waiting for file to download...");
            Thread.sleep(2000);
            waitedTime += 2;
        }
        if (Files.exists(downloadPath)) {
            log.info("File downloaded successfully.");
        } else {
            log.warn("File download timed out.");
        }
    }

    private void moveFile(Path sourcePath, Path destinationPath) throws IOException {
        if (Files.exists(sourcePath)) {
            Files.move(sourcePath, destinationPath);
            log.info("CSV file moved successfully to {}", destinationPath);
        } else {
            log.warn("Downloaded file not found at {}", sourcePath);
        }
    }
}