package toy.ojm.infrastructure.restaurant_openapi;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import toy.ojm.infrastructure.PublicDataConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicDataDownloader {

    private static final String SEOUL_PUBLIC_OPEN_DATA_URL = "https://data.seoul.go.kr/dataList/OA-16094/S/1/datasetView.do";
    private static final String DOWNLOAD_FILE_NAME = "서울시 일반음식점 인허가 정보.csv";

    @Transactional
    public Path downloadCsvFile() {
        log.info("downloadCsvFile 진행 - 현재 시간 : " + new Date().toString());

        Path downloadPath = resolveDownloadPath();
        Path destinationPath = resolveDestinationPath();

        WebDriverManager.chromedriver().setup();

        // Chrome 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");                 // Headless 모드 : 웹 브라우저를 열지 않고 크롤링을 진행할 때 사용하는 옵션
        options.addArguments("--no-sandbox");              // Chrome 브라우저의 샌드박스 기능을 비활성화하는 옵션, 리눅스에서 셀레니움이 적절히 동작하지 않을 때 사용
        options.addArguments("--disable-dev-shm-usage");   // 공유 메모리 파일 시스템 크기를 제한하지 않게 설정


        WebDriver driver = new ChromeDriver(options);

        try {
            download(driver); // 데이터 크롤링
            waitForFileDownload(downloadPath);

            // 저장할 위치에, 기존 파일이 존재하면 삭제
            File downloadFile = destinationPath.toFile();
            if (downloadFile.exists()) {
                downloadFile.delete();
                log.debug("기존 다운로드 파일 삭제 완료: {}", downloadPath);
            }

            moveFile(downloadPath, destinationPath);  // downloadPath 에서 destinationPath으로 파일 위치 이동

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
        // 홈디렉토리 밑에 Downloads 폴더에 DOWNLOAD_FILE_NAME 이름을 가진 파일의 경로
        // user.home: 사용자의 홈 디렉토리
    }

    private Path resolveDestinationPath() {
            Path directoryPath = Paths.get(System.getProperty("user.dir"), PublicDataConstants.DESTINATION_DIRECTORY);
            // user.dir: 현재 작업 디렉토리에, "현재실행디렉토리/csv-data" 경로를 생성
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            log.debug("## Resolved destination directory path: {}", directoryPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("## Failed to create directory", e);
        }

        Path destinationPath = directoryPath.resolve(destinationFilenameWithTime());
        log.debug("## Resolved destination path: {}", destinationPath.toAbsolutePath());

        return destinationPath;   //directoryPath 특정 디렉토리의 경로를 나타내는 Path 객체, resolve() : 기존의 경로에 다른 경로를 결합하여 새로운 경로를 생성, destinationFilenameWithTime : 파일이름을 생성하는 메서드
        // csv-data 디렉토리 내에 시간을 포함한 고유한 파일 이름으로 새로운 파일 경로를 생성하고, 그 경로를 나타내는 Path 객체를 반환
    }

    private String destinationFilenameWithTime() {
        return PublicDataConstants.DESTINATION_FILE_NAME + "." + PublicDataConstants.DESTINATION_FILE_EXTENSION;  // 파일이름을 생성
    }

    private void waitForFileDownload(Path downloadPath) throws InterruptedException {
        int maxWaitTime = 200; // 최대 대기 시간
        int waitedTime = 0;
        while (!Files.exists(downloadPath) && waitedTime < maxWaitTime) {
            log.info("Waiting for file to download...");
            Thread.sleep(2000); // 2초 대기
            waitedTime += 2;
        }
        if (Files.exists(downloadPath)) {
            log.info("File downloaded successfully.");
        } else {
            log.warn("File download timed out.");
        }
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
