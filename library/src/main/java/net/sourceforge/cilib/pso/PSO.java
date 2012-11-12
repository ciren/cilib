/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;

/**
 * <p>
 * An implementation of the standard PSO algorithm.
 * </p>
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> J. Kennedy and R.C. Eberhart, "Particle swarm optimization," in Proceedings of IEEE
 * International Conference on Neural Networks, vol. IV, (Perth Australia), pp. 1942-1948, 1995.
 * </li>
 * <li> R.C. Eberhart and J. Kennedy, "A new optimizer using particle swarm theory," in Proceedings
 * of the Sixth International Symposium on Micro Machine and Human Science, (Nagoya, Japan), pp.
 * 39-43, 1995. </li>
 * <li> Y. Shi amd R.C. Eberhart, "A modified particle swarm optimizer," in Proceedings of the IEEE
 * Congress on Evolutionary Computation, (Anchorage, Alaska), pp. 69-73, May 1998. </li>
 * </ul>
 * </p>
 */
public class PSO extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = -8234345682394295357L;
    private Topology<Particle> topology;
    private IterationStrategy<PSO> iterationStrategy;
    private ContributionSelectionStrategy contributionSelection;

    /**
     * Creates a new instance of <code>PSO</code>. All fields are initialised to reasonable
     * defaults. Note that the {@link net.sourceforge.cilib.problem.OptimisationProblem} is initially
     * <code>null</code> and must be set before {@link #initialise()} is called.
     */
    public PSO() {
        topology = new GBestTopology<Particle>();

        iterationStrategy = new SynchronousIterationStrategy();

        initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityType(new StandardParticle());
        contributionSelection = new ZeroContributionSelectionStrategy();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public PSO(PSO copy) {
        super(copy);
        this.topology = copy.topology.getClone();
        this.iterationStrategy = copy.iterationStrategy; // need to clone?
        this.initialisationStrategy = copy.initialisationStrategy; // need to clone?
        this.contributionSelection = copy.contributionSelection.getClone();

        for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext();) {
            Particle current = i.next();
            Particle nBest = Topologies.getNeighbourhoodBest(topology, current, new SocialBestFitnessComparator());
            current.setNeighbourhoodBest(nBest);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PSO getClone() {
        return new PSO(this);
    }

    /**
     * Perform the required initialisation for the algorithm. Create the particles and add then to
     * the specified topology.
     */
    @Override
    public void algorithmInitialisation() {
        Iterable<Particle> particles = (Iterable<Particle>) this.initialisationStrategy.initialise(this.getOptimisationProblem());
        topology.clear();
        topology.addAll(Lists.<Particle>newLinkedList(particles));

        for (Particle p : topology) {
            p.calculateFitness();
        }
    }

    /**
     * Perform the iteration of the PSO algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration.
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * Get the best current solution. This best solution is determined from the personal bests of the
     * particles.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Particle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<Particle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    /**
     * Get the collection of best solutions. This result does not actually make sense in the normal
     * PSO algorithm, but rather in a MultiObjective optimisation.
     * @return The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (Particle e : Topologies.getNeighbourhoodBestEntities(topology, new SocialBestFitnessComparator<Particle>())) {
            solutions.add(new OptimisationSolution(e.getBestPosition(), e.getBestFitness()));
        }
        return solutions;
    }

    /**
     * Sets the particle topology used. The default is {@link GBestTopology}.
     * @param topology A class that implements the {@link Topology} interface.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    /**
     * Accessor for the topology being used.
     * @return The {@link Topology} being used.
     */
    @Override
    public Topology<Particle> getTopology() {
        return topology;
    }

    /**
     * Get the <code>IterationStrategy</code> of the PSO algorithm.
     * @return Returns the iterationStrategy..
     */
    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the <code>IterationStrategy</code> to be used.
     * @param iterationStrategy The iterationStrategy to set.
     */
    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }
}
