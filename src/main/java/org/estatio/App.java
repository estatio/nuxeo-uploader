package org.estatio;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.nuxeo.ecm.automation.client.model.Document;

public class App
{

    public static void main(String[] args)
    { 
        boolean persist = true;

        String properties = "CAG";//,CAS,CRE,CUR,FAV,GIG,LAM,LEO,LUN,MCB,POR,RPC,RPG";
        // String properties = "RPG";
        // String properties = "LAM,LEO,RPG,RPM";

        for (String property : properties.split(",")) {
            long tStart = System.currentTimeMillis();
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(String.format("Start processing spreadsheet %s", property));
            String path = String.format("\\\\ams-s-storage\\storageroom\\A\\TechnicalArchive\\%1$s\\%1$s 2_Tecnico", property);
            String excelFile="";
            try{
                excelFile = String.format("\\\\ams-s-storage\\storageroom\\A\\TechnicalArchive\\%1$s\\%1$s 2_Tecnico\\%1$s_Indice Archivio Tecnico ITA Unico.xls", property);
            }catch(Exception e){
                if(excelFile==""){
                    try{
                        excelFile = String.format("\\\\ams-s-storage\\storageroom\\A\\TechnicalArchive\\%1$s\\%1$s 2_Tecnico\\%1$s_Indice Archivio Tecnico ITA Unico.xlsx", property);
                    }catch(Exception f){
                    }
                }
            }

            int countFilesAccessedMultipleTimes = 0;
            int countFilesNotFound = 0;
            int countFilesNotInExcel = 0;
            int countFilesImported = 0;
            int totalExcelRows = 0;
            int totalFiles = 0;

            try {
                DocumentFinder finder = new DocumentFinder(path);
                ExcelReader reader = new ExcelReader();
                List<ImportDocument> excelDocs = reader.read(excelFile);
                totalExcelRows = excelDocs.size();
                totalFiles = finder.getDocuments().size();
                DocumentCreator creator = new DocumentCreator("http://ams-s-nuxeo02.ecp.loc:8080/nuxeo/site/automation", "lbos", "Chosentj33h");
                for (ImportDocument doc : excelDocs) {
                    // System.out.print(".");
                    File file = null;
                    String note = (String) doc.getProperty("def:Note");
                    List<ImportDocument> fileDocs = finder.findAll(doc.getName().concat("."));
                    if (fileDocs.size() == 0) {
                        if (note != null && !note.contains("manca")&& !note.contains("vedi")&& !doc.getName().contains("jpg")&&!doc.getName().contains("img")) {
                            System.out.println(String.format("[%s] not found on file system. Note: [%s]", doc.getName(), note != null ? note : ""));
                            countFilesNotFound++;
                            continue;
                        }
                    }

                    Document nuxeoDoc = null;

                    if (persist) {
                        nuxeoDoc = creator.create(doc);
                        countFilesImported++;
                        if(fileDocs.size()>1){
                            creator.attach(nuxeoDoc, fileDocs.get(0));
                            fileDocs.get(0).setProcessed(true);
                            for(int i = 1; i<fileDocs.size();i++){
                                creator.attachMore(nuxeoDoc, fileDocs.get(i));
                                fileDocs.get(i).setProcessed(true);
                            }
                        }else if(fileDocs.size()==1){
                            creator.attach(nuxeoDoc, fileDocs.get(0));
                            fileDocs.get(0).setProcessed(true);
                        }
                       
                    }
                    for(ImportDocument fileDoc : fileDocs){
                        fileDoc.setProcessed(true);
                    }
                }
                System.out.println("");
                for (ImportDocument fileDoc : finder.getDocuments()) {
                    if (!fileDoc.isProcessed()&&!fileDoc.getName().contains(".JPG")&&!fileDoc.getName().contains("Thumbs.db")&&!fileDoc.getName().contains("jpg")&&!fileDoc.getName().contains("img")) {
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
