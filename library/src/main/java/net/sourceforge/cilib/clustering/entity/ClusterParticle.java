/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.clustering.StandardDataClusteringPositionProvider;
import net.sourceforge.cilib.clustering.StandardDataClusteringVelocityProvider;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomBoundedInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.StandardCentroidInitialisationStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.pbestupdate.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * This is an entity developed to deal with CentroidHolders
 */
public class ClusterParticle extends AbstractParticle{

    private ClusterParticle neighbourhoodBest;
    private int numberOfClusters;
    private InitialisationStrategy centroidInitialisationStrategyCandidate;
    private InitialisationStrategy centroidInitialisationStrategyBest;
    private InitialisationStrategy centroidInitialisationStrategyVelocity;
    private boolean isCharged;

    /*
     * The default constructor of the ClusterParticle
     */
    public ClusterParticle() {
        super();
        super.positionInitialisationStrategy = new RandomBoundedInitialisationStrategy<>();
        super.velocityInitialisationStrategy = new RandomBoundedInitialisationStrategy<>();
        super.personalBestInitialisationStrategy = new RandomBoundedInitialisationStrategy<>();
        personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
        centroidInitialisationStrategyCandidate = new StandardCentroidInitialisationStrategy();
        centroidInitialisationStrategyBest = new StandardCentroidInitialisationStrategy();
        centroidInitialisationStrategyVelocity = new StandardCentroidInitialisationStrategy();
        numberOfClusters = 1;
        isCharged = false;

        ((StandardParticleBehaviour) behaviour).setVelocityProvider(new StandardDataClusteringVelocityProvider());
        ((StandardParticleBehaviour) behaviour).setPositionProvider(new StandardDataClusteringPositionProvider());
    }

    /*
     * The copy constructor of the ClusterParticle
     * @param copy The ClusterParticle to be copied
     */
    public ClusterParticle(ClusterParticle copy) {
        super(copy);
        personalBestUpdateStrategy = copy.personalBestUpdateStrategy.getClone();
        centroidInitialisationStrategyCandidate = copy.centroidInitialisationStrategyCandidate.getClone();
        centroidInitialisationStrategyBest = copy.centroidInitialisationStrategyBest.getClone();
        centroidInitialisationStrategyVelocity = copy.centroidInitialisationStrategyVelocity.getClone();
        numberOfClusters = copy.numberOfClusters;
        isCharged = copy.isCharged;
    }

    /*
     * The clone method of the ClusterParticle
     */
    @Override
    public ClusterParticle getClone() {
        return new ClusterParticle(this);
    }

    /*
     * Updates the fitness of the ClusterParticle\
     * Updates the Personal Best values
     * 
     * @param newFitness The new fitness to update with.
     */
    @Override
    public void updateFitness(Fitness newFitness) {
        super.updateFitness(newFitness);
        this.personalBestUpdateStrategy.updatePersonalBest(this);
    }

    /*
     * Returns the best fitness acquired so far
     * @return fitness the fitness of the particle's Personal Best position
     */
    @Override
    public Fitness getBestFitness() {
        return (Fitness) get(Property.BEST_FITNESS);
    }

    /*
     * Returns the best position acquired by the ClusterParticle so far
     * @return The Personal Best position
     */
    @Override
    public CentroidHolder getBestPosition() {
        return (CentroidHolder) get(Property.BEST_POSITION);
    }

    /*
     * Returns the velocity of the ClusterParticle
     * @return The velocity of the ClusterParticle
     */
    @Override
    public CentroidHolder getVelocity() {
        return (CentroidHolder) get(Property.VELOCITY);
    }

    /*
     * Returns the best particle in the neighbourhood of the ClusterParticle
     * @return neighbourhoodBest The Neighbourhood Best particle
     */
    @Override
    public ClusterParticle getNeighbourhoodBest() {
        return neighbourhoodBest;
    }

    /*
     * Initialises the ClusterParticle
     * @param problem The optimisation problem being dealt with
     */
    @Override
    public void initialise(Problem problem) {	
        numberOfClusters = ((ClusteringProblem) problem).getNumberOfClusters();

        put(Property.CANDIDATE_SOLUTION, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        put(Property.BEST_POSITION, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        put(Property.VELOCITY, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));

        if(centroidInitialisationStrategyCandidate instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) centroidInitialisationStrategyCandidate).setInitialisationStrategy(positionInitialisationStrategy);
        centroidInitialisationStrategyCandidate.initialise(Property.CANDIDATE_SOLUTION, this);

        put(Property.BEST_POSITION, getPosition());

        if(centroidInitialisationStrategyVelocity instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) centroidInitialisationStrategyVelocity).setInitialisationStrategy(velocityInitialisationStrategy);
        centroidInitialisationStrategyVelocity.initialise(Property.VELOCITY, this);

        if(centroidInitialisationStrategyBest instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) centroidInitialisationStrategyBest).setInitialisationStrategy(personalBestInitialisationStrategy);
        centroidInitialisationStrategyBest.initialise(Property.BEST_POSITION, this);

        put(Property.FITNESS, InferiorFitness.instance());
        put(Property.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        put(Property.PREVIOUS_SOLUTION, getPosition());


    }

    /*
     * Reinitialises the ClusterParticle
     */
    @Override
    public void reinitialise() {
        if(centroidInitialisationStrategyCandidate instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) this.centroidInitialisationStrategyCandidate).reinitialise(Property.CANDIDATE_SOLUTION, this);
        put(Property.BEST_POSITION, getPosition());

        if(centroidInitialisationStrategyVelocity instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) this.centroidInitialisationStrategyVelocity).reinitialise(Property.VELOCITY, this);

        put(Property.FITNESS, InferiorFitness.instance());
        put(Property.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

    }

    /*
     * Sets the Neighbourhood Best particle to the one received as a parameter
     * @param particle the best particle in the neighbourhood of the ClusterParticle
     */
    @Override
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = (ClusterParticle) particle;
    }

    /*
     * Sets the velocity of the ClusterParticle to the one received as a parameter
     * @param velocity the new velocity of the ClusterParticle
     */
    public void setVelocity(CentroidHolder holder) {
        put(Property.VELOCITY, holder);
    }

    /*
     * Sets the initialisation strategy that will be used by the ClusterParticle to initialise or reinitialise its position
     * @param strategy The new initialisation strategy
     */
    public void setCentroidInitialisationStrategy(InitialisationStrategy initialisationStrategy) {
        centroidInitialisationStrategyBest = initialisationStrategy.getClone();
        centroidInitialisationStrategyCandidate = initialisationStrategy.getClone();
        centroidInitialisationStrategyVelocity = initialisationStrategy.getClone();
    }

    /*
     * Returns the centroidInitialisationStrategy used for the Candidate Solution
     * @return strategy The initialisation strategy that will be used to initialise the Candidate solution
     */
    public InitialisationStrategy getCentroidInitialisationStrategyCandidate() {
        return centroidInitialisationStrategyCandidate;
    }

    /*
     * Returns the centroidInitialisationStrategy used for the Velocity
     * @return strategy The initialisation strategy that will be used to initialise the Velocity
     */
    public InitialisationStrategy getCentroidInitialisationStrategyVelocity() {
        return centroidInitialisationStrategyVelocity;
    }

    /*
     * Returns the centroidInitialisationStrategy used for the Best Solution
     * @return strategy The initialisation strategy that will be used to initialise the Best solution
     */
    public InitialisationStrategy getCentroidInitialisationStrategyBest() {
        return centroidInitialisationStrategyBest;
    }


}
