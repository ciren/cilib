/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import org.junit.Test;

public class DataDependantPopulationInitialisationStrategyTest {

    /**
     * Test of setEntityType method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testSetEntityType() {
        ClusterParticle type  = new ClusterParticle();
        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setEntityType(type);

        Assert.assertEquals(instance.getEntityType(), type);
    }

    /**
     * Test of getEntityType method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testGetEntityType() {
        ClusterParticle type  = new ClusterParticle();
        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setEntityType(type);

        Assert.assertEquals(instance.getEntityType(), type);
    }

    /**
     * Test of initialise method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testInitialise() {
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5:5)");
        problem.setNumberOfClusters(3);
        problem.setDimension(4);
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.initialiseWindow();
        DataTable table = window.getCurrentDataset();

        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setDataset(table);
        instance.setEntityType(new ClusterParticle());
        instance.setEntityNumber(2);

        Iterable resultingTopology = instance.initialise(problem);

        boolean isCorrectType = false;
        int size = 0;
        int numberOfClusters = 0;
        int dimensions = 0;
        for(Object entity : resultingTopology) {
            if(entity instanceof ClusterParticle) {
                isCorrectType = true;
                ClusterParticle newEntity = (ClusterParticle) entity;
                numberOfClusters = newEntity.getCandidateSolution().size();
                dimensions = ((CentroidHolder) newEntity.getCandidateSolution()).get(0).size();
            }
            size++;
        }
        Assert.assertEquals(2, size);
        Assert.assertTrue(isCorrectType);
        Assert.assertEquals(3, numberOfClusters);
        Assert.assertEquals(4, dimensions);
    }

    /**
     * Test of getEntityNumber method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testGetEntityNumber() {
        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setEntityNumber(3);

        Assert.assertEquals(3, instance.getEntityNumber());
    }

    /**
     * Test of setEntityNumber method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testSetEntityNumber() {
        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setEntityNumber(3);

        Assert.assertEquals(3, instance.getEntityNumber());
    }

    /**
     * Test of setDelegate method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testSetDelegate() {
        PopulationInitialisationStrategy newDelegate = new ClonedPopulationInitialisationStrategy();
        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setDelegate(newDelegate);

        Assert.assertEquals(newDelegate, instance.getDelegate());
    }

    /**
     * Test of getDelegate method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testGetDelegate() {
        PopulationInitialisationStrategy newDelegate = new ClonedPopulationInitialisationStrategy();
        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setDelegate(newDelegate);

        Assert.assertEquals(newDelegate, instance.getDelegate());
    }

    /**
     * Test of getBounds method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testGetBounds() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.initialiseWindow();
        DataTable table = window.getCurrentDataset();

        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setDataset(table);

        ArrayList<ControlParameter[]> bounds = instance.getBounds();

        Assert.assertEquals(bounds.get(0)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(0)[1].getParameter(), 2.0);

        Assert.assertEquals(bounds.get(1)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(1)[1].getParameter(), 3.0);

        Assert.assertEquals(bounds.get(2)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(2)[1].getParameter(), 4.0);

        Assert.assertEquals(bounds.get(3)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(3)[1].getParameter(), 2.0);
    }

    /**
     * Test of setDataset method, of class DataDependantPopulationInitialisationStrategy.
     */
    @Test
    public void testSetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.initialiseWindow();
        DataTable table = window.getCurrentDataset();

        DataDependantPopulationInitialisationStrategy instance = new DataDependantPopulationInitialisationStrategy();
        instance.setDataset(table);

        Assert.assertEquals(table, instance.dataset);
    }
}
