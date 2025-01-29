package toy.ojm.infrastructure;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PublicDataConstants {

    public static final Path DESTINATION_DIRECTORY = Paths.get(
        System.getProperty("user.dir"),
        "csv-data"
    );
    public static final String DESTINATION_FILE_NAME = "data";
    public static final String DESTINATION_FILE_EXTENSION = "csv";
}
