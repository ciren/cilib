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
package net.sourceforge.cilib.pso.vectorbased;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.particle.VBParticle;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;


public class VBPSO extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = -8234345682394295357L;
    private Topology<VBParticle> topology;
    private IterationStrategy<VBPSO> iterationStrategy;
    private ContributionSelectionStrategy contributionSelection;
    private DistanceMeasure distanceMeasure;

    /**
     * Creates a new instance of <code>PSO</code>. All fields are initialised to reasonable
     * defaults. Note that the {@link net.sourceforge.cilib.problem.OptimisationProblem} is initially
     * <code>null</code> and must be set before {@link #initialise()} is called.
     */
    public VBPSO() {
        topology = new GBestTopology<VBParticle>();

        iterationStrategy = new SynchronousIterationStrategy();

        initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityType(new VBParticle());
        contributionSelection = new ZeroContributionSelectionStrategy();
        distanceMeasure = new EuclideanDistanceMeasure();
    }

     public VBPSO(VBParticle p) {
        topology = new GBestTopology<VBParticle>();

        iterationStrategy = new SynchronousIterationStrategy();

        initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityType(new VBParticle());
        contributionSelection = new ZeroContributionSelectionStrategy();
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public VBPSO(VBPSO copy) {
        super(copy);
        this.topology = copy.topology.getClone();
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.initialisationStrategy = copy.initialisationStrategy.getClone();
        this.contributionSelection = copy.contributionSelection.getClone();
        this.distanceMeasure = copy.distanceMeasure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VBPSO getClone() {
        return new VBPSO(this);
    }

    /**
     * Perform the required initialisation for the algorithm. Create the particles and add then to
     * the specified topology.
     */
    @Override
    public void performInitialisation() {
        Iterable<? extends Entity> particles = this.initialisationStrategy.initialise(this.getOptimisationProblem());
        for (Entity particle : particles)
            topology.add((VBParticle) particle);
    }

    /**
     * Perform the iteration of the PSO algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration.
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
        VBParticle p;
        for (Particle particle : this.getTopology()) {
            p = (VBParticle) particle;
            p.calculateVP();
            p.calculateVG();
            p.calculateDotProduct();
            p.setRadius(distanceMeasure.distance(p.getPosition(), p.getNeighbourhoodBest().getBestPosition()));
        }
    }

    /**
     * Get the best current solution. This best solution is determined from the personal bests of the
     * particles.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Particle bestEntity = topology.get(0).getNeighbourhoodBest();
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
    public Topology<VBParticle> getTopology() {
        return topology;
    }

    /**
     * Get the <code>IterationStrategy</code> of the PSO algorithm.
     * @return Returns the iterationStrategy..
     */
    public IterationStrategy<VBPSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the <code>IterationStrategy</code> to be used.
     * @param iterationStrategy The iterationStrategy to set.
     */
    public void setIterationStrategy(IterationStrategy<VBPSO> iterationStrategy) {
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

    public int getPopulationSize(){
        return this.topology.size();
    }

    public void performEnhancedIteration(){
        iterationStrategy.performIteration(this);
        VBParticle p;
        VBParticle tmp;
        for (Particle particle : this.getTopology()) {
            p = (VBParticle) particle;
            tmp = p.getClone();
            p.calculateVP();
            p.calculateVG();
            p.calculateDotProduct();

            if(p.getDotProduct() < 0.0){
                p = tmp;
            }
        }
    }
}