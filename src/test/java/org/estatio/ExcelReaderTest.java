package org.estatio;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import junit.framework.TestCase;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class ExcelReaderTest extends TestCase {

    private ExcelReader reader;

    private List<ImportDocument> list;

    @Before
    public void setUp() throws Exception {
        reader = new ExcelReader();
        list = reader.read("\\\\ams-s-storage\\storageroom\\DeleteByUser\\Marc\\TechnicalArchive\\RPM\\RPM 2_Tecnico\\RPM_Indice Archivio Tecnico ITA Unico.xls");
    }

    @Test
    public void testRead() throws Exception {

        assertThat(list.size(), is(250));

    }

    @Test
    public void testDate() throws Exception {
        assertThat((LocalDate) list.get(0).getProperty("def:Date"), is(new LocalDate(2000, 12, 6)));

    }

    @Test
    public void testDump() throws Exception {
        System.out.println(list.get(0).dumpProperties());
    }

}
