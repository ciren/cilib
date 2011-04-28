package net.cilib.matchers;

import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.CandidateSolution;
import net.cilib.entity.FitnessComparator;
import net.cilib.entity.Individual;
import org.junit.Assert;
import org.junit.Test;
import static fj.data.Option.some;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 */
public class EntityMatchersTest {

    @Test
    public void mostFitEntity() {
        Individual best = new Individual(CandidateSolution.empty(), some(4.0));
        Individual worst = new Individual(CandidateSolution.empty(), some(1.0));
        Topology<Individual> topology = ImmutableGBestTopology.<Individual>topologyOf(best, worst);

        Option<Individual> result = EntityMatchers.mostFit(topology, FitnessComparator.MAX);
        Assert.assertThat(result.some(), is(best));
    }
}
