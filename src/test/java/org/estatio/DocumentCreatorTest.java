package org.estatio;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class DocumentCreatorTest {

    private static final String TEST_ROOT = "/Test/";
    private static final String TEST_FILE = "\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM_Indice Archivio Tecnico ITA Unico.xls";
    private DocumentCreator cr;

    @Before
    public void setUp() throws Exception {
        cr = new DocumentCreator("http://ams-s-nuxeo02:8080/nuxeo/site/automation", "Administrator", "Administrator");
    }

    @Test
    public void test() throws Exception {
        cr.connect();
    }

    @Test
    public void test2() throws Exception {
        cr.create(new ItalyTechnicalDocument(new File(TEST_FILE)), TEST_ROOT);
    }

}
