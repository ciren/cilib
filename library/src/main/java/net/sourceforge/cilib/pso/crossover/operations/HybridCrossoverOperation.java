/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Topologies;
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

import fj.F;
import fj.F2;
import fj.P;
import fj.P2;

import com.google.common.collect.Lists;

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

    private static <A> P2<fj.data.List<A>, fj.data.List<A>> partition(final fj.data.List<A> list, final F<A, Boolean> predicate) {
        return list.foldLeft(new F2<P2<fj.data.List<A>, fj.data.List<A>>, A, P2<fj.data.List<A>, fj.data.List<A>>>() {
            @Override
            public P2<fj.data.List<A>, fj.data.List<A>> f(final P2<fj.data.List<A>, fj.data.List<A>> accum, final A element) {
                if (predicate.f(element)) {
                    return P.p(fj.data.List.cons(element, accum._1()), accum._2());
                } else {
                    return P.p(accum._1(), fj.data.List.cons(element, accum._2()));
                }
            };
        }, P.p(fj.data.List.<A>nil(), fj.data.List.<A>nil()));
    }

    @Override
    public fj.data.List<Particle> f(final PSO pso) {
        final fj.data.List<Particle> topology = pso.getTopology();
        final UniformDistribution uniform = new UniformDistribution();

        P2<fj.data.List<Particle>, fj.data.List<Particle>> pair = partition(topology, new F<Particle, Boolean>() {
            public Boolean f(Particle p) {
                return uniform.getRandomNumber() < crossoverProbability.getParameter();
            }
        });

        fj.data.List<Particle> parents = pair._1();
        fj.data.List<Particle> newTopology = pair._2();

        while (!parents.isEmpty()) {
            int numberOfParents = particleCrossover.getCrossoverStrategy().getNumberOfParents();

            // need specific number of parents to perform crossover
            if (parents.length() < numberOfParents) {
                newTopology = newTopology.append(parents);
                break;
            }

            final List<Particle> selectedParents = selector.on(parents).select(Samples.first(numberOfParents));
            final List<Particle> offspring = particleCrossover.crossover(selectedParents);

            parents = parents.removeAll(new F<Particle, Boolean>() {
                @Override
                public Boolean f(Particle p) {
                    return selectedParents.contains(p);
                }
            });

            newTopology = newTopology.append(fj.data.List.iterableList(parentReplacementStrategy.f(selectedParents, offspring)));
        }

        final fj.data.List<Particle> local = newTopology;
        return newTopology.map(new F<Particle, Particle>() {
            public Particle f(Particle p) {
                Particle nBest = Topologies.getNeighbourhoodBest(local, p, pso.getNeighbourhood(), new SocialBestFitnessComparator());
                p.setNeighbourhoodBest(nBest);
                return p;
            }
        });
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
