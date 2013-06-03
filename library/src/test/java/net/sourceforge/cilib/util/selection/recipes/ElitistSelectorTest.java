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
import net.sourceforge.cilib.entity.Property;
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

    private static fj.data.List<Individual> createDummyTopology() {
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        Individual individual3 = new Individual();
       
        return fj.data.List.list(individual1, individual2, individual3);
    }

    @Test
    public void minimisationSelection() {
        fj.data.List<Individual> topology = createDummyTopology();
        topology.index(0).put(Property.FITNESS, new MinimisationFitness(99.0));
        topology.index(1).put(Property.FITNESS, new MinimisationFitness(8.0));
        topology.index(2).put(Property.FITNESS, new MinimisationFitness(9.0));

        ElitistSelector<Individual> selection = new ElitistSelector<Individual>();
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.index(1)));
    }

    @Test
    public void maximisationSelection() {
        fj.data.List<Individual> topology = createDummyTopology();
        topology.index(0).put(Property.FITNESS, new MaximisationFitness(99.0));
        topology.index(1).put(Property.FITNESS, new MaximisationFitness(8.0));
        topology.index(2).put(Property.FITNESS, new MaximisationFitness(9.0));

        ElitistSelector<Individual> selection = new ElitistSelector<Individual>();
        Individual selected = selection.on(topology).select();

        Assert.assertThat(selected, is(notNullValue()));
        Assert.assertThat(topology, hasItem(selected));
        Assert.assertThat(selected, is(topology.index(0)));
    }

    @Test
    public void elitistSelection() {
        List<Integer> elements = Lists.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        ElitistSelector<Integer> selection = new ElitistSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(9));
    }
}
