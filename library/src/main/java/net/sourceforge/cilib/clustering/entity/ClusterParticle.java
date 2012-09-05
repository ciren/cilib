/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.StandardCentroidInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
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
    private InitializationStrategy centroidInitialisationStrategyCandidate;
    private InitializationStrategy centroidInitialisationStrategyBest;
    private InitializationStrategy centroidInitialisationStrategyVelocity;
    private boolean isCharged;

    /*
     * The default constructor of the ClusterParticle
     */
    public ClusterParticle() {
        super();
        super.positionInitialisationStrategy = new RandomBoundedInitializationStrategy<Particle>();
        super.velocityInitializationStrategy = new RandomBoundedInitializationStrategy<Particle>();
        super.personalBestInitialisationStrategy = new RandomBoundedInitializationStrategy<Particle>();
        personalBestUpdateStrategy = new StandardPersonalBestUpdateStrategy();
        centroidInitialisationStrategyCandidate = new StandardCentroidInitializationStrategy();
        centroidInitialisationStrategyBest = new StandardCentroidInitializationStrategy();
        centroidInitialisationStrategyVelocity = new StandardCentroidInitializationStrategy();
        numberOfClusters = 1;
        isCharged = false;
    }

    /*
     * The xopy constructor of the ClusterParticle
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
     * Returns the dimension of the ClusterParticle
     * @return size The size of the Candidate Solution
     */
    @Override
    public int getDimension() {
        return getCandidateSolution().size();
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
     * Retruns the best particle in the neighbourhood of the ClusterParticle
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
     * Initializes the ClusterParticle
     * @param problem The optimization problem being dealt with
     */
    @Override
    public void initialise(Problem problem) {
        numberOfClusters = ((ClusteringProblem) problem).getNumberOfClusters();
        
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        this.getProperties().put(EntityType.Particle.BEST_POSITION,  new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        this.getProperties().put(EntityType.Particle.VELOCITY,  new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));

        if(centroidInitialisationStrategyCandidate instanceof StandardCentroidInitializationStrategy)
            ((StandardCentroidInitializationStrategy) centroidInitialisationStrategyCandidate).setInitialisationStrategy(positionInitialisationStrategy);
        centroidInitialisationStrategyCandidate.initialize(EntityType.CANDIDATE_SOLUTION, this);

        getProperties().put(EntityType.Particle.BEST_POSITION, getCandidateSolution());

        if(centroidInitialisationStrategyVelocity instanceof StandardCentroidInitializationStrategy)
            ((StandardCentroidInitializationStrategy) centroidInitialisationStrategyVelocity).setInitialisationStrategy(velocityInitializationStrategy);
        centroidInitialisationStrategyVelocity.initialize(EntityType.Particle.VELOCITY, this);

        if(centroidInitialisationStrategyBest instanceof StandardCentroidInitializationStrategy)
            ((StandardCentroidInitializationStrategy) centroidInitialisationStrategyBest).setInitialisationStrategy(personalBestInitialisationStrategy);
        centroidInitialisationStrategyBest.initialize(EntityType.Particle.BEST_POSITION, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        this.getProperties().put(EntityType.PREVIOUS_SOLUTION, getCandidateSolution());


    }

    /*
     * Reinitializes the ClusterParticle
     */
    @Override
    public void reinitialise() {
        if(centroidInitialisationStrategyCandidate instanceof StandardCentroidInitializationStrategy)
            ((StandardCentroidInitializationStrategy) this.centroidInitialisationStrategyCandidate).reinitialize(EntityType.CANDIDATE_SOLUTION, this);
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getCandidateSolution());

        if(centroidInitialisationStrategyVelocity instanceof StandardCentroidInitializationStrategy)
            ((StandardCentroidInitializationStrategy) this.centroidInitialisationStrategyVelocity).reinitialize(EntityType.Particle.VELOCITY, this);

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
     * Sets the initialization strategy that will be used by the ClusterParticle to initialize or reinitialize its position
     * @param strategy The new initialization strategy
     */
    public void setCentroidInitialisationStrategy(InitializationStrategy initializationStrategy) {
        centroidInitialisationStrategyBest = initializationStrategy.getClone();
        centroidInitialisationStrategyCandidate = initializationStrategy.getClone();
        centroidInitialisationStrategyVelocity = initializationStrategy.getClone();
    }

    /*
     * Returns the centroidInitializationStrategy used for the Candidate Solution
     * @return strategy The initialization strategy that will be used to initialize the Candidate solution
     */
    public InitializationStrategy getCentroidInitializationStrategyCandidate() {
        return centroidInitialisationStrategyCandidate;
    }

    /*
     * Returns the centroidInitializationStrategy used for the Velocity
     * @return strategy The initialization strategy that will be used to initialize the Velocity
     */
    public InitializationStrategy getCentroidInitializationStrategyVelocity() {
        return centroidInitialisationStrategyVelocity;
    }

    /*
     * Returns the centroidInitializationStrategy used for the Best Solution
     * @return strategy The initialization strategy that will be used to initialize the Best solution
     */
    public InitializationStrategy getCentroidInitializationStrategyBest() {
        return centroidInitialisationStrategyBest;
    }


}
