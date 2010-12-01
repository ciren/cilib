package net.cilib.algorithm;

import com.google.common.collect.Lists;
import net.cilib.algorithm.SimulationBuilder.PopulationBasedSimulationBuilder;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.inject.SimulationScope;
import net.cilib.main.MockProblem;
import net.cilib.measurement.Measurement;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class SimulationBuilderTest {

    @Test(expected = NullPointerException.class)
    public void populationBuilderWithoutAlgorithm() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .using(null);
    }


    @Test(expected = NullPointerException.class)
    public void populationBuilderWithoutProblem() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .on(null);
    }

    @Test(expected = NullPointerException.class)
    public void populationBuilderWithoutTopology() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .initialTopology(null);
    }

    @Test(expected=NullPointerException.class)
    public void populationBuilderWithoutMeasurements() {
        new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .measuredBy(null);
    }

    @Test(expected=NullPointerException.class)
    public void populationBuilderClearsMembersAfterBuild() {
        PopulationBasedSimulationBuilder builder = new SimulationBuilder(new SimulationScope())
                .newPopulationBasedSimulation()
                .using(new DE(null, null, null, null))
                .on(new MockProblem())
                .initialTopology(ImmutableGBestTopology.of())
                .measuredBy(Lists.<Measurement>newArrayList());

        builder.build(); // Creates the instance
        builder.build(); // Throws the exception -> Builders are invalid after use.
    }
}