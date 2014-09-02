package org.estatio;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DocumentFinderTest {


    @Test
    public void test() {
        DocumentFinder finder = new DocumentFinder("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM");
        assertThat(finder.getDocuments().size(), is(281));
    }

    @Test
    public void testFind() throws Exception {
        DocumentFinder finder = new DocumentFinder("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\CAR");
        assertNotNull(finder.find("CAR 2.E.20081024.7.1"));
    }
}
