package application;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel {
	String sciezka;
	public Excel(String sciezka) {
		this.sciezka = sciezka;
	}
	
	public boolean createSheet(ArrayList<piastTag> tags) throws IOException {
        // Creating Workbook instances 
		boolean stan = false;
        Workbook wb = new HSSFWorkbook(); 
  
        // An output stream accepts output bytes and sends them to sink. 
        OutputStream fileOut = new FileOutputStream(sciezka); 
          
        // Creating Sheets using sheet object 
        Sheet sheet1 = wb.createSheet("Komentarz");
        for (int i =0;i<tags.size();i++){
        	piastTag tag = tags.get(i);
            Row row = sheet1.createRow(i); 
            
            // Specific cell number nazwa 
            Cell cell = row.createCell(0); 
            // putting value at specific position 
            cell.setCellValue(tag.nazwa); 
         // Specific cell number nazwa 
            cell = row.createCell(1); 
            // putting value at specific position 
            cell.setCellValue(tag.komunikacja);
         // Specific cell number nazwa 
            cell = row.createCell(2); 
            // putting value at specific position 
            cell.setCellValue(tag.typ);
         // Specific cell number nazwa 
            cell = row.createCell(3); 
            // putting value at specific position 
            cell.setCellValue(tag.adres);
         // Specific cell number nazwa 
            cell = row.createCell(4); 
            // putting value at specific position 
            cell.setCellValue(tag.opis);
            
        }

          

  
        wb.write(fileOut); 
        stan = true;
        return stan;
    } 
		
	

}
