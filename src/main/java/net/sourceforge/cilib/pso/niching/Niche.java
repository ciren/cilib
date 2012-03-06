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

import com.google.common.collect.Lists;
import fj.P2;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.niching.absorption.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.absorption.StandardAbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.merging.MergeOperation;
import net.sourceforge.cilib.pso.niching.merging.MergeStrategy;
import net.sourceforge.cilib.pso.niching.merging.StandardMergeOperation;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 * <p>
 * Generalized Niche algorithm.
 * </p>
 * <p>
 * This class is intended to be the base class (or even the only class) for all
 * algorithms implementing a form of niching.
 * </p>
 * <p>
 * Currently the main implementation is the NichePSO, however, any implementation
 * can be achieved by correctly applying the different setter methods with the appropriate
 * instances.
 * </p>
 * <pre>
 * {@literal @}inproceedings{}
 * </pre>
 */
public class Niche extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = 3575627467034673738L;

    protected PopulationBasedAlgorithm mainSwarm;

    protected NicheIdentificationStrategy nicheIdentificationStrategy;
    protected NicheCreationStrategy swarmCreationStrategy;
    protected AbsorptionStrategy absorptionStrategy;
    protected MergeOperation mergeOperation;
    protected Particle mainSwarmParticle;

    /**
     * Create a new instance of Niche.
     */
    public Niche() {
        this.mainSwarm = new PSO();
        PSO pso = (PSO) this.mainSwarm;
        ((SynchronousIterationStrategy)pso.getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        mainSwarmParticle = new StandardParticle();
        mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setSocialAcceleration(ConstantControlParameter.of(0.0));

        mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);
        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(20);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);

        this.nicheIdentificationStrategy = new StandardNicheIdentificationStrategy();
        this.swarmCreationStrategy = new StandardSwarmCreationStrategy();
        this.absorptionStrategy = new StandardAbsorptionStrategy();
        this.mergeOperation = new StandardMergeOperation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopulationBasedAlgorithm getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Initialise the main population based algorithm, provided such a notion exists.
     * @see MultiPopulationBasedAlgorithm#performInitialisation()
     */
    @Override
    public void performInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions())
            this.mainSwarm.addStoppingCondition(stoppingCondition);

        this.mainSwarm.setOptimisationProblem(getOptimisationProblem());

        this.mainSwarm.performInitialisation();
    }

    /**
     * <p>
     * Perform the iteration of the algorithm.
     * </p>
     * <p>
     * The general format of this method would be the following steps:
     * <ol>
     *   <li>Perform an iteration of the main swarm.</li>
     *   <li>Perform an iteration for each of the contained sub-swarms.</li>
     *   <li>Merge any sub-swarms as defined my the associated {@link MergeStrategy}.</li>
     *   <li>Perform an absorption step defined by a {@link AbsorptionStrategy}.</li>
     *   <li>Identify any new potential niches using a {@link NicheIdentificationStrategy}.</li>
     *   <li>Create new sub-swarms via a {@link NicheCreationStrategy} for the identified niches.</li>
     * </ol>
     * </p>
     */
    @Override
    protected void algorithmIteration() {
        mainSwarm.performIteration();

        for (PopulationBasedAlgorithm subSwarm : subPopulationsAlgorithms) {
            subSwarm.performIteration(); // TODO: There may be an issue with this and the number of iterations
        }

        fj.data.List<PopulationBasedAlgorithm> algs = fj.data.List.<PopulationBasedAlgorithm>iterableList(subPopulationsAlgorithms);
        P2<PopulationBasedAlgorithm, fj.data.List<PopulationBasedAlgorithm>> newPops = mergeOperation.f(mainSwarm, algs);
        subPopulationsAlgorithms = Lists.newArrayList(newPops._2().toCollection());
        mainSwarm = newPops._1();

        this.absorptionStrategy.absorb(this);

        List<Entity> niches = this.nicheIdentificationStrategy.identify(mainSwarm.getTopology());
        this.swarmCreationStrategy.create(this, niches);
    }

    /**
     * There is no best solution associated with a top level Niche algorithm.
     * @see #getSolutions()
     */
    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    /**
     * Get the solutions of the the optimisation. The solutions are the best
     * entities within each identified niche.
     * @return The list of best solutions for each niche.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> list = new ArrayList<OptimisationSolution>();

        for (PopulationBasedAlgorithm subSwarm : this)
            list.add(subSwarm.getBestSolution());

        return list;
    }

    /**
     * Get the main swarm.
     * @return The main swarm.
     */
    public PopulationBasedAlgorithm getMainSwarm() {
        return this.mainSwarm;
    }

    /**
     * Set the main swarm of the Niche.
     * @param mainSwarm The swarm to set.
     */
    public void setMainSwarm(PopulationBasedAlgorithm mainSwarm) {
        this.mainSwarm = mainSwarm;
    }

    /**
     * @param absorptionStrategy the absorptionStrategy to set
     */
    public void setAbsorptionStrategy(AbsorptionStrategy absorptionStrategy) {
        this.absorptionStrategy = absorptionStrategy;
    }

    public MergeOperation getMergeOperation() {
        return mergeOperation;
    }

    public void setMergeOperation(MergeOperation mergeOperation) {
        this.mergeOperation = mergeOperation;
    }
}
