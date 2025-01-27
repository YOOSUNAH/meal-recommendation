package toy.ojm.infrastructure.restaurant_openapi;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import toy.ojm.infrastructure.PublicDataConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicDataDownloader {

    private static final String SEOUL_PUBLIC_OPEN_DATA_URL = "https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do";
    private static final String DOWNLOAD_FILE_NAME = "서울시 일반음식점 인허가 정보.csv";

    @Value("${webdriver.browser-type:FIREFOX}")
    private String browser;

    @Transactional
    public Path downloadCsvFile() {
        log.info("downloadCsvFile 진행 - 현재 시간 : " + new Date().toString());

        Path downloadPath = resolveDownloadPath();
        Path destinationPath = resolveDestinationPath();
        WebDriver driver = null;

        log.info("downloadPath : {}", downloadPath );
        log.info("destinationPath : {}", destinationPath );

        try {
            driver = createWebDriver();
            log.info("########## 1111111111111111111111111111");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            log.info("########## 2222222222222222222222222222");
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            log.info("########## 3333333333333333333333333333");

            download(driver);
            log.info("########## 4444444444444444444444444444");
            waitForFileDownload(downloadPath);
            log.info("########## 5555555555555555555555555555");

            deleteIfExists(destinationPath, downloadPath);
            moveFile(downloadPath, destinationPath);


        } catch (SessionNotCreatedException e) {
            log.error("fail to create browser session: ", e);
        } catch (IOException | InterruptedException e) {
            log.error("Error downloading CSV file: ", e);
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
        Path downloadPath;
        if ("FIREFOX".equalsIgnoreCase(browser)) {
            downloadPath = Paths.get("/tmp", DOWNLOAD_FILE_NAME);
        } else {
            downloadPath = Paths.get(System.getProperty("user.home"), "Downloads", DOWNLOAD_FILE_NAME);        // 홈디렉토리 밑에 Downloads 폴더에 DOWNLOAD_FILE_NAME 이름을 가진 파일의 경로
            // user.home: 사용자의 홈 디렉토리
        }
        log.info("Resolved download path: {}", downloadPath.toAbsolutePath());
        return downloadPath;
    }

    private Path resolveDestinationPath() {
        Path directoryPath = Paths.get(System.getProperty("user.dir"), PublicDataConstants.DESTINATION_DIRECTORY);
        // user.dir: 현재 작업 디렉토리에, "현재실행디렉토리/csv-data" 경로를 생성
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            log.debug("########## Resolved destination directory path: {}", directoryPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("########## Failed to create directory", e);
        }

        Path destinationPath = directoryPath.resolve(destinationFilenameWithTime());
        log.debug("########## Resolved destination path: {}", destinationPath.toAbsolutePath());

        return destinationPath;   //directoryPath 특정 디렉토리의 경로를 나타내는 Path 객체, resolve() : 기존의 경로에 다른 경로를 결합하여 새로운 경로를 생성, destinationFilenameWithTime : 파일이름을 생성하는 메서드
        // csv-data 디렉토리 내에 시간을 포함한 고유한 파일 이름으로 새로운 파일 경로를 생성하고, 그 경로를 나타내는 Path 객체를 반환
    }

    private WebDriver createWebDriver() throws IOException {
        ChromeOptions options = new ChromeOptions();
//        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
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

    private void waitForFileDownload(Path downloadPath) throws InterruptedException, IOException {
        int maxRetries = 100;
        int retryCount = 0;

        while(retryCount < maxRetries){
            if (Files.exists(downloadPath) && Files.size(downloadPath) > 0) {
                log.info("########## 파일 다운로드 성공");
                return;
            }
            Thread.sleep(5000); // 5초 대기
            retryCount++;
        }

        throw new InterruptedException("########## 파일 다운로드 실패");
    }

    private static void deleteIfExists(Path destinationPath, Path downloadPath) {
        File downloadFile = destinationPath.toFile();
        if (downloadFile.exists()) {
            downloadFile.delete();
            log.debug("기존 다운로드 파일 삭제 완료: {}", downloadPath);
        }
    }

    private String destinationFilenameWithTime() {
        return PublicDataConstants.DESTINATION_FILE_NAME + "." + PublicDataConstants.DESTINATION_FILE_EXTENSION;  // 파일이름을 생성
    }

    private void moveFile(Path sourcePath, Path destinationPath) throws IOException {
        if (!Files.exists(sourcePath)) {
            log.warn("Source file not found: {}", sourcePath.toAbsolutePath());
            return;
        }

        try {
            Files.move(sourcePath, destinationPath); // 파일 이동 시도
            log.info("Move operation executed.");
        } catch (IOException e) {
            log.error("IOException occurred while moving file from {} to {}: {}",
                    sourcePath.toAbsolutePath(), destinationPath.toAbsolutePath(), e.getMessage(), e);
            throw e; // 상위로 예외 전달
        } catch (Exception e) {
            log.error("Unexpected error occurred during file move: {}", e.getMessage(), e);
            throw e;
        }

        if (Files.exists(destinationPath)) {
            log.info("CSV file moved successfully to {}", destinationPath.toAbsolutePath());
        } else {
            log.error("File move operation completed but destination file not found: {}", destinationPath.toAbsolutePath());
            throw new IOException("File move failed ");
        }

    }
}
