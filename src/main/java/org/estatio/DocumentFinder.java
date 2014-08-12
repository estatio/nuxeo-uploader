package org.estatio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DocumentFinder {

    private String path;

    private List<ImportDocument> files;

    public DocumentFinder(String path) {
        files = new ArrayList<ImportDocument>();
        this.path = path;
        Collection<File> listFiles = FileUtils.listFiles(new File(path), null, true);
        Iterator<File> itr = listFiles.iterator();
        while (itr.hasNext()) {
            File file = itr.next();
            if (file.isFile()) {
                files.add(new ImportDocument(file));
            }
        }
    }

    public void find(final String pattern) {

    }

    public List<ImportDocument> getFiles() {
        return files;
    }

}
