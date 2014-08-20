package org.estatio;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class App
{
    public static void main(String[] args)
    {
        String properties = "CAG,CAR,CAS,CRE,CUR,FAV,GIG,LAM,LEO,LUN,MCB,POR,RPC,RPG,RPM";
        // String properties = "CAR";
        // String properties = "LAM,LEO,RPG,RPM";

        
        for (String property : properties.split(",")) {
            long tStart = System.currentTimeMillis();
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(String.format("Start processing spreadsheet %s", property));
            String path = String.format("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\%1$s\\%1$s 2_Tecnico", property);
            String excelFile = String.format("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\%1$s\\%1$s 2_Tecnico\\%1$s_Indice Archivio Tecnico ITA Unico.xls", property);
            int countFilesAccessedMultipleTimes = 0;
            int countFilesNotFound = 0;
            int countFilesNotInExcel = 0;
            int countFilesImported = 0;
            int totalExcelRows = 0;
            int totalFiles = 0;

            try {
                DocumentFinder finder = new DocumentFinder(path);
                ExcelReader reader = new ExcelReader();
                List<ImportDocument> docs = reader.read(excelFile);
                totalExcelRows = docs.size();
                totalFiles = finder.getDocuments().size();
                DocumentCreator creator = new DocumentCreator("http://ams-s-nuxeo02:8080/nuxeo/site/automation", "Administrator", "Administrator");
                for (ImportDocument doc : docs) {
                    File file = null;
                    String note = (String) doc.getProperty("def:Note");
                    ImportDocument fileDoc = finder.find(doc.getName().concat("."));
                    if (fileDoc == null) {
                        if (note != null && !note.contains("manca")) {
                            System.out.println(String.format("[%s] not found on file system. Note: [%s]", doc.getName(), note != null ? note : ""));
                            countFilesNotFound++;
                            continue;
                        }
                    } else {
                        if (fileDoc.isProcessed()) {
                            System.out.println(String.format("[%s] accessed multiple times!", doc.getName()));
                            countFilesAccessedMultipleTimes++;
                            continue;
                        }
                        file = fileDoc.getFile();
                        fileDoc.setProcessed(true);
                    }
                    doc.setFile(file);
                    countFilesImported++;
                    creator.create(doc);
                }
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println(String.format("Start processing files %s", property));
                for (ImportDocument fileDoc : finder.getDocuments()) {
                    if (!fileDoc.isProcessed()) {
                        System.out.println(String.format("[%s] is found on filesytem but is not in spreadsheet", fileDoc.getName()));
                        countFilesNotInExcel++;
                    }
                }
            } catch (InvalidFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;
            
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(String.format("Summary for %s:", property));
            System.out.println(String.format("TotalExcelRows            : %d", totalExcelRows));
            System.out.println(String.format("TotalFiles                : %d", totalFiles));
            System.out.println(String.format("FilesImported             : %d", countFilesImported));
            System.out.println(String.format("FilesAccessedMultipleTimes: %d", countFilesAccessedMultipleTimes));
            System.out.println(String.format("FilesNotFound             : %d", countFilesNotFound));
            System.out.println(String.format("FilesNotInExcel           : %d", countFilesNotInExcel));
            System.out.println(String.format("Process time              : %f seconds", elapsedSeconds));

        }
    }
}
