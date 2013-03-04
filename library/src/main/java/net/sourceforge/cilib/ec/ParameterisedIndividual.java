/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * This is an extended Individual that aims to adapt the parameters of a Differential Evolution algorithm.
 * It holds the Trial Vector Creation Strategy, the Crossover Strategy, the bounds for each parameter and
 * an extra parameter required by the SaDDE algorithm: total number of offspring. The parameters of the
 * algorithm are adapted by adapting a parameterHoldingIndividual in the same manner as the individual itself.
 * It can be thought of as an extension to the individual.
 */

public class ParameterisedIndividual extends Individual{
    protected CreationStrategy trialVectorCreationStrategy;
    protected CrossoverStrategy crossoverStrategy;
    protected Individual parameterHoldingIndividual;
    protected int totalOffspring;
    private BoundaryConstraint parameterConstraint;
    private Bounds scalingFactorBounds;
    private Bounds crossoverProbabilityBounds;
    private Bounds totalOffspringBounds;

    /*
     * Default constructor for ParameterisedIndividual
     */
    public ParameterisedIndividual() {
         super();
         this.trialVectorCreationStrategy = new RandCreationStrategy();
         this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
         parameterHoldingIndividual = new Individual();
         totalOffspring = 1;
         parameterConstraint = new ClampingBoundaryConstraint();
         scalingFactorBounds = new Bounds(0.3, 0.9);
         crossoverProbabilityBounds = new Bounds(0.9, 1.0);
         totalOffspringBounds = new Bounds(3.0,7.0);
    }

    /*
     * Copy constructor for ParameterisedIndividual
     * @param copy The ParameterisedIndividual to be copied
     */
    public ParameterisedIndividual(ParameterisedIndividual copy) {
         super(copy);
         this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
         this.crossoverStrategy = copy.crossoverStrategy.getClone();
         parameterHoldingIndividual = copy.parameterHoldingIndividual.getClone();
         totalOffspring = copy.totalOffspring;
         parameterConstraint = copy.parameterConstraint.getClone();
         scalingFactorBounds = copy.scalingFactorBounds;
         crossoverProbabilityBounds = copy.crossoverProbabilityBounds;
         totalOffspringBounds = copy.totalOffspringBounds;
    }

    /*
     * Clone method for ParameterisedIndividual
     * @return A new instance of this ParameterisedIndividual
     */
    @Override
    public ParameterisedIndividual getClone() {
        return new ParameterisedIndividual(this);
    }

