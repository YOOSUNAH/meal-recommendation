package toy.ojm.infrastructure.csv_parser;

import lombok.Getter;

import java.util.List;

@Getter
public class CsvData {
    private final String managementNumber;
    private final String businessStatus;
    private final String number;
    private final String address;
    private final String roadAddress;
    private final String name;
    private final String category;
    private final Double longitude;
    private final Double latitude;

    public CsvData(List<String> columns) {
        this.managementNumber = removeDoubleQuote(columns.get(1));
        this.businessStatus = removeDoubleQuote(columns.get(7));
        this.number = removeDoubleQuote(columns.get(12));
        this.address = removeDoubleQuote(columns.get(15));
        this.roadAddress = removeDoubleQuote(columns.get(16));
        this.name = removeDoubleQuote(columns.get(18));
        this.category = removeDoubleQuote(columns.get(22));

        this.longitude = parseDouble(columns.get(23));
        this.latitude = parseDouble(columns.get(24));
    }

    private Double parseDouble(String value) {
        try {


            return Double.parseDouble(removeDoubleQuote(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String removeDoubleQuote(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.replaceAll("\"", "");
    }

    public boolean isClosedBusiness() {
        return businessStatus != null && businessStatus.contains("폐업");
    }


}
