package toy.ojm.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.ojm.infrastructure.csv_parser.CsvReaderService;
import toy.ojm.infrastructure.restaurant_openapi.PublicDataDownloader;

@Service
@RequiredArgsConstructor
public class Scheduling {

    private final PublicDataDownloader publicDataDownloader;
    private final CsvReaderService csvReaderService;

    @Transactional
    @Scheduled(cron = "0 55 23 * * *")
    public void schedulingService(){
        publicDataDownloader.downloadCsvFile();
        csvReaderService.readAndSaveCSV();
    }

}
