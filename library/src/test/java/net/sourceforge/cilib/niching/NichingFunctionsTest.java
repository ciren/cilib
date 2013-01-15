/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching;

import fj.P2;
import fj.data.List;
import java.util.Arrays;
import net.sourceforge.cilib.algorithm.initialisation.ChargedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
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
import net.sourceforge.cilib.pso.dynamic.QuantumVelocityProvider;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Populations;
import org.junit.Assert;
import org.junit.Test;

public class NichingFunctionsTest {
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

        NichingSwarms merged = NichingFunctions.merge(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of((PopulationBasedAlgorithm) pso3, List.list((PopulationBasedAlgorithm) pso1, pso2)));

        Assert.assertEquals(1, merged._1().getTopology().size());
        Assert.assertEquals(1, merged._2().length());
        Assert.assertEquals(4, merged._2().head().getTopology().size());
    }

    @Test
    public void testSingleAbsorption() {
        PSO mainSwarm = new PSO();
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        mainSwarm.setTopology(new GBestTopology<Particle>());
        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());

        Particle m1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle m2 = createParticle(new MinimisationFitness(0.0), Vector.of(0.4, 0.4));
        Particle m3 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 11.0));

        Particle p1_1 = createParticle(new MinimisationFitness(0.0), Vector.of(0.3, 0.3));
        Particle p1_2 = createParticle(new MinimisationFitness(3.0), Vector.of(0.6, 0.6));

        Particle p2_1 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 0.0));
        Particle p2_2 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        mainSwarm.getTopology().addAll(Arrays.asList(m1, m2, m3));
        pso1.getTopology().addAll(Arrays.asList(p1_1, p1_2));
        pso2.getTopology().addAll(Arrays.asList(p2_1, p2_2));

        P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> merged = NichingFunctions.absorbSingleSwarm(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of((PopulationBasedAlgorithm) pso1, Populations.populationToAlgorithms().f(mainSwarm)));

        Assert.assertEquals(2, merged._1().getTopology().size());
        Assert.assertEquals(3, merged._2().getTopology().size());
        Assert.assertEquals(Vector.of(0.4, 0.4), merged._2().getTopology().get(2).getCandidateSolution());

        merged = NichingFunctions.absorbSingleSwarm(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of((PopulationBasedAlgorithm) pso2, Populations.populationToAlgorithms().f(mainSwarm)));

        Assert.assertEquals(2, merged._1().getTopology().size());
        Assert.assertEquals(3, merged._2().getTopology().size());
        Assert.assertEquals(Vector.of(100, 11), merged._2().getTopology().get(2).getCandidateSolution());
    }

    @Test
    public void testAbsorption() {
        PSO mainSwarm = new PSO();
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        mainSwarm.setTopology(new GBestTopology<Particle>());
        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());

        Particle m1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle m2 = createParticle(new MinimisationFitness(0.0), Vector.of(0.4, 0.4));
        Particle m3 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        Particle p1_1 = createParticle(new MinimisationFitness(0.0), Vector.of(0.3, 0.3));
        Particle p1_2 = createParticle(new MinimisationFitness(3.0), Vector.of(0.6, 0.6));

        Particle p2_1 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));
        Particle p2_2 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        mainSwarm.getTopology().addAll(Arrays.asList(m1, m2, m3));
        pso1.getTopology().addAll(Arrays.asList(p1_1, p1_2));
        pso2.getTopology().addAll(Arrays.asList(p2_1, p2_2));

        NichingSwarms merged = NichingFunctions.absorb(new RadiusOverlapMergeDetection(), new SingleSwarmMergeStrategy(), new StandardMergeStrategy())
                .f(NichingSwarms.of((PopulationBasedAlgorithm) mainSwarm, List.list((PopulationBasedAlgorithm) pso1, pso2)));

        Assert.assertEquals(1, merged._1().getTopology().size());
        Assert.assertEquals(2, merged._2().length());
        Assert.assertEquals(3, merged._2().head().getTopology().size());
        Assert.assertEquals(3, merged._2().tail().head().getTopology().size());
    }

    @Test
    public void testEntityToAlgorithm() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy<Entity>());
        PopulationBasedAlgorithm a = Populations.entityToAlgorithm().f(
                createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)),
                pso);

        Assert.assertEquals(1, a.getTopology().size());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.getInitialisationStrategy().getClass());
    }

    @Test
    public void testEntitiesToAlgorithm() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy<Entity>());
        List<PopulationBasedAlgorithm> a = Populations.entitiesToAlgorithms().f(Arrays.asList(
                (Entity) createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)),
                createParticle(new MinimisationFitness(0.0), Vector.of(2.0, 2.0))),
                pso);

        Assert.assertEquals(2, a.length());
        Assert.assertEquals(1, a.head().getTopology().size());
        Assert.assertEquals(1, a.tail().head().getTopology().size());
        Assert.assertEquals(Vector.of(1.0, 1.0), a.head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(Vector.of(2.0, 2.0), a.tail().head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.head().getInitialisationStrategy().getClass());
    }

    @Test
    public void testSwarmToAlgorithm() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy<Entity>());
        pso.getTopology().addAll(Arrays.asList(
                createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)),
                createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0))
                ));
        List<PopulationBasedAlgorithm> a = Populations.populationToAlgorithms().f(pso);

        Assert.assertEquals(2, a.length());
        Assert.assertEquals(1, a.head().getTopology().size());
        Assert.assertEquals(1, a.tail().head().getTopology().size());
        Assert.assertEquals(Vector.of(1.0, 1.0), a.head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(Vector.of(2.0, 2.0), a.tail().head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.head().getInitialisationStrategy().getClass());
    }

    @Test
    public void testEmptyPopulation() {
        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy<Entity>());
        PopulationBasedAlgorithm a = Populations.emptyPopulation().f(pso);

        Assert.assertEquals(0, a.getTopology().size());
        Assert.assertEquals(ChargedPopulationInitialisationStrategy.class, a.getInitialisationStrategy().getClass());
    }

    @Test
    public void testEnforceTopology() {
        ParticleBehavior pb = new ParticleBehavior();
        pb.setVelocityProvider(new QuantumVelocityProvider());

        PSO pso = new PSO();
        pso.setInitialisationStrategy(new ChargedPopulationInitialisationStrategy<Entity>());
        pso.getTopology().addAll(Arrays.asList(
                createParticle(new MinimisationFitness(0.0), Vector.of(1.0, 1.0)).getClone(),
                createParticle(new MinimisationFitness(1.0), Vector.of(2.0, 2.0)).getClone()
                ));

        Assert.assertNull(pso.getTopology().get(0).getNeighbourhoodBest());
        Assert.assertEquals(StandardVelocityProvider.class, pso.getTopology().get(0).getVelocityProvider().getClass());

        PopulationBasedAlgorithm a = Populations.enforceTopology(pb).f(pso);

        Assert.assertNotNull(((Particle) a.getTopology().get(0)).getNeighbourhoodBest());
        Assert.assertEquals(QuantumVelocityProvider.class, ((Particle) a.getTopology().get(0)).getVelocityProvider().getClass());
    }

    @Test
    public void testCreateNichings() {
        PSO mainSwarm = new PSO();
        PSO pso1 = new PSO();
        PSO pso2 = new PSO();

        mainSwarm.setTopology(new GBestTopology<Particle>());
        pso1.setTopology(new GBestTopology<Particle>());
        pso2.setTopology(new GBestTopology<Particle>());

        Particle m1 = createParticle(new MinimisationFitness(1.0), Vector.of(0.0, 0.0));
        Particle m2 = createParticle(new MinimisationFitness(0.0), Vector.of(0.4, 0.4));
        Particle m3 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        Particle p1_1 = createParticle(new MinimisationFitness(0.0), Vector.of(0.3, 0.3));
        Particle p1_2 = createParticle(new MinimisationFitness(3.0), Vector.of(0.6, 0.6));

        Particle p2_1 = createParticle(new MinimisationFitness(3.0), Vector.of(10.0, 0.0));
        Particle p2_2 = createParticle(new MinimisationFitness(3.0), Vector.of(100.0, 12.0));

        mainSwarm.getTopology().addAll(Arrays.asList(m1, m2, m3));
        pso1.getTopology().addAll(Arrays.asList(p1_1, p1_2));
        pso2.getTopology().addAll(Arrays.asList(p2_1, p2_2));

        NicheDetection detector = new MaintainedFitnessNicheDetection();
        NicheCreationStrategy creator = new ClosestNeighbourNicheCreationStrategy();
        ((ClosestNeighbourNicheCreationStrategy) creator).setSwarmBehavior(new ParticleBehavior());
        MergeStrategy merger = new SingleSwarmMergeStrategy();

        NichingSwarms merged = NichingFunctions.createNiches(detector, creator, merger)
                .andThen(NichingFunctions.createNiches(detector, creator, merger)
                .andThen(NichingFunctions.createNiches(detector, creator, merger)))
                .f(NichingSwarms.of((PopulationBasedAlgorithm) mainSwarm, List.list((PopulationBasedAlgorithm) pso1, pso2)));

        Assert.assertEquals(1, merged._1().getTopology().size());
        Assert.assertEquals(3, merged._2().length());
        Assert.assertEquals(2, merged._2().head().getTopology().size());
        Assert.assertEquals(Vector.of(0.0, 0.0), merged._2().head().getTopology().get(0).getCandidateSolution());
        Assert.assertEquals(Vector.of(0.4, 0.4), merged._2().head().getTopology().get(1).getCandidateSolution());
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
