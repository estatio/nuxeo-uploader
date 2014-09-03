package org.estatio;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.automation.client.model.Document;

public class DocumentCreatorTest {

    private static final String TEST_ROOT = "/Test";
    private static final String TEST_COUNTRY = "Italy";
    private static final String TEST_PROPERTY = "RPM";
    private static final String TEST_SUBJECT = "A";
    private static final String TEST_FILE2 = "\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM 2.A_Provenienza\\RPM 2.A.20001206_Atto Compravendita\\RPM 2.A.20001206.1.1_atto di compravendita.pdf";
    private static final String TEST_FILE = "\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM_Indice Archivio Tecnico ITA Unico.xls";
    private DocumentCreator cr;
    private List<ImportDocument> list;
    private ImportDocument doc;
    private ImportDocument doc2;

    @Before
    public void setUp() throws Exception {
        cr = new DocumentCreator("http://ams-s-nuxeo02:8080/nuxeo/site/automation", "Administrator", "Administrator");
        cr.connect();
        cr.delete(TEST_ROOT + TEST_COUNTRY);
        doc2 = new ImportDocument(new File(TEST_FILE2));
        doc = new ImportDocument(new File(TEST_FILE));
        doc.setName("Test");
        doc.addProperty("def:Property", "RPM");
        doc.addProperty("def:Department", "2");
        doc.addProperty("def:Subject", "A");
        doc.addProperty("def:SubSubject", null);
        
        
        doc.addProperty("def:Date", new LocalDate(2011,1,1));
        doc.addProperty("dc:title", " n° CAR 2.E.19971217");
        doc.addProperty("def:Note", "Note");
        doc.addProperty("dc:expired", new LocalDate(2100,1,1));
        doc.addProperty("def:Cadastral", "318.69.687"); // Das mooi!
        doc.addProperty("dc:description", "Description");
        doc.addProperty("def:DocumentNumber", "999");
        doc.addProperty("def:Asset", "Asset");
        doc.addProperty("def:DocumentKind", "");
        doc.addProperty("def:Format", "Format");
        doc.addProperty("def:DataRoom", "DataRoom");
        doc.addProperty("def:Location", "Location");
        doc.addProperty("def:Department", "A");
        doc.addProperty("def:Subject", "Technical");
        doc.addProperty("def:SubSubject", "SubSubject");
        doc.addProperty("def:Brand", "Brand");

        
        
    }

    @After
    public void tearDown() throws Exception {
        cr.disconnect();
    }

    @Test
    public void test1() throws Exception {
        assertNotNull(cr.find(TEST_ROOT));
    }

    @Test
    public void test2() throws Exception {
        cr.findOrCreate(TEST_ROOT, TEST_COUNTRY, "Country");
    }

    @Test
    public void test3() throws Exception {
        cr.findOrCreateParent("Test", "Italy", "RPM", "2", "A", null);
    }

    @Test
    public void test5() throws Exception {
        cr.findOrCreate(TEST_ROOT + "/" + TEST_COUNTRY, TEST_PROPERTY, "Property");
    }
    
    @Test 
    public void test6() throws Exception {
        Document nuxeoDoc = cr.create(doc);
        cr.attachMore(nuxeoDoc, doc);
        cr.attach(nuxeoDoc, doc);
        assertNotNull(nuxeoDoc);
        

    }
    
}
