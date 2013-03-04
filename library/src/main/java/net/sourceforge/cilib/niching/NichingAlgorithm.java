/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialisation.RandomInitialisationStrategy;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.niching.creation.ClosestNeighbourNicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.MaintainedFitnessNicheDetection;
import net.sourceforge.cilib.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.NicheDetection;
import net.sourceforge.cilib.niching.iterationstrategies.NichePSO;
import net.sourceforge.cilib.niching.iterators.AllSwarmsIterator;
import net.sourceforge.cilib.niching.iterators.NicheIteration;
import net.sourceforge.cilib.niching.iterators.SingleNicheIteration;
import net.sourceforge.cilib.niching.iterators.SubswarmIterator;
import net.sourceforge.cilib.niching.merging.MergeStrategy;
import net.sourceforge.cilib.niching.merging.SingleSwarmMergeStrategy;
import net.sourceforge.cilib.niching.merging.StandardMergeStrategy;
import net.sourceforge.cilib.niching.merging.detection.MergeDetection;
import net.sourceforge.cilib.niching.merging.detection.RadiusOverlapMergeDetection;
import net.sourceforge.cilib.problem.boundaryconstraint.ReinitialisationBoundary;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Int;

/**
 * <p>
 * Generalized NichingAlgorithm algorithm.
 * </p>
 * <p>
 * This class is intended to be the base class (or even the only class) for all
 * algorithms implementing a form of niching.
 * </p>
 * <pre>
 * {@literal @}inproceedings{}
 * </pre>
 */
public class NichingAlgorithm extends MultiPopulationBasedAlgorithm implements PopulationBasedAlgorithm {
    private static final long serialVersionUID = 3575627467034673738L;

    protected IterationStrategy<NichingAlgorithm> iterationStrategy;
    protected PopulationBasedAlgorithm mainSwarm;
    protected Entity entityType;

    protected NicheIteration mainSwarmIterator;
    protected SubswarmIterator subSwarmIterator;

    protected NicheDetection nicheDetector;
    protected NicheCreationStrategy nicheCreator;
    protected MergeStrategy mainSwarmCreationMerger;

    protected MergeStrategy subSwarmMerger;
    protected MergeStrategy mainSwarmMerger;
    protected MergeDetection mergeDetector;

    protected MergeStrategy mainSwarmAbsorber;
    protected MergeStrategy subSwarmAbsorber;
    protected MergeDetection absorptionDetector;

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
     *  <li>Creation strategy: standard creation i.e. a swarm is created with a particle and its closest neighbour</li>
     * </ul>
     * </p>
     */
    public NichingAlgorithm() {
        this.mainSwarm = new PSO();
        StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
        velocityUpdateStrategy.setSocialAcceleration(ConstantControlParameter.of(0.0));
        velocityUpdateStrategy.setCognitiveAcceleration(ConstantControlParameter.of(1.2));

        Particle particle = new StandardParticle();
        particle.setVelocityInitialisationStrategy(new RandomInitialisationStrategy());
        particle.setVelocityProvider(velocityUpdateStrategy);
        this.entityType = particle;

        ((ClonedPopulationInitialisationStrategy) ((PSO) this.mainSwarm).getInitialisationStrategy()).setEntityType(entityType);
        ((SynchronousIterationStrategy) ((PSO) this.mainSwarm).getIterationStrategy()).setBoundaryConstraint(new ReinitialisationBoundary());

        this.nicheDetector = new MaintainedFitnessNicheDetection();
        this.nicheCreator = new ClosestNeighbourNicheCreationStrategy();
        this.mainSwarmCreationMerger = new SingleSwarmMergeStrategy();

        this.mainSwarmAbsorber = new SingleSwarmMergeStrategy();
        this.subSwarmAbsorber = new StandardMergeStrategy();
        this.absorptionDetector = new RadiusOverlapMergeDetection();

        this.subSwarmMerger = new StandardMergeStrategy();
        this.mainSwarmMerger = new SingleSwarmMergeStrategy();
        this.mergeDetector = new RadiusOverlapMergeDetection();

        this.iterationStrategy = new NichePSO();
        this.mainSwarmIterator = new SingleNicheIteration();
        this.subSwarmIterator = new AllSwarmsIterator();
        this.subSwarmIterator.setIterator(new SingleNicheIteration());
    }

