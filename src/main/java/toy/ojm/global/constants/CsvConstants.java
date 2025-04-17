package toy.ojm.global.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvConstants {
    // 파일 저장 관련 상수
    public static final Path DESTINATION_DIRECTORY = Paths.get(
            System.getProperty("user.dir"),
            "csv-data"
    );
    public static final String DESTINATION_FILE_NAME = "data";
    public static final String DESTINATION_FILE_EXTENSION = "csv";

    // 데이터 처리 관련 상수
    public static final int BATCH_SIZE = 1000;
}
