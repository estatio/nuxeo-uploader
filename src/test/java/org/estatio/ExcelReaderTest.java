package org.estatio;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class ExcelReaderTest extends TestCase {

    @Test
    public void testRead() throws Exception {

        ExcelReader reader = new ExcelReader();
        List<ItalyTechnicalDocument> list = reader.read();
        assertThat(list.size(), is(250));

    }
    
    @Test
    public void sdf() throws Exception {
        
    }

}
