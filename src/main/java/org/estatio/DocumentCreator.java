package org.estatio;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.poifs.storage.BlockList;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;

import org.estatio.ImportDocument.DocProperty;

public class DocumentCreator {

    private Session session;
    private HttpAutomationClient client;
    private String url;
    private String username;
    private String password;

    public DocumentCreator(String url, String username, String password) {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() throws Exception {
        client = new HttpAutomationClient(url);
        session = client.getSession(username, password);
    }

    public void disconnect() {
        client.shutdown();
    }

    public Document find(String path) {
        try {
            return (Document) session.newRequest("Document.Fetch")
                    .setHeader(Constants.HEADER_NX_SCHEMAS, "*")
                    .set("value", path)
                    .execute();
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(String path) {
        Document document = find(path);
        if (document != null) {
            try {
                session.newRequest("Document.Delete").setInput(document).execute();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Document findOrCreateParent(String root, String country, String property, String department, String subject, String subSubject) {
        List<DocumentType> x = new ArrayList<DocumentType>();
        x.add(new DocumentType("Domain", root));
        x.add(new DocumentType("Country", country));
        x.add(new DocumentType("Property", property));
        x.add(new DocumentType("Subject", department));
        x.add(new DocumentType("Subject", subject));
        if (subSubject != null) {
            x.add(new DocumentType("Subject", subSubject));
        }
        return findOrCreateParent(x);
    }

    public Document findOrCreateParent(ImportDocument document) {
        String property = (String) document.getProperty("def:Property");
        String department = (String) document.getProperty("def:Department");
        String subject = (String) document.getProperty("def:Subject");
        String subSubject = (String) document.getProperty("def:SubSubject");
        List<DocumentType> x = new ArrayList<DocumentType>();
        x.add(new DocumentType("Domain", "ECPIT Technical Archive"));
        x.add(new DocumentType("Country", "Italy"));
        x.add(new DocumentType("Property", property));
        x.add(new DocumentType("Subject", department));
        x.add(new DocumentType("Subject", subject));
        if (subSubject != null) {
            x.add(new DocumentType("Subject", subSubject));
        }
        return findOrCreateParent(x);
    }

    private Document findOrCreateParent(List<DocumentType> parentList) {
        StringBuilder bldr = new StringBuilder();
        for (int j = 0; j <= parentList.size() - 1; j++) {
            bldr.append("/").append(parentList.get(j).getName());
        }
        Document document = find(bldr.toString());
        if (document == null) {
            int x = parentList.size() - 1;
            ArrayList<DocumentType> sub = new ArrayList<DocumentCreator.DocumentType>(parentList);
            sub.remove(sub.size() - 1);
            Document ownParent = findOrCreateParent(sub);
            document = findOrCreate(ownParent.getPath(), parentList.get(x).getName(), parentList.get(x).getType());
        }
        return document;
    }

    private class DocumentType {
        private String type;
        private String name;

        public DocumentType(String type, String name) {
            super();
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    public Document findOrCreate(String path, String name, String type) {
        Document root = find(path);
        Document property = null;
        if (find(path + "/" + name) == null) {
            try {
                property = (Document) session.newRequest("Document.Create")
                        .setHeader(Constants.HEADER_NX_SCHEMAS, "*")
                        .setInput(root)
                        .set("type", type)
                        .set("name", name)
                        .set("properties", "dc:title=" + name)
                        .execute();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return property;
    }

    public Document create(ImportDocument doc) throws Exception {
        if (session == null) {
            connect();
        }

        Document parent = findOrCreateParent(doc);
        // Instantiate a new Document with the simple constructor
        Document document = new Document(doc.getName(), "ECP_file");
        for (DocProperty prop : doc.getProperties()) {
            if (prop.getValue() != null) {
                document.set(prop.getField(), prop.getValue().toString());
            }
        }
        document = (Document) session.newRequest("Document.Create")
                .setHeader(Constants.HEADER_NX_SCHEMAS, "*")
                .setInput(parent)
                .set("type", document.getType())
                .set("name", document.getId())
                .set("properties", document)
                .execute();

        return document;
    }

    public Document attach(Document document, ImportDocument doc) throws Exception {
        // create a file document //
        File file = doc.getFile();

        
        if (file != null) {
            FileBlob fb = new FileBlob(file);
            fb.setMimeType(Files.probeContentType(file.toPath()));
            session.newRequest("Blob.Attach")
                    .setHeader(Constants.HEADER_NX_VOIDOP, "true")
                    .setInput(fb)
                    .set("document", document.getId())
                    .execute();
        }
        return document;
    }
//    public Document attachMore(Document document, ImportDocument doc) throws Exception {
//        // create a file document //
//        File file = doc.getFile();
//        if (file != null) {
//            FileBlob fb = new FileBlob(file);
//            fb.setMimeType(Files.probeContentType(file.toPath()));
//            session.newRequest("BlobHolder.Attach")
//                    .setHeader(Constants.HEADER_NX_VOIDOP, "true")
//                    .setInput(fb)
//                    .set("file", file)
//                    .execute();
//        }
//        return document;
//    }
}
