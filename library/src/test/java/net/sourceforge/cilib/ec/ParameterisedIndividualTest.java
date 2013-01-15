/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import junit.framework.Assert;
import org.junit.Test;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.problem.boundaryconstraint.BouncingBoundaryConstraint;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;


public class ParameterisedIndividualTest {

    @Test
     public void initialise() {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-5.12:5.12)^30");
        problem.setFunction(new Spherical());

        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.initialise(problem);
        Assert.assertTrue(!individual.getCandidateSolution().isEmpty());
    }

    @Test
    public void getTrialVectorCreationStrategy() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        RandCreationStrategy strategy = new RandCreationStrategy();
        individual.setTrialVectorCreationStrategy(strategy);
        Assert.assertEquals(strategy, individual.getTrialVectorCreationStrategy());
    }

    @Test
    public void setTrialVectorCreationStrategy() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        RandCreationStrategy strategy = new RandCreationStrategy();
        individual.setTrialVectorCreationStrategy(strategy);
        Assert.assertEquals(strategy, individual.getTrialVectorCreationStrategy());
    }

    @Test
    public void getCrossoverStrategy() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        DifferentialEvolutionBinomialCrossover strategy = new DifferentialEvolutionBinomialCrossover();
        individual.setCrossoverStrategy(strategy);
        Assert.assertEquals(strategy, individual.getCrossoverStrategy());
    }

    @Test
    public void setCrossoverStrategy() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        DifferentialEvolutionBinomialCrossover strategy = new DifferentialEvolutionBinomialCrossover();
        individual.setCrossoverStrategy(strategy);
        Assert.assertEquals(strategy, individual.getCrossoverStrategy());
    }

    @Test
    public void getParameterHoldingIndividual() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        Individual individual2 = new Individual();
        individual2.setCandidateSolution(Vector.of(5,2,4));
        individual.setParameterHoldingIndividual(individual2);
        Assert.assertEquals(individual2, individual.getParameterHoldingIndividual());
    }

    @Test
    public void setParameterHoldingIndividual() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        Individual individual2 = new Individual();
        individual2.setCandidateSolution(Vector.of(5,2,4));
        individual.setParameterHoldingIndividual(individual2);
        Assert.assertEquals(individual2, individual.getParameterHoldingIndividual());
    }

    @Test
    public void getTotalOffspring() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setTotalOffspring(2);
        Assert.assertEquals(2, individual.getTotalOffspring());
    }

    @Test
    public void setTotalOffspring() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setTotalOffspring(2);
        Assert.assertEquals(2, individual.getTotalOffspring());
    }

    public void getParameterConstraint() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        BoundaryConstraint constraint = new BouncingBoundaryConstraint();
        individual.setParameterConstraint(constraint);
        Assert.assertEquals(constraint, individual.getParameterConstraint());
    }

    public void setParameterConstraint() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        BoundaryConstraint constraint = new BouncingBoundaryConstraint();
        individual.setParameterConstraint(constraint);
        Assert.assertEquals(constraint, individual.getParameterConstraint());
    }

    public void getScalingFactorBounds() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setScalingFactorLowerBound(0.1);
        individual.setScalingFactorUpperBound(0.3);
        Bounds bounds = individual.getScalingFactorBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void setScalingFactorBounds() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setScalingFactorLowerBound(0.1);
        individual.setScalingFactorUpperBound(0.3);
        Bounds bounds = individual.getScalingFactorBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void getCrossoverProbabilityBounds() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setCrossoverProbabilityLowerBound(0.1);
        individual.setCrossoverProbabilityUpperBound(0.3);
        Bounds bounds = individual.getCrossoverProbabilityBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void setCrossoverProbabilityBounds() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setCrossoverProbabilityLowerBound(0.1);
        individual.setCrossoverProbabilityUpperBound(0.3);
        Bounds bounds = individual.getCrossoverProbabilityBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void getTotalOffspringBounds() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setTotalOffspringLowerBound(0.1);
        individual.setTotalOffspringUpperBound(0.3);
        Bounds bounds = individual.getTotalOffspringBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void setTotalOffspringBounds() {
        ParameterisedIndividual individual = new ParameterisedIndividual();
        individual.setTotalOffspringLowerBound(0.1);
        individual.setTotalOffspringUpperBound(0.3);
        Bounds bounds = individual.getTotalOffspringBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

}
