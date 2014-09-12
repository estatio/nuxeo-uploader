package org.estatio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DocumentFinder {

    private List<ImportDocument> documents;

    public DocumentFinder(String path) {
        documents = new ArrayList<ImportDocument>();
        
        
        
        Collection<File> listFiles = FileUtils.listFiles(new File(path), null, true);
        Iterator<File> itr = listFiles.iterator();
        while (itr.hasNext()) {
            File file = itr.next();
            if (file.isFile() && !file.getName().startsWith(".") && !file.getName().contains("Indice Archivio Tecnico ITA")) {
                documents.add(new ImportDocument(file));
            }
        }
    }

    public List<ImportDocument> findAll(final String name) {
        List<ImportDocument> result = new ArrayList<ImportDocument>();
        // fix wrong excel entries
        if (!"RPG 2.D..;RPG 2.E..".contains(name)) {
            String excelName = name
                    .replace("  ", " ").replace("..", ".");
            for (ImportDocument doc : documents) {
                // fix wrong filenames
                String fileName = doc.getName().replaceAll("  ", " ").replaceAll("^GGI", "GIG").replaceAll("^GAG", "CAG").replace(". ", "");

                if (fileName.contains(excelName)||fileName.contains(excelName.replaceAll("^CRE", "CUR"))||fileName.contains(excelName.replaceAll("^LAM", "FAB"))) {
                    result.add(doc);
                }
            }
        }
        return result;
    }

    public List<ImportDocument> getDocuments() {
        return documents;
    }

}
