/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.real;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

public class MultiParentCrossoverStrategy implements CrossoverStrategy {
    
    private ProbabilityDistributionFunction random;
    
    public MultiParentCrossoverStrategy() {
        this.random = new UniformDistribution();
    }
    
    public MultiParentCrossoverStrategy(MultiParentCrossoverStrategy copy) {
        this.random = copy.random;
    }

    @Override
    public CrossoverStrategy getClone() {
        return new MultiParentCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 4, "MultiParentCrossoverStrategy requires 4 parents.");
        
        double a1 = random.getRandomNumber();
        double a2 = random.getRandomNumber();
        double a3 = random.getRandomNumber();
        double a4 = random.getRandomNumber();
        double sum = a1 + a2 + a3 + a4;
        
        a1 = 5 * (a1/sum) - 1;
        a2 = 5 * (a2/sum) - 1;
        a3 = 5 * (a3/sum) - 1;
        a4 = 5 * (a4/sum) - 1;
        
        Vector v1 = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector v2 = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector v3 = (Vector) parentCollection.get(2).getCandidateSolution();
        Vector v4 = (Vector) parentCollection.get(3).getCandidateSolution();
        
        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setCandidateSolution(v1.multiply(a1).plus(v2.multiply(a2)).plus(v3.multiply(a3)).plus(v4.multiply(a4)));

        return Arrays.asList(offspring);
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    @Override
    public int getNumberOfParents() {
        return 4;
    }
    
    public void setCrossoverPointProbability(double crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable");
    }
    
    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }
}
