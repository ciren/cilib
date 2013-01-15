/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.real.ArithmeticCrossoverStrategy;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.ParticleCrossoverStrategy;
import net.sourceforge.cilib.pso.crossover.parentupdate.AlwaysReplaceParentReplacementStrategy;
import net.sourceforge.cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import net.sourceforge.cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.LovbjergOffspringVelocityProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * A crossover operation for PSOs that selects particles according to Lovbjerg
 * et al's hybrid PSO.
 *
 * <p>Defined in:</p>
 * <p>
 * @INPROCEEDINGS{Løvbjerg01hybridparticle,
 *   author = {Morten Løvbjerg and Thomas Kiel Rasmussen and Thiemo Krink},
 *   title = {Hybrid Particle Swarm Optimiser with Breeding and Subpopulations},
 *   booktitle = {Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001},
 *   year = {2001},
 *   pages = {469--476},
 *   publisher = {Morgan Kaufmann}
 * }
 * </p>
 * <p>Also used in:</p>
 */
public class HybridCrossoverOperation extends PSOCrossoverOperation {

    private ParticleCrossoverStrategy particleCrossover;
    private ParentReplacementStrategy parentReplacementStrategy;
    private ControlParameter crossoverProbability;
    private Selector selector;

    public HybridCrossoverOperation() {
        this.particleCrossover = new ParticleCrossoverStrategy(new ArithmeticCrossoverStrategy(),
                new CurrentPositionOffspringPBestProvider(),
                new LovbjergOffspringVelocityProvider());

        this.parentReplacementStrategy = new AlwaysReplaceParentReplacementStrategy();
        this.crossoverProbability = ConstantControlParameter.of(0.2);
        this.selector = new RandomSelector();
    }

    public HybridCrossoverOperation(HybridCrossoverOperation copy) {
        this.particleCrossover = copy.particleCrossover;
        this.parentReplacementStrategy = copy.parentReplacementStrategy;
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.selector = copy.selector;
    }

    @Override
    public HybridCrossoverOperation getClone() {
        return new HybridCrossoverOperation(this);
    }

    @Override
    public Topology<Particle> f(PSO pso) {
        Topology<Particle> topology = pso.getTopology();
        UniformDistribution uniform = new UniformDistribution();
        List<Particle> parents = Lists.newArrayList();
        List<Particle> offspring;

        Topology<Particle> newTopology = topology.getClone();
        newTopology.clear();

        for (Particle p : topology) {
            if (uniform.getRandomNumber() < crossoverProbability.getParameter()) {
                parents.add(p);
            } else {
                newTopology.add(p);
            }
        }

        while (!parents.isEmpty()) {
            int numberOfParents = particleCrossover.getCrossoverStrategy().getNumberOfParents();

            // need specific number of parents to perform crossover
            if (parents.size() < numberOfParents) {
                newTopology.addAll(parents);
                break;
            }

            List<Particle> selectedParents = selector.on(parents).select(Samples.first(numberOfParents));
            offspring = particleCrossover.crossover(selectedParents);

            parents.removeAll(selectedParents);

            newTopology.addAll(parentReplacementStrategy.f(selectedParents, offspring));
        }

        for (Particle p : newTopology) {
            Particle nBest = Topologies.getNeighbourhoodBest(newTopology, p, new SocialBestFitnessComparator());
            p.setNeighbourhoodBest(nBest);
        }

        return newTopology;
    }

    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setParentReplacementStrategy(ParentReplacementStrategy parentReplacementStrategy) {
        this.parentReplacementStrategy = parentReplacementStrategy;
    }

    public ParentReplacementStrategy getParentReplacementStrategy() {
        return parentReplacementStrategy;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setParticleCrossover(ParticleCrossoverStrategy particleCrossover) {
        this.particleCrossover = particleCrossover;
    }

    public ParticleCrossoverStrategy getParticleCrossover() {
        return particleCrossover;
    }
}
