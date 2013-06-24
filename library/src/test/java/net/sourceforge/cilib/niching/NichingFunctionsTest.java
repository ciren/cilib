/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching;

import java.util.Arrays;

import net.sourceforge.cilib.algorithm.initialisation.ChargedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.niching.creation.ClosestNeighbourNicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.MaintainedFitnessNicheDetection;
import net.sourceforge.cilib.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.NicheDetection;
import net.sourceforge.cilib.niching.merging.MergeStrategy;
import net.sourceforge.cilib.niching.merging.SingleSwarmMergeStrategy;
import net.sourceforge.cilib.niching.merging.StandardMergeStrategy;
import net.sourceforge.cilib.niching.merging.detection.RadiusOverlapMergeDetection;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.dynamic.QuantumVelocityProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Populations;

import org.junit.Assert;
import org.junit.Test;

import fj.P2;
import fj.data.List;

public class NichingFunctionsTest {
    @Test
    public void testNormalMerge() {
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();
        PSO pso3 = new PSO();

        Particle p1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle p2 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3), Math.sqrt(0.3)));
        Particle p3 = createParticle(new MinimisationFitness(0.0), Vector.of(Math.sqrt(0.3) + Maths.EPSILON, Math.sqrt(0.3)));
        Particle p4 = createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 0.0));
        Particle p5 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));

        pso1.setTopology(fj.data.List.list(p1, p2));
        pso2.setTopology(fj.data.List.list(p3, p4));
        pso3.setTopology(fj.data.List.list(p5));

        NichingSwarms merged = NichingFunctions.merge(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of(pso3, List.list((SinglePopulationBasedAlgorithm)pso1, (SinglePopulationBasedAlgorithm)pso2)));

        Assert.assertEquals(1, merged._1().getTopology().length());
        Assert.assertEquals(1, merged._2().length());
        Assert.assertEquals(4, merged._2().head().getTopology().length());
    }

    @Test
    public void testSingleAbsorption() {
        PSO mainSwarm = new PSO();
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle m1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle m2 = createParticle(new MinimisationFitness(0.0), Vector.of(0.4, 0.4));
        Particle m3 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 11.0));

        Particle p1_1 = createParticle(new MinimisationFitness(0.0), Vector.of(0.3, 0.3));
        Particle p1_2 = createParticle(new MinimisationFitness(3.0), Vector.of(0.6, 0.6));

        Particle p2_1 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 0.0));
        Particle p2_2 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        mainSwarm.setTopology(fj.data.List.list(m1, m2, m3));
        pso1.setTopology(fj.data.List.list(p1_1, p1_2));
        pso2.setTopology(fj.data.List.list(p2_1, p2_2));

        P2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm> merged = NichingFunctions.absorbSingleSwarm(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of(pso1, Populations.populationToAlgorithms().f(mainSwarm)));

        Assert.assertEquals(2, merged._1().getTopology().length());
        Assert.assertEquals(3, merged._2().getTopology().length());
        Assert.assertEquals(Vector.of(0.4, 0.4), ((Entity)merged._2().getTopology().index(2)).getPosition());

        merged = NichingFunctions.absorbSingleSwarm(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of(pso2, Populations.populationToAlgorithms().f(mainSwarm)));

        Assert.assertEquals(2, merged._1().getTopology().length());
        Assert.assertEquals(3, merged._2().getTopology().length());
        Assert.assertEquals(Vector.of(100, 11), ((Entity)merged._2().getTopology().index(2)).getPosition());
    }

    @Test
    public void testAbsorption() {
        PSO mainSwarm = new PSO();
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle m1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle m2 = createParticle(new MinimisationFitness(0.0), Vector.of(0.4, 0.4));
        Particle m3 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        Particle p1_1 = createParticle(new MinimisationFitness(0.0), Vector.of(0.3, 0.3));
        Particle p1_2 = createParticle(new MinimisationFitness(3.0), Vector.of(0.6, 0.6));

        Particle p2_1 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));
        Particle p2_2 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        mainSwarm.setTopology(fj.data.List.list(m1, m2, m3));
        pso1.setTopology(fj.data.List.list(p1_1, p1_2));
        pso2.setTopology(fj.data.List.list(p2_1, p2_2));

        NichingSwarms merged = NichingFunctions.absorb(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of(mainSwarm, List.list((SinglePopulationBasedAlgorithm) pso1, pso2)));

        Assert.assertEquals(1, merged._1().getTopology().length());
        Assert.assertEquals(2, merged._2().length());
        Assert.assertEquals(3, merged._2().head().getTopology().length());
        Assert.assertEquals(3, merged._2().tail().head().getTopology().length());
    }

    @Test
    public void testEntityToAlgorithm() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy());
        SinglePopulationBasedAlgorithm a = Populations.entityToAlgorithm().f(
                createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)),
                pso);

        Assert.assertEquals(1, a.getTopology().length());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.getInitialisationStrategy().getClass());
    }

    @Test
    public void testEntitiesToAlgorithm() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy());
        List<SinglePopulationBasedAlgorithm> a = Populations.entitiesToAlgorithms().f(Arrays.<Entity>asList(
                createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)),
                createParticle(new MinimisationFitness(0.0), Vector.of(2.0, 2.0))),
                pso);

        Assert.assertEquals(2, a.length());
        Assert.assertEquals(1, a.head().getTopology().length());
        Assert.assertEquals(1, a.tail().head().getTopology().length());
        Assert.assertEquals(Vector.of(1.0, 1.0), ((Entity) a.head().getTopology().head()).getPosition());
        Assert.assertEquals(Vector.of(2.0, 2.0), ((Entity) a.tail().head().getTopology().head()).getPosition());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.head().getInitialisationStrategy().getClass());
    }

    @Test
    public void testSwarmToAlgorithm() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy());
        pso.setTopology(fj.data.List.list(createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)),
                createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0))
                ));
        List<SinglePopulationBasedAlgorithm> a = Populations.populationToAlgorithms().f(pso);

        Assert.assertEquals(2, a.length());
        Assert.assertEquals(1, a.head().getTopology().length());
        Assert.assertEquals(1, a.tail().head().getTopology().length());
        Assert.assertEquals(Vector.of(1.0, 1.0), ((Entity) a.head().getTopology().head()).getPosition());
        Assert.assertEquals(Vector.of(2.0, 2.0), ((Entity) a.tail().head().getTopology().head()).getPosition());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.head().getInitialisationStrategy().getClass());
    }

    @Test
    public void testEmptyPopulation() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy());
        SinglePopulationBasedAlgorithm a = Populations.emptyPopulation().f(pso);

        Assert.assertEquals(0, a.getTopology().length());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.getInitialisationStrategy().getClass());
    }

    @Test
    public void testEnforceTopology() {
        StandardParticleBehaviour pb = new StandardParticleBehaviour();
        pb.setVelocityProvider(new QuantumVelocityProvider());

        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy());
        pso.setTopology(fj.data.List.list(
                createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)).getClone(),
                createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0)).getClone()
                ));

        Assert.assertNull(pso.getTopology().head().getNeighbourhoodBest());
        Assert.assertEquals(StandardVelocityProvider.class, ((StandardParticleBehaviour) pso.getTopology().head().getBehaviour()).getVelocityProvider().getClass());

        SinglePopulationBasedAlgorithm a = Populations.enforceTopology(pb).f(pso);

        Assert.assertNotNull(((Particle) a.getTopology().head()).getNeighbourhoodBest());
        Assert.assertEquals(QuantumVelocityProvider.class, ((StandardParticleBehaviour) ((Particle) a.getTopology().head()).getBehaviour()).getVelocityProvider().getClass());
    }

    @Test
    public void testCreateNichings() {
        PSO mainSwarm = new PSO();
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        Particle m1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle m2 = createParticle(new MinimisationFitness(0.0), Vector.of(0.4, 0.4));
        Particle m3 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        Particle p1_1 = createParticle(new MinimisationFitness(0.0), Vector.of(0.3, 0.3));
        Particle p1_2 = createParticle(new MinimisationFitness(3.0), Vector.of(0.6, 0.6));

        Particle p2_1 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));
        Particle p2_2 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        mainSwarm.setTopology(fj.data.List.list(m1, m2, m3));
        pso1.setTopology(fj.data.List.list(p1_1, p1_2));
        pso2.setTopology(fj.data.List.list(p2_1, p2_2));

        NicheDetection detector = new MaintainedFitnessNicheDetection();
        NicheCreationStrategy creator = new ClosestNeighbourNicheCreationStrategy();
        ((ClosestNeighbourNicheCreationStrategy) creator).setSwarmBehavior(new StandardParticleBehaviour());
        MergeStrategy merger = new SingleSwarmMergeStrategy();

        NichingSwarms merged = NichingFunctions.createNiches(detector, creator, merger)
                .andThen(NichingFunctions.createNiches(detector, creator, merger)
                .andThen(NichingFunctions.createNiches(detector, creator, merger)))
                .f(NichingSwarms.of(mainSwarm, List.list((SinglePopulationBasedAlgorithm) pso1, pso2)));

        Assert.assertEquals(1, merged._1().getTopology().length());
        Assert.assertEquals(3, merged._2().length());
        Assert.assertEquals(2, merged._2().head().getTopology().length());
        Assert.assertEquals(Vector.of(0.0, 0.0), ((Entity) merged._2().head().getTopology().head()).getPosition());
        Assert.assertEquals(Vector.of(0.4, 0.4), ((Entity) merged._2().head().getTopology().index(1)).getPosition());
    }

    public static Particle createParticle(Fitness fitness, Vector position) {
        Particle particle = new StandardParticle();

        particle.setPosition(position);
        particle.put(Property.FITNESS, fitness);
        particle.put(Property.BEST_POSITION, position);
        particle.put(Property.BEST_FITNESS, fitness);

        return particle;
    }
}
