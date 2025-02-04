package toy.ojm.asynch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.global.ResponseDto;
import toy.ojm.infrastructure.csv_parser.CsvReaderService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm/async")
public class AsyncController {

    private final CsvReaderService csvReaderService;

    @Async
    @GetMapping("/csv")
    public void csvReadAndSave() {
        csvReaderService.readAndSaveCSV();
        log.info("##### csv 읽어오기 완료");
    }

}




// test용
//    private final AsyncService asyncService;
//    @GetMapping("/async")
//    public ResponseEntity<Void> temp(){
//
//        log.info("controller start");
//        asyncService.AsyncMethod();
//        log.info("controller end");
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }