package reader;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvReader {

    private CsvReader() {

    }

    public static Map<String, List<String>> useMap(String filePath) {

        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] headers = csvReader.peek();
            List<String[]> rows = csvReader.readAll();

            Map<String, List<String>> map = new LinkedHashMap<>();


            int totalColumns = 0;
            int row = 1;
            int counter = 0;

            while ( totalColumns < headers.length) {

                int totalRows = rows.size();
                List<String> list = new ArrayList<>();

                while (row < totalRows) {
                    String[] rowValues = rows.get(row);
                    String rowValue = rowValues[counter];
                    list.add(rowValue);
                    map.put(headers[totalColumns], list);
                    row++;
                }

                totalColumns++;
                row = 1;
                counter++;

            }

            return map;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
