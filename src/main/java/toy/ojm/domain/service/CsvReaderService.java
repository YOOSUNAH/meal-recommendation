package toy.ojm.domain.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    public static void main(String[] args) {
        readCSV();
    }
    public static void readCSV() {
        try {
            File file = new File("out/production/resources/csv-data/data.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "EUC-KR"));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> aLine = new ArrayList<>();
                String[] lineArr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                aLine = Arrays.asList(lineArr);
                System.out.println(aLine);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

