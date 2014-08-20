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
            if (file.isFile()) {
                documents.add(new ImportDocument(file));
            }
        }
    }

    public ImportDocument find(final String name) {
        for (ImportDocument doc : documents) {
            if (doc.getName().contains(name)) {
                return doc;
            }
        }
        return null;
    }

    public List<ImportDocument> getDocuments() {
        return documents;
    }

}
