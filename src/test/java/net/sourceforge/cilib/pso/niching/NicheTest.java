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
package net.sourceforge.cilib.pso.niching;

import fj.P2;
import fj.data.List;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.niching.merging.RadiusOverlapMergeDetection;
import net.sourceforge.cilib.pso.niching.merging.SingleSwarmMergeStrategy;
import net.sourceforge.cilib.pso.niching.merging.StandardMergeStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class NicheTest {
    @Test
    public void testNormalMerge() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        PSO pso3 = new PSO();

        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());
        pso3.setTopology(new GBestTopology<Particle>());

        Particle p1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3) + Maths.EPSILON, Math.sqrt(0.3)));
        Particle p4 = createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 0.0));
        Particle p5 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));

        pso1.getTopology().add(p1); pso1.getTopology().add(p2);
        pso2.getTopology().add(p3); pso2.getTopology().add(p4);
        pso3.getTopology().add(p5);
        
        Niche.Swarms merged = Niche.merge(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(Niche.Swarms.of(pso3, List.list((PopulationBasedAlgorithm) pso1, pso2)));

        Assert.assertEquals(1, merged._1().getTopology().size());
        Assert.assertEquals(1, merged._2().length());
        Assert.assertEquals(4, merged._2().head().getTopology().size());
    }
    
    @Test
    public void testSingleAbsorption() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        PSO pso3 = new PSO();
        PSO pso4 = new PSO();

        pso1.setTopology(new GBestTopology<Particle>());
        pso1.setTopology(new GBestTopology<Particle>());
        pso3.setTopology(new GBestTopology<Particle>());
        pso4.setTopology(new GBestTopology<Particle>());

        Particle p1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p4 = createParticle(new MinimisationFitness(3.0), Vector.of(Maths.EPSILON, Maths.EPSILON));
        Particle p5 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));

        pso1.getTopology().addAll(Arrays.asList(p1, p2));
        pso2.getTopology().add(p3);
        pso3.getTopology().add(p4);
        pso4.getTopology().add(p5);
        
        P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> merged = Niche.absorbSingleSwarm(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(Niche.Swarms.of(pso1, List.list((PopulationBasedAlgorithm) pso2, pso3, pso4)));

        Assert.assertEquals(4, merged._2().getTopology().size());
        Assert.assertEquals(1, merged._1().getTopology().size());
        Assert.assertEquals(Vector.of(10.0, 0.0), merged._1().getTopology().get(0).getCandidateSolution());
    }
    
    @Test
    public void testAbsorption() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        PSO pso3 = new PSO();
        PSO pso4 = new PSO();

        pso1.setTopology(new GBestTopology<Particle>());
        pso1.setTopology(new GBestTopology<Particle>());
        pso3.setTopology(new GBestTopology<Particle>());
        pso4.setTopology(new GBestTopology<Particle>());

        Particle p1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3) + Maths.EPSILON, Math.sqrt(0.3)));
        Particle p4 = createParticle(new MinimisationFitness(3.0), Vector.of(Maths.EPSILON, Maths.EPSILON));
        Particle p5 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));
        Particle p6 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        pso1.getTopology().addAll(Arrays.asList(p1, p2));
        pso2.getTopology().add(p3);
        pso3.getTopology().add(p4);
        pso4.getTopology().add(p5);
        
        Niche.Swarms merged = Niche.absorb(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(Niche.Swarms.of(pso1, List.list((PopulationBasedAlgorithm) pso2, pso3, pso4)));

        Assert.assertEquals(0, merged._1().getTopology().size());
        Assert.assertEquals(3, merged._2().length());
        
        pso1.getTopology().add(p6);
        
        merged = Niche.absorb(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(Niche.Swarms.of(pso1, List.list((PopulationBasedAlgorithm) pso2, pso3, pso4)));
        
        System.out.println(merged._2().head().getTopology().size());
        System.out.println(merged._2().head().getTopology().get(0).getCandidateSolution());
        
        Assert.assertEquals(1, merged._1().getTopology().size());
        //Assert.assertEquals(Vector.of(10.0, 0.0), merged._1().getTopology().get(0).getCandidateSolution());
    }

    public static Particle createParticle(Fitness fitness, Vector position) {
        Particle particle = new StandardParticle();

        particle.setCandidateSolution(position);
        particle.getProperties().put(EntityType.FITNESS, fitness);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, position);
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, fitness);

        return particle;
    }
}
