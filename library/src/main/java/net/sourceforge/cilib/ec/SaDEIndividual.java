/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.adaptation.ParameterAdaptationStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.SaDEParameterAdaptationStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.ControlParameterInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.RandomParameterInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;

/*
 * This is an individual that holds extends a Parametrised Individual in order to keep track of
 * the parameter adaptation strategies. It is specific to self adapting DE problems.
 */
public class SaDEIndividual extends ParameterisedIndividual{
     private ParameterAdaptationStrategy scalingFactorParameterAdaptationStrategy;
     private ParameterAdaptationStrategy crossoverProbabilityParameterAdaptationStrategy;

     private ControlParameterInitialisationStrategy scalingFactorInitialisationStrategy;
     private ControlParameterInitialisationStrategy  crossoverProbabilityInitialisationStrategy;

     private Fitness previousFitness;

     /*
      * Default constructor for SaDEIndividual
      */
     public SaDEIndividual() {
         super();
         this.trialVectorCreationStrategy = new RandCreationStrategy();
         this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
         this.scalingFactorParameterAdaptationStrategy = new SaDEParameterAdaptationStrategy();
         this.crossoverProbabilityParameterAdaptationStrategy = new SaDEParameterAdaptationStrategy();
         this.scalingFactorInitialisationStrategy = new RandomParameterInitialisationStrategy();
         this.crossoverProbabilityInitialisationStrategy = new RandomParameterInitialisationStrategy();
         this.previousFitness = this.getFitness();
     }

     /*
      * Copy constructor for SaDEIndividual
      * @param copy The SaDEIndividual to be copied
      */
     public SaDEIndividual(SaDEIndividual copy) {
         super(copy);
         this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
         this.crossoverStrategy = copy.crossoverStrategy.getClone();
         this.scalingFactorParameterAdaptationStrategy = copy.scalingFactorParameterAdaptationStrategy.getClone();
         this.crossoverProbabilityParameterAdaptationStrategy = copy.crossoverProbabilityParameterAdaptationStrategy.getClone();
         this.scalingFactorInitialisationStrategy = copy.scalingFactorInitialisationStrategy.getClone();
         this.crossoverProbabilityInitialisationStrategy = copy.crossoverProbabilityInitialisationStrategy.getClone();
         this.previousFitness = copy.previousFitness.getClone();
     }

     /*
      * Clone method of SaDEIndividual
      * @return A new instance of this SaDEIndividual
      */
     @Override
     public SaDEIndividual getClone() {
         return new SaDEIndividual(this);
     }

