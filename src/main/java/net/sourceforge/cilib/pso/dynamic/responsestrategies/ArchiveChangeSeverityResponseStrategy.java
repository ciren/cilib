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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * When a change occurs in the environment, a specified number of sentries are selected from the archive.
 * If these sentries' solutions varies more than a specified threshold, the solutions from the archive
 * within a specified radius is removed from the archive. All solutions within the archive is also
 * re-evaluated.
 *
 * @author Marde Greeff
 *
 */
public class ArchiveChangeSeverityResponseStrategy<E extends PopulationBasedAlgorithm>  extends
        EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm>  {

    private static final long serialVersionUID = 3044874503105791208L;

    protected ControlParameter numberOfSentries;
    protected RandomProvider randomGenerator = null;
    protected double radiusFactor;

    /**
     * Creates a new instance of ArchiveChangeSeverityResponseStrategy.
     */
    public ArchiveChangeSeverityResponseStrategy() {
    	this.numberOfSentries = new ConstantControlParameter(10.0);
    	this.radiusFactor = 1.5;
    	this.randomGenerator = new MersenneTwister();
    }

    /**
     * Creates a copy of provided instance.
     * @param asrs Instance to copy
     */
    public ArchiveChangeSeverityResponseStrategy(ArchiveChangeSeverityResponseStrategy asrs){
    	super(asrs);
    	this.numberOfSentries = asrs.numberOfSentries.getClone();
    	this.randomGenerator = asrs.randomGenerator;
    	this.radiusFactor = asrs.radiusFactor;
    }

    /**
     * @{@inheritDoc}
     */
	@Override
    public EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> getClone() {
        return this;
    }

    /**
     * When a change occurs in the environment, a specified number of sentries
     * are selected from the archive. If these sentries' solutions varies less
     * than a specified threshold, the solutions from the archive within a
     * specified radius are removed from the archive. All solutions within the
     * archive are also re-evaluated.
     * @param algorithm The algorithm to perform the response on.
     */
    @Override
    protected void performReaction(PopulationBasedAlgorithm algorithm) {
    	//check whether archive size is bigger than the number of sentries
    	if (Archive.Provider.get().size() <= this.numberOfSentries.getParameter())
    		this.numberOfSentries.setParameter(Archive.Provider.get().size()/2);

    	//old archive values
    	List<OptimisationSolution> oldList = new LinkedList<OptimisationSolution>();
    	for (OptimisationSolution solution : Archive.Provider.get())
    		oldList.add(solution);

    	//re-evaluating entities' fitness
        for (Entity entity : algorithm.getTopology()) {
        	entity.getProperties().put(EntityType.Particle.BEST_FITNESS, entity.getFitnessCalculator().getFitness(entity));
            //entity.getProperties().put(EntityType.Particle.BEST_POSITION, entity.getCandidateSolution());
            entity.calculateFitness();
        }

        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        OptimisationProblem problem = populationBasedAlgorithm.getOptimisationProblem();

        //re-evaluating archive solutions
        List<OptimisationSolution> newList = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution solution : Archive.Provider.get()) {
            OptimisationSolution os = new OptimisationSolution(solution.getPosition(), problem.getFitness(solution.getPosition()));
            newList.add(os);
        }

        //average change in the objective space that the sentries experienced
        double avgChanges = 0.0;
        // select random sentry entities
        List<Double> changes = new LinkedList<Double>();
        List<Double> sentries = new LinkedList<Double>();

        for (int sentryCount=0; sentryCount < this.numberOfSentries.getParameter(); sentryCount++) {
        	int random = randomGenerator.nextInt(Archive.Provider.get().size());
        	sentries.add((double)random);
        	OptimisationSolution solution1 = oldList.get(random);
        	OptimisationSolution solution2 = newList.get(random);
        	MOFitness fitness1 = (MOFitness)(solution1.getFitness());
            MOFitness fitness2 = (MOFitness)(solution2.getFitness());

            double change = 0.0;
            for (int i=0; i<fitness1.getDimension(); i++) {
            	change += Math.pow(fitness1.getFitness(i).getValue()-fitness2.getFitness(i).getValue(), 2);
            }
            changes.add(Math.sqrt(change));
            avgChanges += Math.sqrt(change);
        }
        avgChanges = avgChanges / (double)numberOfSentries.getParameter();
        //System.out.println("avgChanges = " + avgChanges);

        List<OptimisationSolution> clearedSolutions = new LinkedList<OptimisationSolution>();
        //checking sentries' change and respond
        for (int k=0; k < sentries.size(); k++) {
        	double change = changes.get(k);
        	if (change < this.radiusFactor*avgChanges*0.5) {
        		//System.out.println("iteration = " + AbstractAlgorithm.get().getIterations());

        		//System.out.println("for sentry " + k + " change severity is greater - change is " + change);
        		clearedSolutions.add(newList.get(k));
        		//find closest sentry to calculate the radius
        		double minSentryDistance = 0.0;
        		for (int kk=0; kk < sentries.size(); kk++)
        		{
        			if (kk != k) {
        			OptimisationSolution sol = oldList.get(sentries.get(kk).intValue());
        			MOFitness sentry1 = (MOFitness)(sol.getFitness());
        			OptimisationSolution sol2 = oldList.get(sentries.get(kk).intValue());
        			MOFitness sentry2 = (MOFitness)(sol2.getFitness());

        			double distance = 0.0;
        			for (int j = 0; j < sentry1.getDimension(); j++) {
        				distance += Math.pow(sentry1.getFitness(j).getValue()-sentry2.getFitness(j).getValue(), 2);
        			}
        			distance = Math.sqrt(distance);
        			//**********

        			if (kk == 0)
        				minSentryDistance = distance;
        			else if (distance < minSentryDistance)
        				minSentryDistance = distance;
        			} //end of if kk
        		} //end for kk
        		//System.out.println("minSentryDistance = " + minSentryDistance);

        		//remove solutions from Archive that is within the radius
        		for (int i=0; i < oldList.size(); i++) {
        			if (i != k) {
        				OptimisationSolution sol = oldList.get(i);
            			MOFitness sentry1 = (MOFitness)(sol.getFitness());
            			OptimisationSolution sol2 = oldList.get(sentries.get(k).intValue());
            			MOFitness sentry2 = (MOFitness)(sol2.getFitness());

        				double distance = 0.0;
        				for (int ii=0; ii < sentry1.getDimension(); ii++) {
        					distance += Math.pow(sentry1.getFitness(ii).getValue()-sentry2.getFitness(ii).getValue(), 2);
        				}
        				distance = Math.sqrt(distance);

        				if (distance < (0.5*minSentryDistance)) {
        					clearedSolutions.add(newList.get(i));
        					//System.out.println("removing solution from archive with distance = " + distance);
        					//System.out.println("radius = " + minSentryDistance);
        				}
        			}
        		}
        	} //end if

        } //end for k

        newList.removeAll(clearedSolutions);
        Archive.Provider.get().clear();
        Archive.Provider.get().addAll(newList);
    }

    /**
     * Set the number of sentries that are selected from the archive
     * @param parameter Number of sentries
     */
    public void setNumberOfSentries(ControlParameter parameter) {
        if (parameter.getParameter() <= 0) {
            throw new IllegalArgumentException("It doesn't make sense to have <= 0 sentry points");
        }

        this.numberOfSentries = parameter;
    }

    /**
     * Returns the number of sentries
     * @return numberOfSentries The number of sentries that are used
     */
    public ControlParameter getNumberOfSentries() {
        return this.numberOfSentries;
    }

    /**
     * Set the number of sentries that are selected from the archive
     * @param parameter Number of sentries
     */
    public void setRadiusFactor(double parameter) {
        if (parameter <= 0) {
            throw new IllegalArgumentException("It doesn't make sense to have <= 0 sentry points");
        }

        this.radiusFactor = parameter;
    }

    /**
     * Returns the number of sentries
     * @return numberOfSentries The number of sentries that are used
     */
    public double getRadiusFactor() {
        return this.radiusFactor;
    }

    /**
     * Sets the random number generator that is used to randomly select sentries from the archive
     * @param rng RandomNumberGenerator
     */
    public void setRandomNumberGenerator(RandomProvider rng) {
        this.randomGenerator = rng;
    }

    /**
     * Returns the random number generator that is used to randomly select sentries from the archive
     * @return randomGenerator Random number generator
     */
    public RandomProvider getRandomNumberGenerator() {
        return this.randomGenerator;
    }


}

