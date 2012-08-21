/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.clustering;

import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SlidingWindowTest {
    
    public SlidingWindowTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of slideWindow method, of class SlidingWindow.
     */
    @Test
    public void testSlideWindow() {
        System.out.println("slideWindow");
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets\\iris2.arff");
        window.setWindowSize(1);
        window.setFrequency(1);
        window.initializeWindow();
        
        Vector beforeSlide =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expectedBeforeSlide = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(beforeSlide.containsAll(expectedBeforeSlide));
        
        window.slideWindow();
        
        Vector afterSlide =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expectedAfterSlide = Vector.of(2.0,3.0,4.0,2.0);
        
        System.out.println(afterSlide.toString());
        System.out.println(expectedAfterSlide.toString());
        Assert.assertTrue(afterSlide.containsAll(expectedAfterSlide));
    }

    /**
     * Test of initializeWindow method, of class SlidingWindow.
     */
    @Test
    public void testInitializeWindow() {
        System.out.println("initializeWindow");
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets\\iris2.arff");
        window.setWindowSize(2);
        window.initializeWindow();
        
        int totalPatternsInWindow = window.getCurrentDataset().size();
        Vector firstValue =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector secondValue =  ((StandardPattern) window.getCurrentDataset().getRow(1)).getVector();
        
        Vector expectedValue1 = Vector.of(1.0,1.0,1.0,2.0);
        Vector expectedValue2 = Vector.of(2.0,3.0,4.0,2.0);
        
        Assert.assertEquals(2, totalPatternsInWindow);
        Assert.assertTrue(firstValue.containsAll(expectedValue1));
        Assert.assertTrue(secondValue.containsAll(expectedValue2));
    }

    /**
     * Test of setWindowSize method, of class SlidingWindow.
     */
    @Test
    public void testSetWindowSize() {
        System.out.println("setWindowSize");
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(2);
        Assert.assertEquals(2, window.getWindowSize());
    }

    /**
     * Test of getWindowSize method, of class SlidingWindow.
     */
    @Test
    public void testGetWindowSize() {
        System.out.println("getWindowSize");
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(2);
        Assert.assertEquals(2, window.getWindowSize());
    }

    /**
     * Test of getDataTableBuilder method, of class SlidingWindow.
     */
    @Test
    public void testGetDataTableBuilder() {
        System.out.println("getDataTableBuilder");
        SlidingWindow window = new SlidingWindow();
        window.setDataTableBuilder(new DataTableBuilder(new DelimitedTextFileReader()));
        Assert.assertTrue(window.getDataTableBuilder().getDataReader() instanceof DelimitedTextFileReader);
    }

    /**
     * Test of setDataTableBuilder method, of class SlidingWindow.
     */
    @Test
    public void testSetDataTableBuilder() {
        System.out.println("setDataTableBuilder");
        SlidingWindow window = new SlidingWindow();
        window.setDataTableBuilder(new DataTableBuilder(new DelimitedTextFileReader()));
        Assert.assertTrue(window.getDataTableBuilder().getDataReader() instanceof DelimitedTextFileReader);
    }

    /**
     * Test of getSourceURL method, of class SlidingWindow.
     */
    @Test
    public void testGetSourceURL() {
        System.out.println("getSourceURL");
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets");
        Assert.assertEquals("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets", window.getSourceURL());
    }

    /**
     * Test of setSourceURL method, of class SlidingWindow.
     */
    @Test
    public void testSetSourceURL() {
        System.out.println("setSourceURL");
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets");
        Assert.assertEquals("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets", window.getSourceURL());
    }

    /**
     * Test of getPatternConversionOperator method, of class SlidingWindow.
     */
    @Test
    public void testGetPatternConversionOperator() {
        System.out.println("getPatternConversionOperator");
        SlidingWindow window = new SlidingWindow();
        PatternConversionOperator operator = new PatternConversionOperator();
        window.setPatternConversionOperator(operator);
        Assert.assertSame(operator, window.getPatternConversionOperator());
    }

    /**
     * Test of setPatternConversionOperator method, of class SlidingWindow.
     */
    @Test
    public void testSetPatternConversionOperator() {
        System.out.println("setPatternConversionOperator");
        SlidingWindow window = new SlidingWindow();
        PatternConversionOperator operator = new PatternConversionOperator();
        window.setPatternConversionOperator(operator);
        Assert.assertSame(operator, window.getPatternConversionOperator());
    }

    /**
     * Test of getCurrentDataset method, of class SlidingWindow.
     */
    @Test
    public void testGetCurrentDataset() {
        System.out.println("getCurrentDataset");
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets\\iris2.arff");
        window.setWindowSize(1);
        window.initializeWindow();
        
        int totalPatterns = window.getCurrentDataset().size();
        Vector result =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expected = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(result.containsAll(expected));
        Assert.assertEquals(1, totalPatterns);
    }

    /**
     * Test of getCompleteDataset method, of class SlidingWindow.
     */
    @Test
    public void testGetCompleteDataset() {
        System.out.println("getCompleteDataset");
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("C:\\Users\\Kristina\\Work\\cilib_kgerogieva\\src\\test\\resources\\datasets\\iris2.arff");
        window.setWindowSize(1);
        window.initializeWindow();
        
        int totalPatterns = window.getCurrentDataset().size();
        Vector result =  ((StandardPattern) window.getCurrentDataset().getRow(0)).getVector();
        Vector expected = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(result.containsAll(expected));
        Assert.assertEquals(1, totalPatterns);
    }
}
