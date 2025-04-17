package toy.ojm.csv;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantDataCrawler {
    private static final String SEOUL_PUBLIC_OPEN_DATA_URL = "https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do";
    private static final String DOWNLOAD_FILE_NAME = "서울시 일반음식점 인허가 정보.csv";

    static {
        WebDriverManager.chromedriver().setup();
    }

    @Transactional
    public Path downloadCsvFile() {
        log.debug("########## downloadCsvFile 진행 - 현재 시간 : " + new Date().toString());
        long startTime = System.currentTimeMillis();

        Path downloadPath = resolveDownloadPath();
        Path destinationPath = resolveDestinationPath();

        WebDriver driver = null;

        log.debug("########## downloadPath : {}", downloadPath);
        log.debug("########## destinationPath : {}", destinationPath);

        try {
            driver = createWebDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            download(driver);
            waitForFileDownload(downloadPath);
            deleteIfExists(destinationPath, downloadPath);
            moveFile(downloadPath, destinationPath);
            log.debug("##### 다운로드 하는데 걸린 시간 : {} ms", System.currentTimeMillis() - startTime);
        } catch (SessionNotCreatedException e) {
            log.error("browser session 생성 실패 : ", e);
        } catch (IOException | InterruptedException e) {
            log.error("Error CSV file 다운로드: ", e);
        } catch (Exception e) {
            log.error("Unexpected Error: ", e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return destinationPath;
    }

    private Path resolveDownloadPath() {
        return Paths.get(System.getProperty("user.home"), "Downloads", DOWNLOAD_FILE_NAME);
    }

    private Path resolveDestinationPath() {
        Path directoryPath = CsvConstants.DESTINATION_DIRECTORY;
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            log.debug("##########  destination directory path: {}", directoryPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("########## directory 생성 실패 ", e);
        }

        Path destinationPath = directoryPath.resolve(destinationFilenameWithTime());
        log.debug("##########  destination path: {}", destinationPath.toAbsolutePath());

        return destinationPath;
    }

    private WebDriver createWebDriver() throws IOException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        return new ChromeDriver(options);
    }

    private void download(WebDriver driver) {
        try {
            driver.get(SEOUL_PUBLIC_OPEN_DATA_URL);

            WebElement csvButton = driver.findElement(By.id("btnCsv"));
            csvButton.click();

            // 파일 다운로드 대기
            Thread.sleep(15000);
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 발생", e);
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }

    private void waitForFileDownload(Path downloadPath) throws InterruptedException, IOException {
        int maxRetries = 500;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            if (Files.exists(downloadPath) && Files.size(downloadPath) > 0) {
                log.debug("########## 파일 다운로드 성공");
                return;
            }
            log.debug("##### 다운로드 대기 중... 시도 횟수: {}/{}", retryCount + 1, maxRetries);
            Thread.sleep(1000);
            retryCount++;
        }
        throw new InterruptedException("########## 파일 다운로드 실패");
    }

    private static void deleteIfExists(Path destinationPath, Path downloadPath) {
        File downloadFile = destinationPath.toFile();
        if (downloadFile.exists()) {
            downloadFile.delete();
            log.debug("##### 기존 다운로드 파일 삭제 완료: {}", downloadPath);
        }
    }

    private String destinationFilenameWithTime() {
        return CsvConstants.DESTINATION_FILE_NAME + "." + CsvConstants.DESTINATION_FILE_EXTENSION;  // 파일이름을 생성
    }

    private void moveFile(Path sourcePath, Path destinationPath) throws IOException {
        if (!Files.exists(sourcePath)) {
            log.warn("Source file not found: {}", sourcePath.toAbsolutePath());
            return;
        }

        try {
            Files.move(sourcePath, destinationPath);
            log.debug("##### 파일이동 작업 실행");
        } catch (IOException e) {
            log.error("IOException occurred while moving file from {} to {}: {}",
                    sourcePath.toAbsolutePath(), destinationPath.toAbsolutePath(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred during file move: {}", e.getMessage(), e);
            throw e;
        }

        if (Files.exists(destinationPath)) {
            log.debug("##### CSV file 이동 성공 {}", destinationPath.toAbsolutePath());
        } else {
            log.error("File move operation completed but destination file not found: {}", destinationPath.toAbsolutePath());
            throw new IOException("File move failed ");
        }
    }
}
