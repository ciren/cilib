/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import com.google.common.collect.Lists;
import fj.F;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.parentprovider.BestParentProvider;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.ParticleCrossoverStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * Reference:
 * <p>
 * "Enhanced Performance of Particle Swarm Optimization with Generalized Generation 
 * Gap Model with Parent-Centric Recombination Operator", by C. Worasucheep, 
 * C Pipopwatthana, S Srimontha, W. Phanmak, in ECTI Transactions on Computer 
 * and Information Technology, vol 6, Non 2, 2012
 * </p>
 */
public class PSPGIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private ControlParameter iterationProbability;
    private ParticleCrossoverStrategy crossoverStrategy;
    private IterationStrategy iterationStrategy;
    
    public PSPGIterationStrategy() {
        ParentCentricCrossoverStrategy pcx = new ParentCentricCrossoverStrategy();
        pcx.setNumberOfParents(ConstantControlParameter.of(3));
        pcx.setNumberOfOffspring(ConstantControlParameter.of(2));
        pcx.setParentProvider(new BestParentProvider());
        
        ParticleCrossoverStrategy xover = new ParticleCrossoverStrategy();
        xover.setCrossoverStrategy(pcx);
        
        this.crossoverStrategy = xover;
        this.iterationProbability = ConstantControlParameter.of(0.05);
        this.iterationStrategy = new SynchronousIterationStrategy();
    }

    public PSPGIterationStrategy(PSPGIterationStrategy copy) {
        this.iterationProbability = copy.iterationProbability.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    @Override
    public PSPGIterationStrategy getClone() {
        return new PSPGIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        if (Rand.nextDouble() < iterationProbability.getParameter()) {
            crossoverStep(algorithm);
        } else {
            iterationStrategy.performIteration(algorithm);
        }
    }

    private void crossoverStep(PSO algorithm) {
        fj.data.List<Particle> topology = algorithm.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<>());
        
        // Get parents
        final List<Particle> parents = Lists.newArrayList(new RandomSelector<Particle>()
                .on(topology)
                .exclude(gbest)
                .select(Samples.first(crossoverStrategy.getNumberOfParents() - 1)));
        parents.add(gbest);
        // Add offspring
        parents.addAll(crossoverStrategy.crossover(parents));
        Collections.sort(parents, new DescendingFitnessComparator<>());
        
        // Get particles to replace
        final List<Particle> rp = Lists.newArrayList(new RandomSelector<Particle>()
                .on(topology)
                .exclude(gbest)
                .select(Samples.first(crossoverStrategy.getNumberOfParents() - 1)));
        rp.add(gbest);
        
        // Replace particles
        final Iterator<Particle> iter = parents.iterator();
        algorithm.setTopology(topology.map(new F<Particle, Particle>() {
            @Override
            public Particle f(Particle a) {
                if (rp.contains(a) && iter.hasNext()) {
                    return iter.next();
                }
                
                return a;
            }
        }));
        
        // Update nhood
        for (Particle current : algorithm.getTopology()) {
            for (Particle other : algorithm.getNeighbourhood().f(topology, current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }

    public void setIterationStrategy(IterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public void setIterationProbability(ControlParameter iterationProbability) {
        this.iterationProbability = iterationProbability;
    }

    public void setCrossoverStrategy(ParticleCrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

}
