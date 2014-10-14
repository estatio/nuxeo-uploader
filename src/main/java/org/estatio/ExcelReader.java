package org.estatio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class ExcelReader {

    public List<ImportDocument> read(String filePath) throws InvalidFormatException, IOException {

        List<ImportDocument> documents = new ArrayList<ImportDocument>();
        InputStream inp;
        try {
            inp = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            inp = new FileInputStream(filePath.concat("x"));
        }

        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);

        Iterator<Row> itr = sheet.rowIterator();
        while (itr.hasNext()) {
            Row row = itr.next();
            Cell cell = row.getCell(4);
            Cell cell5 = row.getCell(5);
            if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue() > 0 || cell5 != null && cell5.getCellType() == Cell.CELL_TYPE_NUMERIC && cell5.getNumericCellValue() > 0) {

                String fileName = String.format(
                        "%s %s.%s.%s.%s",
                        stringOfCell(row.getCell(0)),
                        stringOfCell(row.getCell(1)),
                        stringOfCell(row.getCell(2)),
                        stringOfCell(row.getCell(3)),
                        stringOfCell(row.getCell(4))).replace("..", ".");
                LocalDate date = null;

                String dateStr = StringUtils.substring(stringOfCell(row.getCell(3)).concat("0101"), 0, 8);
                try {
                    date = LocalDate.parse(dateStr, DateTimeFormat.forPattern("yyyyMMdd"));
                } catch (Exception e) {
                    // System.out.println(String.format("Cannot parse [%s] in file [%s]",
                    // dateStr, fileName));
                }

                String departmentSubject =
                        stringOfCell(row.getCell(1))
                                .concat("/" + StringUtils.substring(stringOfCell(row.getCell(2)), 0, 2))
                                .concat(stringOfCell(row.getCell(2)).length() > 2 ? "/" + stringOfCell(row.getCell(2)) : "");

                ImportDocument doc = new ImportDocument(fileName, date);
                doc.addProperty("def:Property", stringOfCell(row.getCell(0)));
                doc.addProperty("def:Date", date);
                doc.addProperty("dc:title", fileName);
                doc.addProperty("def:Note", stringOfCell(row.getCell(24)).replace("\n", " "));
                doc.addProperty("dc:expired", getExpirationDate(row));
                doc.addProperty("def:Cadastral", getCadastrals(row));
                doc.addProperty("dc:description", stringOfCell(row.getCell(8)));
                doc.addProperty("def:DocumentNumber", stringOfCell(row.getCell(18)));
                doc.addProperty("def:Asset", stringOfCell(row.getCell(9)));
                doc.addProperty("def:DocumentKind", stringOfCell(row.getCell(7)));
                doc.addProperty("def:Format", stringOfCell(row.getCell(20)));
                doc.addProperty("def:DataRoom", stringOfCell(row.getCell(25)));
                doc.addProperty("def:Location", stringOfCell(row.getCell(23)));

                doc.addProperty("def:DepartmentSubject", departmentSubject);

                doc.addProperty("def:Department", stringOfCell(row.getCell(1)));
                doc.addProperty("def:Subject", StringUtils.substring(stringOfCell(row.getCell(2)), 0, 2));
                doc.addProperty("def:SubSubject", stringOfCell(row.getCell(2)).length() > 2 ? stringOfCell(row.getCell(2)) : null);

                doc.addProperty("def:Brand", stringOfCell(row.getCell(10)));
                try {
                    System.out.println(doc.getProperty("dc:Expired").toString());
                } catch (Exception e) {

                }
                documents.add(doc);
            }
        }
        return documents;
    }

    private String getCadastrals(Row row) {
        String cadastral = "";
        if (stringOfCell(row.getCell(14)).contains("-")) {
            String[] strings = stringOfCell(row.getCell(14)).split("-");
            for (String s : strings) {
                cadastral += stringOfCell(row.getCell(12)) + "." + stringOfCell(row.getCell(13)) + "." + s + "|";
            }
        }
        else if (!stringOfCell(row.getCell(12)).equals("")) {
            cadastral += stringOfCell(row.getCell(12)) + "." + stringOfCell(row.getCell(13)) + "." + stringOfCell(row.getCell(14));
        }
        return cadastral;
    }

    private LocalDate getExpirationDate(Row row) {
        String expirationString = "";
        LocalDate expirationDate = null;
        if (stringOfCell(row.getCell(28)) != "" && row.getCell(28) != null) {
            expirationString += stringOfCell(row.getCell(28));
            expirationString += getDate(row);
            expirationString += getDay(row);
            try {
                expirationDate = LocalDate.parse(expirationString, DateTimeFormat.forPattern("yyyyMMdd"));
                return expirationDate;
            } catch (Exception e) {

            }
        }
        return null;
    }

    private String getDate(Row row) {
        if (stringOfCell(row.getCell(27)) != "") {
            if (stringOfCell(row.getCell(27)).length() == 2) {
                return stringOfCell(row.getCell(27));
            }
            if (stringOfCell(row.getCell(27)).length() < 2) {
                return "0" + stringOfCell(row.getCell(27));
            }
        }
        return "01";
    }

    private String getDay(Row row) {
        if (stringOfCell(row.getCell(26)) != "") {
            if (stringOfCell(row.getCell(26)).length() == 2) {
                return stringOfCell(row.getCell(26));
            }
            if (stringOfCell(row.getCell(26)).length() < 2) {
                return "0" + stringOfCell(row.getCell(26));
            }
        }
        return "01";
    }

    private String returnFileType(Cell cell) {
        return cell == null ? "ECP_CONTAINER" : "ECP_FILE";

    }

    private String stringOfCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue().trim().replace("\n", " ");

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
