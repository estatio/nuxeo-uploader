package org.estatio;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class DocumentCreatorTest {

    private DocumentCreator cr;

    @Before
    public void setUp() throws Exception {
        cr = new DocumentCreator();
    }

    @Test
    public void test() throws Exception {
        cr.connect();
    }

    @Test
    public void test2() throws Exception {
        cr.create(new ImportDocument(new File("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM_Indice Archivio Tecnico ITA Unico.xls")));
    }

}
