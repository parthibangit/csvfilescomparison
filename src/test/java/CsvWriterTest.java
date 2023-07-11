
import org.testng.annotations.Test;
import reader.CsvWriter;

public class CsvWriterTest {


    @Test
    public void csvWrite() {
        String path = "src/test/app/in/InstrumentDetails - Sheet1.csv";
        String path1 = "src/test/app/in/PositionDetails - Sheet1.csv";
        String endPath = "src/test/app/out/PositionReport - Sheet1.csv";

        CsvWriter.writeAndCompareCsvFile(path, path1, endPath);

    }

}
