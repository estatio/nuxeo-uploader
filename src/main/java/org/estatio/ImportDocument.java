package org.estatio;

import java.io.File;

import org.joda.time.LocalDate;

public class ImportDocument {

    public ImportDocument(String name, LocalDate date) {
        super();
        this.name = name;
        this.date = date;
    }
    
    public ImportDocument(File file) {
        this.file = file;
    }

    private String name;

    private LocalDate date;
    
    private File file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public File getFile() {
        return file;
    }
}
