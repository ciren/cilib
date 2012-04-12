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
package net.sourceforge.cilib.niching;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.niching.creation.ClosestNeighbourNicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.MaintainedFitnessNicheDetection;
import net.sourceforge.cilib.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.NicheDetection;
import net.sourceforge.cilib.niching.iterationstrategies.NichePSO;
import net.sourceforge.cilib.niching.merging.*;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 * <p>
 * Generalized NicheAlgorithm algorithm.
 * </p>
 * <p>
 * This class is intended to be the base class (or even the only class) for all
 * algorithms implementing a form of niching.
 * </p>
 * <p>
 * Currently the main implementation is the NicheAlgorithm, however, any implementation
 * can be achieved by correctly applying the different setter methods with the appropriate
 * instances.
 * </p>
 * <pre>
 * {@literal @}inproceedings{}
 * </pre>
 */
public class NicheAlgorithm extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = 3575627467034673738L;

    protected IterationStrategy<NicheAlgorithm> iterationStrategy;
    protected PopulationBasedAlgorithm mainSwarm;
    protected Particle mainSwarmParticle;

    protected NicheDetection nicheDetection;
    protected NicheCreationStrategy nicheCreationStrategy;
    protected MergeStrategy mainSwarmPostCreation;

    protected MergeStrategy subSwarmsMergeStrategy;
    protected MergeStrategy mainSwarmMergeStrategy;
    protected MergeDetection mergeDetection;

    protected MergeStrategy mainSwarmAbsorptionStrategy;
    protected MergeStrategy subSwarmsAbsorptionStrategy;
    protected MergeDetection absorptionDetection;

    /**
     * Default constructor. The defaults are:
     * <p>
     * Merging:
     * <ul>
     *  <li>Detection: radius overlap detection</li>
     *  <li>Main swarm merging: no merging back into the main swarm (SingleSwarmMergingStrategy)</li>
     *  <li>Swarm merging: standard merging i.e. entities in the second swarm go into the first swarm</li>
     * </ul>
     * </p>
     * <p>
     * Absorption:
     * <ul>
     *  <li>Detection: radius overlap detection</li>
     *  <li>Main swarm absorption: no merging back into the main swarm (SingleSwarmMergingStrategy)</li>
     *  <li>Swarm absorption: standard merging i.e. entities in the main swarm go into the sub swarm</li>
     * </ul>
     * </p>
     * <p>
     * Niche creation:
     * <ul>
     *  <li>Detection: fitness deviation threshold</li>
     *  <li>Main swarm merging: no merging back into the main swarm (SingleSwarmMergingStrategy)</li>
     *  <li>Creation strategy: standard creation i.e. a swarm is created with a particle and its closest neighbor</li>
     * </ul>
     * </p>
     */
    public NicheAlgorithm() {
        this.mainSwarm = new PSO();
        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setSocialAcceleration(ConstantControlParameter.of(0.0));
        velocityUpdateStrategy.setCognitiveAcceleration(ConstantControlParameter.of(1.2));
        
        this.mainSwarmParticle = new StandardParticle();
        this.mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        this.mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);

        ((ClonedPopulationInitialisationStrategy) ((PSO) this.mainSwarm).getInitialisationStrategy()).setEntityType(mainSwarmParticle);
        ((SynchronousIterationStrategy) ((PSO) this.mainSwarm).getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        this.nicheDetection = new MaintainedFitnessNicheDetection();
        this.nicheCreationStrategy = new ClosestNeighbourNicheCreationStrategy();
        this.mainSwarmPostCreation = new SingleSwarmMergeStrategy();

        this.mainSwarmAbsorptionStrategy = new SingleSwarmMergeStrategy();
        this.subSwarmsAbsorptionStrategy = new StandardMergeStrategy();
        this.absorptionDetection = new RadiusOverlapMergeDetection();

        this.subSwarmsMergeStrategy = new StandardMergeStrategy();
        this.mainSwarmMergeStrategy = new SingleSwarmMergeStrategy();
        this.mergeDetection = new RadiusOverlapMergeDetection();

        this.iterationStrategy = new NichePSO();
    }
    
    /**
     * Copy constructor.
     */
    public NicheAlgorithm(NicheAlgorithm copy) {
        super(copy);

        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.mainSwarm = copy.mainSwarm.getClone();
        this.mainSwarmParticle = copy.mainSwarmParticle.getClone();
        
        this.nicheDetection = copy.nicheDetection;
        this.nicheCreationStrategy = copy.nicheCreationStrategy;
        this.mainSwarmPostCreation = copy.mainSwarmPostCreation;

        this.mainSwarmAbsorptionStrategy = copy.mainSwarmAbsorptionStrategy;
        this.subSwarmsAbsorptionStrategy = copy.subSwarmsAbsorptionStrategy;
        this.absorptionDetection = copy.absorptionDetection;

        this.subSwarmsMergeStrategy = copy.subSwarmsMergeStrategy;
        this.mainSwarmMergeStrategy = copy.mainSwarmMergeStrategy;
        this.mergeDetection = copy.mergeDetection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PopulationBasedAlgorithm getClone() {
        return new NicheAlgorithm(this);
    }

    /**
     * Initialize the main swarm..
     * 
     * @see MultiPopulationBasedAlgorithm#performInitialisation()
     */
    @Override
    public void performInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions())
            this.mainSwarm.addStoppingCondition(stoppingCondition);

        this.mainSwarm.setOptimisationProblem(getOptimisationProblem());

        this.mainSwarm.performInitialisation();
    }

    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * There is no best solution associated with a top level NicheAlgorithm algorithm.
     * 
     * @see #getSolutions()
     */
    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    /**
     * Get the solutions of the the optimization. The solutions are the best
     * entities within each identified niche.
     * 
     * @return The list of best solutions for each niche.
     */
    @Override
    public java.util.List<OptimisationSolution> getSolutions() {
        java.util.List<OptimisationSolution> list = Lists.newArrayList();
        for (PopulationBasedAlgorithm pba : subPopulationsAlgorithms) {
            list.add(pba.getBestSolution());
        }
        return list;
    }

    /**
     * Getters and setters for the strategies.
     */
    public PopulationBasedAlgorithm getMainSwarm() {
        return this.mainSwarm;
    }

    public void setMainSwarm(PopulationBasedAlgorithm mainSwarm) {
        this.mainSwarm = mainSwarm;
    }

    public Particle getMainSwarmParticle() {
        return mainSwarmParticle;
    }

    public void setMainSwarmParticle(Particle mainSwarmParticle) {
        this.mainSwarmParticle = mainSwarmParticle;
    }

    public MergeDetection getMergeDetection() {
        return mergeDetection;
    }

    public void setMergeDetection(MergeDetection mergeDetection) {
        this.mergeDetection = mergeDetection;
    }

    public MergeStrategy getMainSwarmMergeStrategy() {
        return mainSwarmMergeStrategy;
    }

    public void setMainSwarmMergeStrategy(MergeStrategy mainSwarmMergeStrategy) {
        this.mainSwarmMergeStrategy = mainSwarmMergeStrategy;
    }

    public MergeStrategy getSubSwarmsMergeStrategy() {
        return subSwarmsMergeStrategy;
    }

    public void setSubSwarmsMergeStrategy(MergeStrategy subSwarmsMergeStrategy) {
        this.subSwarmsMergeStrategy = subSwarmsMergeStrategy;
    }

    public MergeDetection getAbsorptionDetection() {
        return absorptionDetection;
    }

    public void setAbsorptionDetection(MergeDetection absorptionDetection) {
        this.absorptionDetection = absorptionDetection;
    }

    public MergeStrategy getMainSwarmAbsorptionStrategy() {
        return mainSwarmAbsorptionStrategy;
    }

    public void setMainSwarmAbsorptionStrategy(MergeStrategy mainSwarmAbsorptionStrategy) {
        this.mainSwarmAbsorptionStrategy = mainSwarmAbsorptionStrategy;
    }

    public MergeStrategy getSubSwarmsAbsorptionStrategy() {
        return subSwarmsAbsorptionStrategy;
    }

    public void setSubSwarmsAbsorptionStrategy(MergeStrategy subSwarmsAbsorptionStrategy) {
        this.subSwarmsAbsorptionStrategy = subSwarmsAbsorptionStrategy;
    }

    public NicheDetection getNicheDetection() {
        return nicheDetection;
    }

    public void setNicheDetection(NicheDetection nicheDetection) {
        this.nicheDetection = nicheDetection;
    }

    public NicheCreationStrategy getNicheCreationStrategy() {
        return nicheCreationStrategy;
    }

    public void setNicheCreationStrategy(NicheCreationStrategy swarmCreationStrategy) {
        this.nicheCreationStrategy = swarmCreationStrategy;
    }

    public MergeStrategy getMainSwarmPostCreation() {
        return mainSwarmPostCreation;
    }

    public void setMainSwarmPostCreation(MergeStrategy mainSwarmPostCreation) {
        this.mainSwarmPostCreation = mainSwarmPostCreation;
    }

    public void setIterationStrategy(IterationStrategy<NicheAlgorithm> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public IterationStrategy<NicheAlgorithm> getIterationStrategy() {
        return iterationStrategy;
    }
}
