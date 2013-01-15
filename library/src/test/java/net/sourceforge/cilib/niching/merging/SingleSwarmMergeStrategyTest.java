/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.niching.NichingFunctionsTest;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class SingleSwarmMergeStrategyTest {

    @Test
    public void testSingleSwarmMerge() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());

        Particle p1 = NichingFunctionsTest.createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = NichingFunctionsTest.createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.6), Math.sqrt(0.6)));
        Particle p3 = NichingFunctionsTest.createParticle(new MinimisationFitness(2.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p4 = NichingFunctionsTest.createParticle(new MinimisationFitness(3.0), Vector.of(1.0, 1.0));

        pso1.getTopology().add(p1); pso2.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);

        SingleSwarmMergeStrategy merge = new SingleSwarmMergeStrategy();

        Assert.assertEquals(3, merge.f(pso2, pso1).getTopology().size());
        Assert.assertEquals(Topologies.getBestEntity(merge.f(pso2, pso1).getTopology()).getCandidateSolution(), Topologies.getBestEntity(pso2.getTopology()).getCandidateSolution());
    }
}
