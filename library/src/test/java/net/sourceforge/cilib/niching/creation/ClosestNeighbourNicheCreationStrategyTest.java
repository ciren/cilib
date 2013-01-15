/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import fj.data.List;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.niching.NichingFunctionsTest;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class ClosestNeighbourNicheCreationStrategyTest {

    @Test
    public void testCreation() {
        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(1.0, 1.0));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0));

        PSO pso = new PSO();
        pso.getTopology().addAll(Arrays.asList(p1, p2, p3));

        ClosestNeighbourNicheCreationStrategy creator = new ClosestNeighbourNicheCreationStrategy();
        creator.setSwarmBehavior(new ParticleBehavior());
        NichingSwarms swarms = creator.f(NichingSwarms.of(pso, List.<PopulationBasedAlgorithm>nil()), p1);

        Assert.assertEquals(1, swarms._1().getTopology().size());
        Assert.assertEquals(Vector.of(2.0, 2.0), swarms._1().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(2, swarms._2().head().getTopology().size());
        Assert.assertEquals(Vector.of(0.0, 1.0), swarms._2().head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(Vector.of(1.0, 1.0), swarms._2().head().getTopology().get(1).getCandidateSolution());
    }
}