     /*
      * Initialises the individual and the parameters
      * @param problem The problem being solved by the algorithm
      */
     @Override
     public void initialise(Problem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresentation()).buildRandom());

        this.initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);

        Vector strategy = Vector.fill(0.0, this.getCandidateSolution().size());

        this.getProperties().put(EntityType.STRATEGY_PARAMETERS, strategy);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());

         scalingFactorInitialisationStrategy.initialise((SettableControlParameter) trialVectorCreationStrategy.getScaleParameter());


         crossoverProbabilityInitialisationStrategy.initialise((SettableControlParameter) crossoverStrategy.getCrossoverPointProbability());
     }

     /*
      * Calls the change methods for each parameter which will then adapt or remain the same
      * depending on the type of parameter and the outcomes of the changing algorithms
      */
     public void updateParameters() {
         crossoverProbabilityParameterAdaptationStrategy.change((SettableControlParameter) crossoverStrategy.getCrossoverPointProbability());
         scalingFactorParameterAdaptationStrategy.change((SettableControlParameter) trialVectorCreationStrategy.getScaleParameter());
     }

     /*
      * Informs the adaptation strategies of the acceptance or rejection of the parameter
      * @param accepted Whether the parameter was accepted/rejected
      * @param acceptedEntity The entity that was accepted/rejected
      */
    public void acceptParameters(boolean accepted, Entity acceptedEntity) {
        scalingFactorParameterAdaptationStrategy.accepted((SettableControlParameter) trialVectorCreationStrategy.getScaleParameter(),
                acceptedEntity, accepted);
        crossoverProbabilityParameterAdaptationStrategy.accepted((SettableControlParameter) crossoverStrategy.getCrossoverPointProbability(),
                acceptedEntity, accepted);
    }

    /*
     * Returns the trialVectorCreationStrategy held by the individual
     * @return The trialVectorCreationStrategy
     */
    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

    /*
     * Sets the trialVectorCreationStrategy to the one received as a parameter
     * @param trialVectorCreationStrategy The new trial vector creation strategy
     */
    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }

    /*
     * Returns the crossoverStrategy held by the individual
     * @return The crossoverStrategy
     */
    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    /*
     * Sets the crossoverStrategy to the one received as a parameter
     * @param crossoverStrategy The new crossoverStrategy
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    /*
     * Returns the parameter adaptation strategy used for the scaling factor
     * @return The scalingFactorParameterAdaptationStrategy
     */
    public ParameterAdaptationStrategy getScalingFactorParameterAdaptationStrategy() {
        return scalingFactorParameterAdaptationStrategy;
    }

    /*
     * Sets the scalingFactorParameterAdaptationStrategy to the one received as a parameter
     * @param scalingFactorParameterAdaptationStrategy The new scalingFactorParameterAdaptationStrategy
     */
    public void setScalingFactorParameterAdaptationStrategy(ParameterAdaptationStrategy scalingFactorParameterAdaptationStrategy) {
        this.scalingFactorParameterAdaptationStrategy = scalingFactorParameterAdaptationStrategy;
    }

     /*
     * Returns the parameter adaptation strategy used for the crossover probability
     * @return The crossoverProbabilityParameterAdaptationStrategy
     */
    public ParameterAdaptationStrategy getCrossoverProbabilityParameterAdaptationStrategy() {
        return crossoverProbabilityParameterAdaptationStrategy;
    }

    /*
     * Sets the crossoverProbabilityParameterAdaptationStrategy to the one received as a parameter
     * @param crossoverProbabilityParameterAdaptationStrategy The new crossoverProbabilityParameterAdaptationStrategy
     */
    public void setCrossoverProbabilityParameterAdaptationStrategy(ParameterAdaptationStrategy crossoverProbabilityParameterAdaptationStrategy) {
        this.crossoverProbabilityParameterAdaptationStrategy = crossoverProbabilityParameterAdaptationStrategy;
    }

    /*
     * Returns the initialisation strategy used for the scaling factor
     * @return The scalingFactorInitialisationStrategy
     */
    public ControlParameterInitialisationStrategy getScalingFactorInitialisationStrategy() {
        return scalingFactorInitialisationStrategy;
    }

    /*
     * Sets the scalingFactorInitialisationStrategy to the one received as a parameter
     * @param scalingFactorInitialisationStrategy The new scalingFactorInitialisationStrategy
     */
    public void setScalingFactorInitialisationStrategy(ControlParameterInitialisationStrategy scalingFactorInitialisationStrategy) {
        this.scalingFactorInitialisationStrategy = scalingFactorInitialisationStrategy;
    }

    /*
     * Returns the initialisation strategy used for the scaling factor
     * @return The crossoverProbabilityInitialisationStrategy
     */
    public ControlParameterInitialisationStrategy getCrossoverProbabilityInitialisationStrategy() {
        return crossoverProbabilityInitialisationStrategy;
    }

    /*
     * Sets the crossoverProbabilityInitialisationStrategy to the one received as a parameter
     * @param crossoverProbabilityInitialisationStrategy The new crossoverProbabilityInitialisationStrategy
     */
    public void setCrossoverProbabilityInitialisationStrategy(ControlParameterInitialisationStrategy crossoverProbabilityInitialisationStrategy) {
        this.crossoverProbabilityInitialisationStrategy = crossoverProbabilityInitialisationStrategy;
    }

    /*
     * Returns the previous fitness value
     * @param The previous fitness
     */
    public Fitness getPreviousFitness() {
        return previousFitness;
    }

    /*
     * Sets the previous fitness value to the one received as a parameter
     * @param previousFitness The new previousFitness
     */
    public void setPreviousFitness(Fitness previousFitness) {
        this.previousFitness = previousFitness;
    }

}
