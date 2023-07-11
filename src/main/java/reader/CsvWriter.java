package reader;

import com.opencsv.CSVWriter;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvWriter {

    private CsvWriter() {

    }

    public static void writeAndCompareCsvFile(String filePath1, String filePath2, String writeFilePath) {

        String[] headers = {"ID", "PositionId", "ISIN", "Quantity", "Total Price"};
        Map<String, List<String>> instrumentMap = CsvReader.useMap(filePath1);
        Map<String, List<String>> positionMap = CsvReader.useMap(filePath2);

        try {

            FileWriter fileWriter = new FileWriter(writeFilePath);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            List<String[]> rowValues = new ArrayList<>();
            rowValues.add(headers);
            csvWriter.writeAll(rowValues);

            List<String[]> listArr = new ArrayList<>();
            String[] intArr;
            int totalColumns = 0;
            int row = 1;
            int counter = 0;
            int position = 0;

            // This part fetch the required column information from instrumentMap and positionMap objects

            while(totalColumns < headers.length) {
                int totalRows = positionMap.get(headers[0]).size();
                String[] rowSplit;
                int currentCounter=counter;
                int currentPosition = position;

                while(row < totalRows && currentCounter < headers.length) {

                    if(instrumentMap.containsKey(headers[currentCounter])) {
                        List<String> listValues = instrumentMap.get(headers[currentCounter]);
                        rowSplit = listValues.toArray(new String[listValues.size()]);
                        listArr.add(currentPosition, rowSplit);
                        row++;
                        currentCounter++;
                        currentPosition++;
                    } else if(headers[currentCounter].equals("Total Price")) {
                        List<String> unitPrices = instrumentMap.get("Unit Price");
                        List<String> quantities = positionMap.get("Quantity");
                        intArr = new String[unitPrices.size()];
                        for(int i = 0; i < unitPrices.size(); i++) {
                            String unitPrice = unitPrices.get(i);
                            String quantity = quantities.get(i);
                            int totalPrice = 0;
                            if(unitPrice.length() > 0 && quantity.length() > 0) {
                                totalPrice = Integer.parseInt(unitPrice) * Integer.parseInt(quantity);
                            }
                            intArr[i] = String.valueOf(totalPrice);
                        }
                        listArr.add(currentPosition, intArr);
                        row++;
                        currentCounter++;
                        currentPosition++;

                    } else{
                        if(!positionMap.containsKey(headers[currentCounter])) {
                            headers[currentCounter] = "ID";
                        }
                        List<String> listValues = positionMap.get(headers[currentCounter]);
                        rowSplit = listValues.toArray(new String[listValues.size()]);
                        listArr.add(currentPosition, rowSplit);
                        if(headers[currentCounter].equals("ID")) {
                            headers[currentCounter] = "PositionId";
                        }
                        row++;
                        currentPosition++;
                        currentCounter++;
                    }
                    totalColumns++;
                    row = 1;
                    counter = currentCounter;
                    position = currentPosition;
                }
            }

            // This part update the information on csv file

            String[] writeString;
            String[] rowSize = listArr.get(0);
            int writeRowCount = 0;
            int writeCounter = 0;
            int writePosition = 0;
            for(int i=0; i<rowSize.length; i++) {
                writeString = new String[listArr.size()];
                while(writeRowCount < listArr.size()) {
                    String[] writeRow = listArr.get(writePosition);
                    writeString[writePosition] = writeRow[writeCounter];
                    writePosition++;
                    writeRowCount++;
                }
                writeRowCount=0;
                writePosition=0;
                writeCounter++;
                csvWriter.writeNext(writeString);
            }
            csvWriter.close();
            fileWriter.close();
            System.out.println("File write done successfully");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        // This part is compare the output file data to input files

        Map<String, List<String>> positionReportData = CsvReader.useMap(writeFilePath);
        SoftAssert softAssert = new SoftAssert();
        int outputActualColumnSize;
        int outputExpectedColumnSize;
        int outputColumns = 0;
        int outputCounter = 0;
        boolean status = false;


        while(outputColumns < headers.length ) {

            if(instrumentMap.containsKey(headers[outputCounter])) {
                outputExpectedColumnSize = instrumentMap.get(headers[outputCounter]).size();
                outputActualColumnSize = positionReportData.get(headers[outputCounter]).size();
                List<String> inputData = instrumentMap.get(headers[outputCounter]);
                List<String> outputData = positionReportData.get(headers[outputCounter]);
                for (int i=0; i< inputData.size(); i++) {
                    for (int j=i; j<=i; j++) {
                        if (inputData.get(i).equals(outputData.get(j))) {
                            status = true;
                        }
                        else {
                            System.out.println(inputData.get(i)+" column value not matched with " +outputData.get(j));
                        }
                    }
                }
                if (status) {
                    System.out.println("output data column name "+ "'"+ headers[outputCounter]+ "'"+ " matched with input data column "+ "'"+ headers[outputCounter]+ "'"+" input files");
                }
                softAssert.assertEquals(outputActualColumnSize, outputExpectedColumnSize, "Column size not matched");
                outputCounter++;
                outputColumns++;
            } else if(headers[outputCounter].equals("Total Price")) {
                List<String> unitPrices = instrumentMap.get("Unit Price");
                List<String> quantities = positionMap.get("Quantity");
                List<String> outputData = positionReportData.get(headers[outputCounter]);
                for(int i = 0; i < unitPrices.size(); i++) {
                    String unitPrice = unitPrices.get(i);
                    String quantity = quantities.get(i);
                    int totalPrice;
                    if(unitPrice.length() > 0 && quantity.length() > 0) {
                        totalPrice = Integer.parseInt(unitPrice) * Integer.parseInt(quantity);
                        int outputDataSum = Integer.parseInt(outputData.get(i));
                        if (totalPrice == outputDataSum) {
                            status = true;
                        }
                        else {
                            System.out.println("'Quantity' and 'unit price' sum of value not matched with output data");
                        }
                    }
                }
                if (status) {
                    System.out.println("output data column name "+ "'"+headers[outputCounter]+"'"+ " matched with input data columns 'Quantity' and 'unit price' input files");
                }
                outputCounter++;
                outputColumns++;
            } else {
                if (!positionMap.containsKey(headers[outputCounter])) {
                    headers[outputCounter] = "ID";
                }
                outputExpectedColumnSize = positionMap.get(headers[outputCounter]).size();
                outputActualColumnSize = positionReportData.get(headers[outputCounter]).size();
                List<String> inputData = positionMap.get(headers[outputCounter]);
                if(headers[outputCounter].equals("ID")) {
                    headers[outputCounter] = "PositionId";
                }
                List<String> outputData = positionReportData.get(headers[outputCounter]);
                for (int i=0; i< inputData.size(); i++) {
                    for (int j=i; j<=i; j++) {
                        if (inputData.get(i).equals(outputData.get(j))) {
                            status = true;
                        }
                        else {
                            System.out.println(inputData.get(i)+" column value not matched with " +outputData.get(j));
                        }
                    }
                }
                if (status) {
                    System.out.println("output data column name "+ "'"+ headers[outputCounter]+ "'"+ " matched with input data column "+ "'"+ headers[outputCounter]+ "'"+" input files");
                }
                softAssert.assertEquals(outputActualColumnSize, outputExpectedColumnSize, "Column size not matched");
                outputCounter++;
                outputColumns++;
            }
        }
    }

}
