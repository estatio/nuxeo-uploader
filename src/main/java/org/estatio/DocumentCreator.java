package org.estatio;

import java.io.File;
import java.nio.file.Files;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;

public class DocumentCreator {

    private Session session;
    private HttpAutomationClient client;

    public void connect() throws Exception {
        client = new HttpAutomationClient("http://ams-s-nuxeo02:8080/nuxeo/site/automation");
        session = client.getSession("Administrator", "Administrator");
        // Documents docs = (Documents)
        // session.newRequest("Document.Query").set(
        // "query", "SELECT * FROM Document").execute();
        // System.out.println(docs);

    }

    public void disconnect() {
        client.shutdown();
    }

    public void create(ImportDocument doc) throws Exception {
        if (session == null ){
            connect();
        }
 
        // Fetch the root of Nuxeo repository
        Document root = (Document) session.newRequest("Document.Fetch").set("value", "/Test/").execute();

        // Instantiate a new Document with the simple constructor
        Document document = new Document("myDocument", "File");
        document.set("dc:title", "My File");
        document.set("dc:description", "My Description");

        // Create a document of File type by setting the parameter 'properties'
        // with String metadata values delimited by comma ','
        document = (Document) session.newRequest("Document.Create").setHeader(Constants.HEADER_NX_SCHEMAS, "*").setInput(root).set("type", document.getType()).set("name", document.getId()).set("properties", document).execute();

        // Set another title to update
        document.set("dc:title", "New Title");

        // Update the document
        document = (Document) session.newRequest("Document.Update").setInput(document).set("properties", document).execute();
        
        
     // create a file document
        //session.newRequest("Document.Create").setInput(root).set("type", "File").set("name", "myfile").set("properties", "dc:title=My File").execute();
        File file = doc.getFile();
        FileBlob fb = new FileBlob(file);
        fb.setMimeType(Files.probeContentType(file.toPath()));
        // uploading a file will return null since we used HEADER_NX_VOIDOP
        session.newRequest("Blob.Attach").setHeader(
                Constants.HEADER_NX_VOIDOP, "true").setInput(fb)
                .set("document", document.getId()).execute();

    }

}
