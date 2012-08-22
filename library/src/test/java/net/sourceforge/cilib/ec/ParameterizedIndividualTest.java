/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.omg.CORBA.portable.IndirectionException;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.problem.boundaryconstraint.BouncingBoundaryConstraint;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;


public class ParameterizedIndividualTest {
    
    @Test
     public void initialise() {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-5.12:5.12)^30");
        problem.setFunction(new Spherical());
        
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.initialise(problem);
        Assert.assertTrue(!individual.getCandidateSolution().isEmpty());
    }

    @Test
    public void getTrialVectorCreationStrategy() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        RandCreationStrategy strategy = new RandCreationStrategy();
        individual.setTrialVectorCreationStrategy(strategy);
        Assert.assertEquals(strategy, individual.getTrialVectorCreationStrategy());
    }

    @Test
    public void setTrialVectorCreationStrategy() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        RandCreationStrategy strategy = new RandCreationStrategy();
        individual.setTrialVectorCreationStrategy(strategy);
        Assert.assertEquals(strategy, individual.getTrialVectorCreationStrategy());
    }

    @Test
    public void getCrossoverStrategy() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        DifferentialEvolutionBinomialCrossover strategy = new DifferentialEvolutionBinomialCrossover();
        individual.setCrossoverStrategy(strategy);
        Assert.assertEquals(strategy, individual.getCrossoverStrategy());
    }

    @Test
    public void setCrossoverStrategy() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        DifferentialEvolutionBinomialCrossover strategy = new DifferentialEvolutionBinomialCrossover();
        individual.setCrossoverStrategy(strategy);
        Assert.assertEquals(strategy, individual.getCrossoverStrategy());
    }

    @Test
    public void getParameterHoldingIndividual() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        Individual individual2 = new Individual();
        individual2.setCandidateSolution(Vector.of(5,2,4));
        individual.setParameterHoldingIndividual(individual2);
        Assert.assertEquals(individual2, individual.getParameterHoldingIndividual());
    }

    @Test
    public void setParameterHoldingIndividual() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        Individual individual2 = new Individual();
        individual2.setCandidateSolution(Vector.of(5,2,4));
        individual.setParameterHoldingIndividual(individual2);
        Assert.assertEquals(individual2, individual.getParameterHoldingIndividual());
    }

    @Test
    public void getTotalOffspring() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setTotalOffspring(2);
        Assert.assertEquals(2, individual.getTotalOffspring());
    }

    @Test
    public void setTotalOffspring() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setTotalOffspring(2);
        Assert.assertEquals(2, individual.getTotalOffspring());
    }
    
    public void getParameterConstraint() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        BoundaryConstraint constraint = new BouncingBoundaryConstraint();
        individual.setParameterConstraint(constraint);
        Assert.assertEquals(constraint, individual.getParameterConstraint());
    }

    public void setParameterConstraint() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        BoundaryConstraint constraint = new BouncingBoundaryConstraint();
        individual.setParameterConstraint(constraint);
        Assert.assertEquals(constraint, individual.getParameterConstraint());
    }

    public void getScalingFactorBounds() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setScalingFactorLowerBound(0.1);
        individual.setScalingFactorUpperBound(0.3);
        Bounds bounds = individual.getScalingFactorBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void setScalingFactorBounds() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setScalingFactorLowerBound(0.1);
        individual.setScalingFactorUpperBound(0.3);
        Bounds bounds = individual.getScalingFactorBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void getCrossoverProbabilityBounds() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setCrossoverProbabilityLowerBound(0.1);
        individual.setCrossoverProbabilityUpperBound(0.3);
        Bounds bounds = individual.getCrossoverProbabilityBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void setCrossoverProbabilityBounds() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setCrossoverProbabilityLowerBound(0.1);
        individual.setCrossoverProbabilityUpperBound(0.3);
        Bounds bounds = individual.getCrossoverProbabilityBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void getTotalOffspringBounds() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setTotalOffspringLowerBound(0.1);
        individual.setTotalOffspringUpperBound(0.3);
        Bounds bounds = individual.getTotalOffspringBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }

    public void setTotalOffspringBounds() {
        ParameterizedIndividual individual = new ParameterizedIndividual();
        individual.setTotalOffspringLowerBound(0.1);
        individual.setTotalOffspringUpperBound(0.3);
        Bounds bounds = individual.getTotalOffspringBounds();
        Assert.assertEquals(0.1, bounds.getLowerBound());
        Assert.assertEquals(0.3, bounds.getUpperBound());
    }
    
}
