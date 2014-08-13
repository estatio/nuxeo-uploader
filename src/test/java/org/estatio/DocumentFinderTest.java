package org.estatio;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DocumentFinderTest {

    @Test
    public void test() {
        DocumentFinder finder = new DocumentFinder("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM");
        assertThat(finder.getFiles().size(), is(281));
    }

}
