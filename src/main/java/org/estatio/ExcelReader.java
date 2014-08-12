package org.estatio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.LocalDate;

public class ExcelReader {

    public List<ItalyTechnicalDocument> read() throws InvalidFormatException, IOException {

        List<ItalyTechnicalDocument> documents = new ArrayList<ItalyTechnicalDocument>();

        InputStream inp = new FileInputStream("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM_Indice Archivio Tecnico ITA Unico.xls");

        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);

        Iterator<Row> itr = sheet.rowIterator();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(5);
            
            if (cell != null && cell.getCellType()== Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue() == 1) {

                String fileName = String.format(
                        "%s %s.%s.%s.%s",
                        row.getCell(0).toString(),
                        row.getCell(1).toString(),
                        row.getCell(2).toString(),
                        row.getCell(3).toString(),
                        row.getCell(4).toString());
                LocalDate date = null;

                try {
                    date = new LocalDate(row.getCell(3).toString().concat("0101").substring(0, 7));
                } catch (Exception e) {
                    // iets met e
                }

                documents.add(new ItalyTechnicalDocument(fileName, date));
            }
        }

        return documents;
    }
}
