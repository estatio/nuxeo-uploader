package org.estatio;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DocumentCreatorTest {

    private static final String TEST_ROOT = "/Test";
    private static final String TEST_COUNTRY = "Italy";
    private static final String TEST_PROPERTY = "RPM";
    private static final String TEST_SUBJECT = "A";

    private static final String TEST_FILE = "\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM_Indice Archivio Tecnico ITA Unico.xls";
    private DocumentCreator cr;
    private List<ImportDocument> list;
    private ImportDocument doc;

    @Before
    public void setUp() throws Exception {
        cr = new DocumentCreator("http://ams-s-nuxeo02:8080/nuxeo/site/automation", "Administrator", "Administrator");
        cr.connect();
        cr.delete(TEST_ROOT + TEST_COUNTRY);
        doc = new ImportDocument(new File(TEST_FILE));
        doc.setName("Test");
        doc.addProperty("def:Property", "RPM");
        doc.addProperty("def:Department", "2");
        doc.addProperty("def:Subject", "A");
        doc.addProperty("def:SubSubject", null);
        
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
    public void test4() throws Exception {
        cr.findOrCreate(TEST_ROOT + "/" + TEST_COUNTRY, TEST_PROPERTY, "Property");
    }
    
    @Test 
    public void test5() throws Exception {
        cr.create(doc);
    }

}
