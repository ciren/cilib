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
import net.sourceforge.cilib.problem.solution.MaximisationFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.Matchers.hasItem;

/**
 *
 */
public class ElitistSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        ElitistSelector<Integer> selection = new ElitistSelector<Integer>();
        selection.on(elements).select();
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        ElitistSelector<Integer> selection = new ElitistSelector<Integer>();
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
        topology.get(0).getProperties().put(EntityType.FITNESS, new MinimisationFitness(99.0));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MinimisationFitness(8.0));
        topology.get(2).getProperties().put(EntityType.FITNESS, new MinimisationFitness(9.0));

        ElitistSelector<Individual> selection = new ElitistSelector<Individual>();
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(1)));
    }

    @Test
    public void maximisationSelection() {
        Topology<Individual> topology = createDummyTopology();
        topology.get(0).getProperties().put(EntityType.FITNESS, new MaximisationFitness(99.0));
        topology.get(1).getProperties().put(EntityType.FITNESS, new MaximisationFitness(8.0));
        topology.get(2).getProperties().put(EntityType.FITNESS, new MaximisationFitness(9.0));

        ElitistSelector<Individual> selection = new ElitistSelector<Individual>();
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.get(0)));
    }

    @Test
    public void elitistSelection() {
        List<Integer> elements = Lists.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        ElitistSelector<Integer> selection = new ElitistSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(9));
    }
}
