package toy.ojm.domain.service;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvToExcelService {

    // csv -> excel
    public void changeCSVToExcel() throws Exception {
        // CSV 파일이 있는 디렉토리 경로
        String dataDir = "out/production/resources/csv-data/";
        // CSV 파일 열기
        // CSV LoadOptions 객체 생성
        LoadOptions loadOptions = new LoadOptions(FileFormatType.CSV);
        // CSV 파일 경로 및 loadOptions를 사용하여 통합 문서 개체 만들기
        Workbook workbook = new Workbook(dataDir + "data.csv",
            loadOptions);
        workbook.save(dataDir + "dataExcel.xlsx", SaveFormat.XLSX);
        log.info("CSV 파일이 Excel로 변환되었습니다.");
    }
}
