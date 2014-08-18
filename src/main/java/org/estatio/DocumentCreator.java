package org.estatio;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Fidelity;
import javax.swing.text.html.parser.DTD;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Document;

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
        // Documents docs = (Documents)
        // session.newRequest("Document.Query").set(
        // "query", "SELECT * FROM Document").execute();
        // System.out.println(docs);
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
        x.add(new DocumentType("Department", department));
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
        if (document != null) {
            int x = parentList.size() -1;
            ArrayList<DocumentType> sub = new ArrayList<DocumentCreator.DocumentType>(parentList);
            sub.remove(sub.size()-1);
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

    public void create(ItalyTechnicalDocument doc, String rootPath) throws Exception {
        if (session == null) {
            connect();
        }
        Document root;
        // Fetch the root of Nuxeo repository
        System.out.println(rootPath);

        // Instantiate a new Document with the simple constructor
        Document document = new Document("myDocument", "File");
        document.set("dc:title", doc.getName());
        document.set("dc:description", "test description");
        Document loc = (Document) session.newRequest("Document.Fetch").set("value", "RPM").execute();
        document = (Document) session.newRequest("Document.Create")
                .setHeader(Constants.HEADER_NX_SCHEMAS, "*")
                .setInput(loc)
                .set("type", document.getType())
                .set("name", document.getId())
                .set("properties", document)
                .execute();

        System.out.print(document.getPath());
        // Create a document of File type by setting the parameter
        // 'properties' // with String metadata values delimited by comma ','
        document = (Document) session.newRequest("Document.Create")
                .setHeader(Constants.HEADER_NX_SCHEMAS, "*")
                .setInput(rootPath).set("type", document.getType()).set("name",
                        document.getId()).set("properties", document).execute();

        // Update the document document = (Document)
        session.newRequest("Document.Update").setInput(document)
                .set("properties", document).execute();

        // create a file document //File file = doc.getFile(); //FileBlob fb
        // = new FileBlob(file);
        // fb.setMimeType(Files.probeContentType(file.toPath())); // uploading
        // a file will return null since we used HEADER_NX_VOIDOP
        // session.newRequest("Blob.Attach") //
        // .setHeader(Constants.HEADER_NX_VOIDOP, "true") // .setInput(fb)
        // .set("document", document.getId()) // .execute();

    }

}