    /**
     * Copy constructor.
     */
    public NichingAlgorithm(NichingAlgorithm copy) {
        super(copy);

        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.mainSwarm = copy.mainSwarm.getClone();
        this.entityType = copy.entityType.getClone();

        this.nicheDetector = copy.nicheDetector;
        this.nicheCreator = copy.nicheCreator;
        this.mainSwarmCreationMerger = copy.mainSwarmCreationMerger;

        this.mainSwarmAbsorber = copy.mainSwarmAbsorber;
        this.subSwarmAbsorber = copy.subSwarmAbsorber;
        this.absorptionDetector = copy.absorptionDetector;

        this.subSwarmMerger = copy.subSwarmMerger;
        this.mainSwarmMerger = copy.mainSwarmMerger;
        this.mergeDetector = copy.mergeDetector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NichingAlgorithm getClone() {
        return new NichingAlgorithm(this);
    }

    /**
     * Initialise the main swarm..
     *
     * @see MultiPopulationBasedAlgorithm#performInitialisation()
     */
    @Override
    public void algorithmInitialisation() {
        for (StoppingCondition stoppingCondition : getStoppingConditions()) {
            this.mainSwarm.addStoppingCondition(stoppingCondition);
        }

        this.mainSwarm.setOptimisationProblem(getOptimisationProblem());

        this.mainSwarm.performInitialisation();

        for (Entity e : mainSwarm.getTopology()) {
            e.getProperties().put(EntityType.Coevolution.POPULATION_ID, Int.valueOf(0));
        }

        this.entityType = this.mainSwarm.getTopology().get(0);
    }

    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * There is no best solution associated with a top level NichingAlgorithm algorithm.
     *
     * @see #getSolutions()
     */
    @Override
    public OptimisationSolution getBestSolution() {
        throw new UnsupportedOperationException("Niching algorithms do not have a single solution.");
    }

    /**
     * Get the solutions of the the optimisation. The solutions are the best
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

    public Entity getEntityType() {
        return entityType;
    }

    public MergeDetection getMergeDetector() {
        return mergeDetector;
    }

    public void setMergeDetector(MergeDetection mergeDetector) {
        this.mergeDetector = mergeDetector;
    }

    public MergeStrategy getMainSwarmMerger() {
        return mainSwarmMerger;
    }

    public void setMainSwarmMerger(MergeStrategy mainSwarmMerger) {
        this.mainSwarmMerger = mainSwarmMerger;
    }

    public MergeStrategy getSubSwarmMerger() {
        return subSwarmMerger;
    }

    public void setSubSwarmMerger(MergeStrategy subSwarmMerger) {
        this.subSwarmMerger = subSwarmMerger;
    }

    public MergeDetection getAbsorptionDetector() {
        return absorptionDetector;
    }

    public void setAbsorptionDetector(MergeDetection absorptionDetector) {
        this.absorptionDetector = absorptionDetector;
    }

    public MergeStrategy getMainSwarmAbsorber() {
        return mainSwarmAbsorber;
    }

    public void setMainSwarmAbsorber(MergeStrategy mainSwarmAbsorber) {
        this.mainSwarmAbsorber = mainSwarmAbsorber;
    }

    public MergeStrategy getSubSwarmAbsorber() {
        return subSwarmAbsorber;
    }

    public void setSubSwarmAbsorber(MergeStrategy subSwarmAbsorber) {
        this.subSwarmAbsorber = subSwarmAbsorber;
    }

    public NicheDetection getNicheDetector() {
        return nicheDetector;
    }

    public void setNicheDetector(NicheDetection nicheDetector) {
        this.nicheDetector = nicheDetector;
    }

    public NicheCreationStrategy getNicheCreator() {
        return nicheCreator;
    }

    public void setNicheCreator(NicheCreationStrategy swarmCreationStrategy) {
        this.nicheCreator = swarmCreationStrategy;
    }

    public MergeStrategy getMainSwarmCreationMerger() {
        return mainSwarmCreationMerger;
    }

    public void setMainSwarmCreationMerger(MergeStrategy mainSwarmCreationMerger) {
        this.mainSwarmCreationMerger = mainSwarmCreationMerger;
    }

    public void setIterationStrategy(IterationStrategy<NichingAlgorithm> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public IterationStrategy<NichingAlgorithm> getIterationStrategy() {
        return iterationStrategy;
    }

    public NicheIteration getMainSwarmIterator() {
        return mainSwarmIterator;
    }

    public void setMainSwarmIterator(NicheIteration mainSwarmIterator) {
        this.mainSwarmIterator = mainSwarmIterator;
    }

    public void setSubSwarmIterator(SubswarmIterator subSwarmIterator) {
        this.subSwarmIterator = subSwarmIterator;
    }

    public SubswarmIterator getSubSwarmIterator() {
        return subSwarmIterator;
    }

    public Topology<? extends Entity> getTopology() {
        return mainSwarm.getTopology();
    }

    public Object accept(TopologyVisitor visitor) {
        return mainSwarm.accept(visitor);
    }

    public void setInitialisationStrategy(PopulationInitialisationStrategy<? extends Entity> initialisationStrategy) {
        mainSwarm.setInitialisationStrategy(initialisationStrategy);
    }

    public PopulationInitialisationStrategy getInitialisationStrategy() {
        return mainSwarm.getInitialisationStrategy();
    }
}
