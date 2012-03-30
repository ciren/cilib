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
import fj.P;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import static net.sourceforge.cilib.pso.niching.Niching.*;
import net.sourceforge.cilib.pso.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheDetection;
import net.sourceforge.cilib.pso.niching.creation.ClosestNeighbourNicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.MaintainedFitnessNicheDetection;
import net.sourceforge.cilib.pso.niching.merging.*;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 * <p>
 * Generalized NichePSO algorithm.
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
public class NichePSO extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = 3575627467034673738L;

    protected PopulationBasedAlgorithm mainSwarm;
    protected Particle mainSwarmParticle;

    protected NicheDetection nicheDetection;
    protected NicheCreationStrategy swarmCreationStrategy;
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
    public NichePSO() {
        this.mainSwarm = new PSO();
        ((SynchronousIterationStrategy) ((PSO) this.mainSwarm).getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setSocialAcceleration(ConstantControlParameter.of(0.0));
        velocityUpdateStrategy.setCognitiveAcceleration(ConstantControlParameter.of(1.2));
        
        this.mainSwarmParticle = new StandardParticle();
        this.mainSwarmParticle.setVelocityInitializationStrategy(new RandomInitializationStrategy());
        this.mainSwarmParticle.setVelocityProvider(velocityUpdateStrategy);

        PopulationInitialisationStrategy mainSwarmInitialisationStrategy = new ClonedPopulationInitialisationStrategy();
        mainSwarmInitialisationStrategy.setEntityType(this.mainSwarmParticle);
        mainSwarmInitialisationStrategy.setEntityNumber(20);

        this.mainSwarm.setInitialisationStrategy(mainSwarmInitialisationStrategy);

        this.nicheDetection = new MaintainedFitnessNicheDetection();
        this.swarmCreationStrategy = new ClosestNeighbourNicheCreationStrategy();
        this.mainSwarmPostCreation = new SingleSwarmMergeStrategy();

        this.mainSwarmAbsorptionStrategy = new SingleSwarmMergeStrategy();
        this.subSwarmsAbsorptionStrategy = new StandardMergeStrategy();
        this.absorptionDetection = new RadiusOverlapMergeDetection();

        this.subSwarmsMergeStrategy = new StandardMergeStrategy();
        this.mainSwarmMergeStrategy = new SingleSwarmMergeStrategy();
        this.mergeDetection = new RadiusOverlapMergeDetection();
    }
    
    /**
     * Copy constructor.
     */
    public NichePSO(NichePSO copy) {
        super(copy);
        
        this.mainSwarm = copy.mainSwarm.getClone();
        this.mainSwarmParticle = copy.mainSwarmParticle.getClone();
        
        this.nicheDetection = copy.nicheDetection;
        this.swarmCreationStrategy = copy.swarmCreationStrategy;
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
        return new NichePSO(this);
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
     *   <li>Identify any new potential niches using a {@link NicheDetection}.</li>
     *   <li>Create new sub-swarms via a {@link NicheCreationStrategy} for the identified niches.</li>
     * </ol>
     * </p>
     */
    @Override
    protected void algorithmIteration() {
        P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> newSwarms = combineSwarms
                .andThen(iterateMainSwarm)
                .andThen(iterateSubswarms)
                .andThen(merge(mergeDetection, mainSwarmMergeStrategy, subSwarmsMergeStrategy))
                .andThen(absorb(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy))
                .andThen(enforceMainSwarmTopology(mainSwarmParticle.getParticleBehavior()))
                .andThen(createNiches(nicheDetection, swarmCreationStrategy, mainSwarmPostCreation))
                .f(P.p(mainSwarm, subPopulationsAlgorithms));

        subPopulationsAlgorithms = Lists.newArrayList(newSwarms._2().toCollection());
        mainSwarm = newSwarms._1();
    }

    /**
     * There is no best solution associated with a top level NichePSO algorithm.
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

    public NicheCreationStrategy getSwarmCreationStrategy() {
        return swarmCreationStrategy;
    }

    public void setSwarmCreationStrategy(NicheCreationStrategy swarmCreationStrategy) {
        this.swarmCreationStrategy = swarmCreationStrategy;
    }

    public MergeStrategy getMainSwarmPostCreation() {
        return mainSwarmPostCreation;
    }

    public void setMainSwarmPostCreation(MergeStrategy mainSwarmPostCreation) {
        this.mainSwarmPostCreation = mainSwarmPostCreation;
    }
}
