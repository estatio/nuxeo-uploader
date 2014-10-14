package org.estatio;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Document;

public class NuxeoFindDocumentTest extends TestCase {

    private Session session;
    private HttpAutomationClient client;
    private String url;
    private String username;
    private String password;

    @Before
    public void setUp() throws Exception {
        url="http://ams-s-nuxeo01.ecp.loc:8080/nuxeo/site/automation";
        username="Administrator";
        password="Administrator";
        connect();
    }
    
    @After
    public void tearDown() throws Exception {
        disconnect();
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

    @Test
    public void testFindFailsButExists() throws Exception {
        assertNotNull(find("/Test2"));

    }

    @Test
    public void testFindSucceeds() throws Exception {
        assertNotNull(find("/Technical Archive"));
    }
    @Test
    public void testFindFails() throws Exception {
        assertNull(find("/Testasdf"));

    }

}