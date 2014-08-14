package org.estatio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

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

            if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue() == 1) {

                String fileName = String.format(
                        "%s %s.%s.%s.%s",
                        stringOfCell(row.getCell(0)),
                        stringOfCell(row.getCell(1)),
                        stringOfCell(row.getCell(2)),
                        stringOfCell(row.getCell(3)),
                        stringOfCell(row.getCell(4)));
                LocalDate date = null;
                
                String dateStr = stringOfCell(row.getCell(3)).concat("0101").substring(0, 8);
                try {
                    date = LocalDate.parse(dateStr, DateTimeFormat.forPattern("yyyyMMdd"));
                } catch (Exception e) {
                    System.out.println(String.format("Cannot parse [%s]", dateStr));
                }

                ItalyTechnicalDocument doc = new ItalyTechnicalDocument(fileName, date);
                doc.addProperty("def:Property", stringOfCell(row.getCell(0)));
                doc.addProperty("def:Date", date);
                doc.addProperty("dc:title", fileName);
                doc.addProperty("def:Note", stringOfCell(row.getCell(24)));
                doc.addProperty("def:Cadastral", "");
                doc.addProperty("dc:description", stringOfCell(row.getCell(8)));
                doc.addProperty("def:DocumentNumber", stringOfCell(row.getCell(18)));
                doc.addProperty("def:Asset", stringOfCell(row.getCell(9)));
                doc.addProperty("def:DocumentKind", stringOfCell(row.getCell(7)));
                doc.addProperty("def:Format", stringOfCell(row.getCell(20)));
                doc.addProperty("def:DataRoom", stringOfCell(row.getCell(25)));
                doc.addProperty("dc:Location", stringOfCell(row.getCell(23)));
                doc.addProperty("def:Department", stringOfCell(row.getCell(1)));
                doc.addProperty("def:Subject", stringOfCell(row.getCell(2)));
                doc.addProperty("def:SubSubject", "");
                doc.addProperty("def:DepartmentSubject", stringOfCell(row.getCell(1)) + "/" + stringOfCell(row.getCell(2)));
                doc.addProperty("type",returnFileType(row.getCell(5)));
                doc.addProperty("name", fileName);
                doc.addProperty("def:Brand", stringOfCell(row.getCell(10)));

                documents.add(doc);

            }
        }
        return documents;
    }

    private String returnFileType(Cell cell) {
        return cell == null ? "ECP_CONTAINER" : "ECP_FILE";

    }

    private String stringOfCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();

        // DataFormatter fmt = new DataFormatter();
        // CellReference cr = new CellReference(cell);
        // return fmt.formatCellValue(cell);
    }

    private Double numericOfCell(Cell cell) {
        return cell == null ? null : cell.getNumericCellValue();
    }

    private Date dateOfCell(Cell cell) {
        return cell == null ? null : cell.getDateCellValue();
    }

}
