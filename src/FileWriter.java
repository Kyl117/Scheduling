
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileWriter {

    public static void outputDemandTable(HashMap<String, Float> steelWeightTable) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Demand");

        int rowCount = 0;
        
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("钢种");
        cell = row.createCell(1);
        cell.setCellValue("需求");

        for (Map.Entry<String, Float> item : steelWeightTable.entrySet()) {
            if(item.getValue()>0){
            row = sheet.createRow(++rowCount);
            cell = row.createCell(0);
            cell.setCellValue(item.getKey());
            cell = row.createCell(1);
            cell.setCellValue(item.getValue());
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("Demand.xlsx")) {
            workbook.write(outputStream);
        }
        workbook.close();
    }

    public static void outputSchedule(ArrayList<Order> orderList)throws IOException{
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Schedule");

        int rowCount = 0;
        
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("钢种");
        cell = row.createCell(1);
        cell.setCellValue("需求");

        // for (Map.Entry<String, Float> item : steelWeightTable.entrySet()) {
        //     if(item.getValue()>0){
        //     row = sheet.createRow(++rowCount);
        //     cell = row.createCell(0);
        //     cell.setCellValue(item.getKey());
        //     cell = row.createCell(1);
        //     cell.setCellValue(item.getValue());
        //     }
        // }

        try (FileOutputStream outputStream = new FileOutputStream("Schedule.xlsx")) {
            workbook.write(outputStream);
        }
        workbook.close();
    }        
    
}
