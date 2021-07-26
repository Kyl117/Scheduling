import java.io.*;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class FileReader {

    private ArrayList<ArrayList<String>> records = new ArrayList<>();
    // private static List<String> getRecordFromLine(String line) {
    // List<String> values = new ArrayList<String>();
    // Scanner rowScanner = new Scanner(line);
    // rowScanner.useDelimiter(",");
    // while (rowScanner.hasNext()) {
    // values.add(rowScanner.next());
    // }
    // rowScanner.close();
    // return values;
    // }

    // public void loadfile(String fileName) throws FileNotFoundException {
    // Scanner scanner = new Scanner(new File(fileName));
    // while (scanner.hasNextLine()) {
    // records.add(getRecordFromLine(scanner.nextLine()));
    // }

    // scanner.close();
    // }

    public void loadfile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(fileName));
        // creating workbook instance that refers to .xls file
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        // creating a Sheet object to retrieve the object
        HSSFSheet sheet = wb.getSheetAt(0);
        // evaluating cell type
        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        for (Row row : sheet) {
            records.add(new ArrayList<String>());
        }
        int count = 0;
        for (Row row : sheet) // iteration over row using for each loop
        {
            for (Cell cell : row) // iteration over cell using for each loop
            {
                switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type
                        // getting the value of the cell as a number
                        records.get(count).add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case Cell.CELL_TYPE_STRING: // field that represents string cell type
                        // getting the value of the cell as a string
                        records.get(count).add(cell.getStringCellValue());
                        break;
                }
            }
            count++;
        }
        wb.close();
    }

    public ArrayList<ArrayList<String>> getRecords(){
        return records;
    }
}