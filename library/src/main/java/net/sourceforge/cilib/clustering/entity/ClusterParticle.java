/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomBoundedInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.StandardCentroidInitialisationStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.pbestupdate.StandardPersonalBestUpdateStrategy;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;

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
        super.positionInitialisationStrategy = new RandomBoundedInitialisationStrategy<Particle>();
        super.velocityInitialisationStrategy = new RandomBoundedInitialisationStrategy<Particle>();
        super.personalBestInitialisationStrategy = new RandomBoundedInitialisationStrategy<Particle>();
        personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
        centroidInitialisationStrategyCandidate = new StandardCentroidInitialisationStrategy();
        centroidInitialisationStrategyBest = new StandardCentroidInitialisationStrategy();
        centroidInitialisationStrategyVelocity = new StandardCentroidInitialisationStrategy();
        numberOfClusters = 1;
        isCharged = false;
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
     * Calculates the fitness of the ClusterParticle accordingly
     * Sets the fitness after it is calculated
     * Updates the Personal Best values
     */
    @Override
    public void calculateFitness() {

        EntityBasedFitnessCalculator f = new EntityBasedFitnessCalculator();
        Fitness fitness = f.getFitness(this);
        this.getProperties().put(EntityType.FITNESS, fitness);
        this.personalBestUpdateStrategy.updatePersonalBest(this);
    }

    /*
     * Returns the best fitness acquired so far
     * @return fitness the fitness of the particle's Personal Best position
     */
    @Override
    public Fitness getBestFitness() {
        return (Fitness) this.getProperties().get(EntityType.Particle.BEST_FITNESS);
    }

    /*
     * Returns the current position of the ClusterParticle
     * @return position The current value of the Candidate Solution
     */
    @Override
    public CentroidHolder getPosition() {
        return (CentroidHolder) getCandidateSolution();
    }

    /*
     * Returns the best position acquired by the ClusterParticle so far
     * @return The Personal Best position
     */
    @Override
    public CentroidHolder getBestPosition() {
        return (CentroidHolder) this.getProperties().get(EntityType.Particle.BEST_POSITION);
    }

    /*
     * Returns the velocity of the ClusterParticle
     * @return The velocity of the ClusterParticle
     */
    @Override
    public CentroidHolder getVelocity() {
        return (CentroidHolder) this.getProperties().get(EntityType.Particle.VELOCITY);
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
     * Changes the current position of the ClusterParticle accordingly
     */
    @Override
    public void updatePosition() {
        CentroidHolder newCandidateSolution = new CentroidHolder();
        ClusterCentroid newCentroid;
        Particle particle;
        Particle neighbourhoodBestParticle;
        int index = 0;
        for(ClusterCentroid centroid : (CentroidHolder) getCandidateSolution()) {
            particle = new StandardParticle();
            neighbourhoodBestParticle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());
            particle.getProperties().put(EntityType.Particle.VELOCITY, this.getVelocity().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, this.getBestPosition().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, this.getBestFitness());
            particle.getProperties().put(EntityType.FITNESS, this.getFitness());

            neighbourhoodBestParticle.setCandidateSolution(((CentroidHolder) getNeighbourhoodBest().getCandidateSolution()).get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.VELOCITY, getNeighbourhoodBest().getVelocity().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_POSITION, getNeighbourhoodBest().getBestPosition().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, getNeighbourhoodBest().getBestFitness());
            neighbourhoodBestParticle.getProperties().put(EntityType.FITNESS, getNeighbourhoodBest().getFitness());

            particle.setNeighbourhoodBest(neighbourhoodBestParticle);
            newCentroid = new ClusterCentroid();
            newCentroid.copy(this.behavior.getPositionProvider().get(particle));
            newCandidateSolution.add(newCentroid);
            index++;
        }

        this.setCandidateSolution(newCandidateSolution);

    }

    /*
     * Changes the velocity of the ClusterParticle accordingly
     */
    @Override
    public void updateVelocity() {
        CentroidHolder newVelocity = new CentroidHolder();
        ClusterCentroid newCentroid;
        Particle particle;
        int index = 0;
        Particle neighbourhoodBestParticle;
        for(ClusterCentroid centroid : (CentroidHolder) getCandidateSolution()) {
            particle = new StandardParticle();
            neighbourhoodBestParticle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());
            particle.getProperties().put(EntityType.Particle.VELOCITY, this.getVelocity().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, this.getBestPosition().get(index).toVector());
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, this.getBestFitness());
            particle.getProperties().put(EntityType.FITNESS, this.getFitness());

            neighbourhoodBestParticle.setCandidateSolution(((CentroidHolder) getNeighbourhoodBest().getCandidateSolution()).get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.VELOCITY, getNeighbourhoodBest().getVelocity().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_POSITION, getNeighbourhoodBest().getBestPosition().get(index).toVector());
            neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, getNeighbourhoodBest().getBestFitness());
            neighbourhoodBestParticle.getProperties().put(EntityType.FITNESS, getNeighbourhoodBest().getFitness());

            particle.setNeighbourhoodBest(neighbourhoodBestParticle);
            newCentroid = new ClusterCentroid();
            newCentroid.copy(this.behavior.getVelocityProvider().get(particle));
            newVelocity.add(newCentroid);
            index++;
        }

        getProperties().put(EntityType.Particle.VELOCITY, newVelocity);
    }

    /*
     * Initialises the ClusterParticle
     * @param problem The optimisation problem being dealt with
     */
    @Override
    public void initialise(Problem problem) {
        numberOfClusters = ((ClusteringProblem) problem).getNumberOfClusters();

        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        this.getProperties().put(EntityType.Particle.BEST_POSITION,  new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        this.getProperties().put(EntityType.Particle.VELOCITY,  new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));

        if(centroidInitialisationStrategyCandidate instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) centroidInitialisationStrategyCandidate).setInitialisationStrategy(positionInitialisationStrategy);
        centroidInitialisationStrategyCandidate.initialise(EntityType.CANDIDATE_SOLUTION, this);

        getProperties().put(EntityType.Particle.BEST_POSITION, getCandidateSolution());

        if(centroidInitialisationStrategyVelocity instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) centroidInitialisationStrategyVelocity).setInitialisationStrategy(velocityInitialisationStrategy);
        centroidInitialisationStrategyVelocity.initialise(EntityType.Particle.VELOCITY, this);

        if(centroidInitialisationStrategyBest instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) centroidInitialisationStrategyBest).setInitialisationStrategy(personalBestInitialisationStrategy);
        centroidInitialisationStrategyBest.initialise(EntityType.Particle.BEST_POSITION, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        this.getProperties().put(EntityType.PREVIOUS_SOLUTION, getCandidateSolution());


    }

    /*
     * Reinitialises the ClusterParticle
     */
    @Override
    public void reinitialise() {
        if(centroidInitialisationStrategyCandidate instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) this.centroidInitialisationStrategyCandidate).reinitialise(EntityType.CANDIDATE_SOLUTION, this);
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getCandidateSolution());

        if(centroidInitialisationStrategyVelocity instanceof StandardCentroidInitialisationStrategy)
            ((StandardCentroidInitialisationStrategy) this.centroidInitialisationStrategyVelocity).reinitialise(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
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
        this.getProperties().put(EntityType.Particle.VELOCITY, holder);
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
