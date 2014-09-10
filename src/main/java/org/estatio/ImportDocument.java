package org.estatio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;

import org.joda.time.LocalDate;

public class ImportDocument {

    class DocProperty {

        private String field;
        private Object value;

        public String getField() {
            return field;
        }

        public Object getValue() {
            return value;
        }

        public DocProperty(String field, Object value) {
            super();
            this.field = field;
            this.value = value;
        }

    }

    private String name;
    private String id;
    private LocalDate date;
    private File file;
    private boolean processed;
    private List<DocProperty> properties;

    public ImportDocument(String name, LocalDate date) {
        super();
        this.name = name;
        this.date = date;
        this.properties = new ArrayList<ImportDocument.DocProperty>();
    }

    public ImportDocument(File file) {
        this.file = file;
        this.name = file.getName();
        this.properties = new ArrayList<ImportDocument.DocProperty>();
    }

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

    public void setFile(File file) {
        this.file = file;
    }

    public void addProperty(String field, Object value) {
        properties.add(new DocProperty(field, value));
    }

    public Object getProperty(String field) {
        for (DocProperty prop : properties) {
            if (prop.field.equals(field)) {
                return prop.value;
            }
        }
        return null;

    }

    public List<DocProperty> getProperties() {
        return properties;
    }

    String dumpProperties() {
        StringBuilder bld = new StringBuilder();
        for (DocProperty property : properties) {
            bld.append(property.field);
            bld.append(":");
            bld.append(property.value == null ? "<EMPTY>" : property.value.toString());
            bld.append(System.getProperty("line.separator"));
        }
        return bld.toString();
    }

    public void setProcessed(boolean processed) {
        if (processed == this.processed){
            throw new IllegalArgumentException(String.format("[%s] is already processed", getName()));
        }
        this.processed = processed;
    }

    public boolean isProcessed() {
        return this.processed;
    }

}
