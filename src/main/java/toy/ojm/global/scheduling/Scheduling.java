package toy.ojm.global.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.ojm.csv.CsvProcessor;
import toy.ojm.csv.CsvCrawler;

@Service
@RequiredArgsConstructor
public class Scheduling {
    private final CsvCrawler csvCrawler;
    private final CsvProcessor csvProcessor;

    @Transactional
    @Scheduled(cron = "0 30 23 * * *")
    public void schedulingService(){
        csvCrawler.downloadCsvFile();
        csvProcessor.readAndSaveCSV();
    }
}
