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
package net.sourceforge.cilib.entity.initialization;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.TextFileReader;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class DataPatternInitializationStrategyTest {
    
    public DataPatternInitializationStrategyTest() {
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
     * Test of initialize method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        ARFFFileReader reader = new ARFFFileReader();
        reader.setSourceURL("src/test/resources/datasets/clusters.arff");
        instance.setDataTableBuilder(new DataTableBuilder(reader));
        
        StandardParticle particle = new StandardParticle();
        particle.setCandidateSolution(Vector.of(0,0,0));
        instance.initialize(EntityType.CANDIDATE_SOLUTION, particle);
        Vector result = (Vector) particle.getCandidateSolution();
        System.out.println("Result: " + result.toString());
        ClusterCentroid centroidResult = new ClusterCentroid();
        centroidResult.addAll(result);
        
        DataTableBuilder tableBuilder = new DataTableBuilder(reader);
        tableBuilder.addDataOperator(new TypeConversionOperator());
        tableBuilder.addDataOperator(new PatternConversionOperator());
        try {
            tableBuilder.buildDataTable();
            
        } catch (CIlibIOException ex) {
            Logger.getLogger(DataClusteringPSO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DataTable dataset = tableBuilder.getDataTable();
        
        CentroidHolder datasetHolder = new CentroidHolder();
        for(int i = 0; i < dataset.size(); i++) {
            ClusterCentroid centroid = new ClusterCentroid();
            centroid.addAll(((StandardPattern) dataset.getRow(i)).getVector());
            datasetHolder.add(centroid);
        }
        
        Assert.assertTrue(datasetHolder.contains(centroidResult));
    }

    /**
     * Test of setDataTableBuilder method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testSetDataTableBuilder() {
        System.out.println("setDataTableBuilder");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        instance.setDataTableBuilder(new DataTableBuilder(new TextFileReader()));
        
        Assert.assertTrue(instance.getDataTableBuilder().getDataReader() instanceof TextFileReader);
    }

    /**
     * Test of getDataset method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testGetDataset() {
        System.out.println("getDataset");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        instance.setDataTableBuilder(new DataTableBuilder(new TextFileReader()));
        
        Assert.assertTrue(instance.getDataTableBuilder().getDataReader() instanceof TextFileReader);
    }

    /**
     * Test of setProbabilityDistribution method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testSetProbabilityDistribution() {
        System.out.println("setProbabilityDistribution");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        instance.setProbabilityDistribution(new CauchyDistribution());
        
        Assert.assertTrue(instance.getProbabilityDistribution() instanceof CauchyDistribution);
    }

    /**
     * Test of getProbabilityDistribution method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testGetProbabilityDistribution() {
        System.out.println("getProbabilityDistribution");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        instance.setProbabilityDistribution(new CauchyDistribution());
        
        Assert.assertTrue(instance.getProbabilityDistribution() instanceof CauchyDistribution);
    }

    /**
     * Test of setPatternConversionOperator method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testSetPatternConversionOperator() {
        System.out.println("setPatternConversionOperator");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        PatternConversionOperator operator = new PatternConversionOperator();
        instance.setPatternConversionOperator(operator);
        
        Assert.assertSame(operator, instance.getPatternConversionOperator());
    }

    /**
     * Test of getPatternConversionOperator method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testGetPatternConversionOperator() {
        System.out.println("getPatternConversionOperator");
        DataPatternInitializationStrategy instance = new DataPatternInitializationStrategy();
        PatternConversionOperator operator = new PatternConversionOperator();
        instance.setPatternConversionOperator(operator);
        
        Assert.assertSame(operator, instance.getPatternConversionOperator());
    }
}
