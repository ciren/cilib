/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.util.selection.weighting.EntityWeighting;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Tests to test the behavior of RouletteWheelSelection, in both the minimisation
 * and maximisation cases.
 * </p>
 */
public class RouletteWheelSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        RouletteWheelSelector<Integer> selection = new RouletteWheelSelector<Integer>();
        selection.on(elements).select();
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        RouletteWheelSelector<Integer> selection = new RouletteWheelSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(1));
    }

    private static Topology<Individual> createDummyTopology() {
        Topology<Individual> topology = new GBestTopology<Individual>();
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        Individual individual3 = new Individual();
        topology.add(individual1);
        topology.add(individual2);
        topology.add(individual3);
        return topology;
    }

    @Test
    public void minimisationSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MinimisationFitness(10000.0));
        topology.get(2).getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.00001)); // Should be the best entity

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));

        Assert.assertThat(selected, is(topology.get(2)));
    }

    @Test
    public void maximisationSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MaximisationFitness(90000.0)); // Should be the best entity
        topology.get(2).getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(1)));
    }

    @Test
    public void someNaNSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        topology.get(1).getProperties().put(EntityType.FITNESS, new MaximisationFitness(90000.0)); // Should be the best entity
        topology.get(2).getProperties().put(EntityType.FITNESS, new MaximisationFitness(0.5));

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(1)));
    }

    @Test
    public void allNaNSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        topology.get(1).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        topology.get(2).getProperties().put(EntityType.FITNESS, InferiorFitness.instance());

        RouletteWheelSelector<Individual> selection = new RouletteWheelSelector<Individual>(new EntityWeighting());
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
    }
}
