package toy.ojm.infra;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import toy.ojm.global.constants.CsvConstants;

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
        return Paths.get(System.getProperty("user.home"), "Downloads", DOWNLOAD_FILE_NAME);        // 홈디렉토리 밑에 Downloads 폴더에 DOWNLOAD_FILE_NAME 이름을 가진 파일의 경로;
    }

    private Path resolveDestinationPath() {
        // user.dir: 현재 작업 디렉토리에, "현재실행디렉토리/csv-data" 경로를 생성
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

        return destinationPath;   //directoryPath 특정 디렉토리의 경로를 나타내는 Path 객체, resolve() : 기존의 경로에 다른 경로를 결합하여 새로운 경로를 생성, destinationFilenameWithTime : 파일이름을 생성하는 메서드
        // csv-data 디렉토리 내에 시간을 포함한 고유한 파일 이름으로 새로운 파일 경로를 생성하고, 그 경로를 나타내는 Path 객체를 반환
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

            // JavaScript 로드 대기 시간 확장
//            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
//
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
//
//            // 페이지 완전 로드 대기
//            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
//                    .executeScript("return document.readyState")
//                    .equals("complete"));

//            WebElement csvButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCsv")));
            WebElement csvButton = driver.findElement(By.id("btnCsv"));
            csvButton.click();

            // 파일 다운로드 대기
            Thread.sleep(15000); // 15초 대기
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 발생", e);
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }

//    private void waitForFileDownload(Path downloadPath) throws InterruptedException {
//        int maxWaitTime = 200; // 최대 대기 시간
//        int waitedTime = 0;
//        while (!Files.exists(downloadPath) && waitedTime < maxWaitTime) {
//            log.info("Waiting for file to download...");
//            Thread.sleep(2000); // 2초 대기
//            waitedTime += 2;
//        }
//        if (Files.exists(downloadPath)) {
//            log.info("File downloaded successfully.");
//        } else {
//            log.warn("File download timed out.");
//        }
//    }

        private void waitForFileDownload(Path downloadPath) throws InterruptedException, IOException {
        int maxRetries = 500;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            if (Files.exists(downloadPath) && Files.size(downloadPath) > 0) {
                log.debug("########## 파일 다운로드 성공");
                return;
            }
            log.debug("##### 다운로드 대기 중... 시도 횟수: {}/{}", retryCount + 1, maxRetries);
            Thread.sleep(1000); // 1초 대기
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
            Files.move(sourcePath, destinationPath); // 파일 이동 시도
            log.debug("##### 파일이동 작업 실행");
        } catch (IOException e) {
            log.error("IOException occurred while moving file from {} to {}: {}",
                    sourcePath.toAbsolutePath(), destinationPath.toAbsolutePath(), e.getMessage(), e);
            throw e; // 상위로 예외 전달
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
