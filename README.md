# csv write automation project


# Description

This project runs the java code to write csv file

## How do I get set up?

* Install JAVA JDK 11
* Install [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download)
* Install TestNG Plugin for IntelliJ (should come by default with the IDE)
* Install maven in local machine (Recommended to use the latest version)


## Architecture

The application packages are organized by application that is being tested, e.g.:

```diagram
📦 main
  📦 java
      ┣ 📂 reader

📦 test
  📦 app
      ┣ 📂 in
      ┣ 📂 out

  📦 java
      ┣ 📂 CsvWriterTest
      
```

### Using Testng

Right-click on the testng.xml with the tests you want to run found in the test/resources folder of the project. Then
select run/debug.

### Using Maven
mvn install  (This command will install dependencies and run the test)

### Reports
Once the test run completed, we can find the new csv file under the out folder which placed app sub folder.
Path = (test -> app -> out -> PositionReport - Sheet1.csv)

### Common Classes
CsvReader - This class used to read a values from given csv file.
CsvWriter - This class used to write a values to destination csv file.

