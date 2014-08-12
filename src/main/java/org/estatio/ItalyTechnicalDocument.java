package org.estatio;

import java.io.File;

import org.joda.time.LocalDate;

public class ItalyTechnicalDocument {

    public ItalyTechnicalDocument(String name, LocalDate date) {
        super();
        this.name = name;
        this.date = date;
    }
    
    public ItalyTechnicalDocument(File file) {
        this.file = file;
        this.name = file.getName();
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
