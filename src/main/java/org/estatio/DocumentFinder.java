package org.estatio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DocumentFinder {

    private List<ItalyTechnicalDocument> files;

    public DocumentFinder(String path) {
        files = new ArrayList<ItalyTechnicalDocument>();
        Collection<File> listFiles = FileUtils.listFiles(new File(path), null, true);
        Iterator<File> itr = listFiles.iterator();
        while (itr.hasNext()) {
            File file = itr.next();
            if (file.isFile()) {
                files.add(new ItalyTechnicalDocument(file));
            }
        }
    }

    public void find(final String pattern) {

    }

    public List<ItalyTechnicalDocument> getFiles() {
        return files;
    }

}
