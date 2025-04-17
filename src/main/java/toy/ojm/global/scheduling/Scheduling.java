package toy.ojm.global.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.ojm.infra.RestaurantCsvReader;
import toy.ojm.infra.RestaurantDataCrawler;

@Service
@RequiredArgsConstructor
public class Scheduling {
    private final RestaurantDataCrawler restaurantDataCrawler;
    private final RestaurantCsvReader restaurantCsvReader;

    @Transactional
    @Scheduled(cron = "0 30 23 * * *")
    public void schedulingService(){
        restaurantDataCrawler.downloadCsvFile();
        restaurantCsvReader.readAndSaveCSV();
    }
}
