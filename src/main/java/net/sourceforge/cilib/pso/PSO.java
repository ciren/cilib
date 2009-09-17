/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
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
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class PSO extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = -8234345682394295357L;
    private Topology<Particle> topology;
    private IterationStrategy<PSO> iterationStrategy;

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
    public void performInitialisation() {
        Iterable<? extends Entity> particles = this.initialisationStrategy.initialise(this.getOptimisationProblem());
        //Iterables.addAll(getTopology(), particles); // Use this instead?
        for (Entity particle : particles)
            topology.add((Particle) particle);
    }

    /**
     * Perform the iteration of the PSO algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration.
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);

        for (Particle particle : this.getTopology()) {
            particle.updateControlParameters();
        }
    }

    /**
     * Get the best current solution. This best solution is determined from the personal bests of the
     * particles.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Particle bestEntity = topology.getBestEntity(new SocialBestFitnessComparator<Particle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    /**
     * Get the collection of best solutions. This result does not actually make sense in the normal
     * PSO algorithm, but rather in a MultiObjective optimisation.
     * @return The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
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

    // TODO: Move down heirarchy into MOPSO????
    @Override
    public Particle getContribution() {
        return topology.getBestEntity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getContributionFitness() {
        return topology.getBestEntity().getBestFitness();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateContributionFitness(Fitness fitness) {
        topology.getBestEntity().calculateFitness();
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
}