    /**
     * Initialises the individual according to its initialisation strategy and the parameterHoldingIndividual
     * along with the parameters held by the various strategies.
     */
    @Override
    public void initialise(Problem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresentation()).buildRandom());

        this.initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);

        Vector strategy = Vector.fill(0.0, this.getCandidateSolution().size());

        this.getProperties().put(EntityType.STRATEGY_PARAMETERS, strategy);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());

        //Parameters: Scaling Factor, Crossover Probability, Total Offspring
        parameterHoldingIndividual = new Individual();
        parameterHoldingIndividual.setCandidateSolution(Vector.of(Real.valueOf(0,scalingFactorBounds),
                Real.valueOf(0, crossoverProbabilityBounds),Real.valueOf(0, totalOffspringBounds)));
        parameterHoldingIndividual.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(Real.valueOf(0,scalingFactorBounds),
                Real.valueOf(0, crossoverProbabilityBounds),Real.valueOf(0, totalOffspringBounds)));
        parameterHoldingIndividual.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(Real.valueOf(0,scalingFactorBounds),
                Real.valueOf(0, crossoverProbabilityBounds),Real.valueOf(0, totalOffspringBounds)));

        initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, parameterHoldingIndividual);
        initialisationStrategy.initialise(EntityType.Particle.VELOCITY, parameterHoldingIndividual);
        initialisationStrategy.initialise(EntityType.Particle.BEST_POSITION, parameterHoldingIndividual);

        trialVectorCreationStrategy.setScaleParameter(((Vector) parameterHoldingIndividual.getCandidateSolution()).get(0).doubleValue());
        crossoverStrategy.setCrossoverPointProbability(((Vector) parameterHoldingIndividual.getCandidateSolution()).get(1).doubleValue());
        totalOffspring = ((Vector) parameterHoldingIndividual.getCandidateSolution()).get(2).intValue();
    }

    /**
     * Create a textual representation of the current {@linkplain Individual}. The
     * returned {@linkplain String} will contain both the genotypes and phenotypes for
     * the current {@linkplain Individual}.
     * @return The textual representation of this {@linkplain Individual}.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getCandidateSolution().toString());
        str.append(parameterHoldingIndividual.getCandidateSolution().toString());
        str.append(getProperties().get(EntityType.STRATEGY_PARAMETERS));
        return str.toString();
    }

    /*
     * Gets the Trial Vector Creation Strategy
     * @return The Trial Vector Creation Strategy
     */
    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

    /*
     * Sets the Trial Vector Creation Strategy
     * @param The new Trial Vector Creation Strategy
     */
    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }

    /*
     * Gets the Crossover Strategy
     * @return The Crossover Strategy
     */
    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    /*
     * Sets the Crossover Strategy
     * @param The new Crossover Strategy
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    /*
     * Gets the Parameter Holding Individual
     * @return The Parameter Holding Individual
     */
    public Individual getParameterHoldingIndividual() {
        return parameterHoldingIndividual;
    }

    /*
     * Sets the Parameter Holding Individual and all parameters
     * @param The new Parameter Holding Individual
     */
    public void setParameterHoldingIndividual(Individual parameterHoldingIndividual) {
        parameterConstraint.enforce(parameterHoldingIndividual);
        this.parameterHoldingIndividual = parameterHoldingIndividual;
        trialVectorCreationStrategy.setScaleParameter(((Vector) parameterHoldingIndividual.getCandidateSolution()).get(0).doubleValue());
        crossoverStrategy.setCrossoverPointProbability(((Vector) parameterHoldingIndividual.getCandidateSolution()).get(1).doubleValue());
        totalOffspring = ((Vector) parameterHoldingIndividual.getCandidateSolution()).get(2).intValue();
    }

    /*
     * Gets the Total Number of Offspring
     * @return The Total Number of Offspring
     */
    public int getTotalOffspring() {
        return totalOffspring;
    }

     /*
     * Sets the Total Number of Offspring
     * @param The new Total Number of Offspring
     */
    public void setTotalOffspring(int totalOffspring) {
        this.totalOffspring = totalOffspring;
    }

    /*
     * Gets the Parameter Boundary Constraint
     * @return The Parameter Boundary Constraint
     */
    public BoundaryConstraint getParameterConstraint() {
        return parameterConstraint;
    }

    /*
     * Sets the Parameter Boundary Constraint
     * @param The new Parameter Boundary Constraint
     */
    public void setParameterConstraint(BoundaryConstraint parameterConstraint) {
        this.parameterConstraint = parameterConstraint;
    }

    /*
     * Gets the Scaling Factor Bounds
     * @return The Scaling Factor Bounds
     */
    public Bounds getScalingFactorBounds() {
        return scalingFactorBounds;
    }

    /*
     * Sets the Scaling Factor Bounds
     * @param The new Scaling Factor Bounds
     */
    public void setScalingFactorLowerBound(double lowerBound) {
        this.scalingFactorBounds = new Bounds(lowerBound, scalingFactorBounds.getUpperBound());
    }

    /*
     * Sets the Scaling Factor Bounds
     * @param The new Scaling Factor Bounds
     */
    public void setScalingFactorUpperBound(double upperBound) {
        this.scalingFactorBounds = new Bounds(scalingFactorBounds.getLowerBound(), upperBound);
    }

    /*
     * Gets the Crossover Probability Bounds
     * @return The Crossover Probability Bounds
     */
    public Bounds getCrossoverProbabilityBounds() {
        return crossoverProbabilityBounds;
    }

    /*
     * Sets the Crossover Probability Bounds
     * @param The new Crossover Probability Bounds
     */
    public void setCrossoverProbabilityLowerBound(double bound) {
        this.crossoverProbabilityBounds = new Bounds(bound, crossoverProbabilityBounds.getUpperBound());;
    }

    /*
     * Sets the Crossover Probability Bounds
     * @param The new Crossover Probability Bounds
     */
    public void setCrossoverProbabilityUpperBound(double bound) {
        this.crossoverProbabilityBounds = new Bounds(crossoverProbabilityBounds.getLowerBound(), bound);
    }

    /*
     * Gets the Total Offspring Bounds
     * @return The Total Offspring Bounds
     */
    public Bounds getTotalOffspringBounds() {
        return totalOffspringBounds;
    }

    /*
     * Sets the Total Offspring Bounds
     * @param The new Total Offspring Bounds
     */
    public void setTotalOffspringLowerBound(double bound) {
        this.totalOffspringBounds = new Bounds(bound, totalOffspringBounds.getUpperBound());
    }

    /*
     * Sets the Total Offspring Bounds
     * @param The new Total Offspring Bounds
     */
    public void setTotalOffspringUpperBound(double bound) {
        this.totalOffspringBounds = new Bounds(totalOffspringBounds.getLowerBound(), bound);
    }

}
