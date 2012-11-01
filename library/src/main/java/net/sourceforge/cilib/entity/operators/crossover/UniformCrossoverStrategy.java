/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

public class UniformCrossoverStrategy implements CrossoverStrategy {

    private static final long serialVersionUID = 8912494112973025634L;
    
    private ProbabilityDistributionFunction random;
    private ControlParameter crossoverPointProbability;

    public UniformCrossoverStrategy() {
        this.random = new UniformDistribution();
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
    }

    public UniformCrossoverStrategy(UniformCrossoverStrategy copy) {
        this.random = copy.random;
        this.crossoverPointProbability = copy.crossoverPointProbability.getClone();
    }

    @Override
    public UniformCrossoverStrategy getClone() {
        return new UniformCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "UniformCrossoverStrategy requires 2 parents.");
        
        //How do we handle variable sizes? Resizing the entities?
        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        int minDimension = Math.min(offspring1.getDimension(), offspring2.getDimension());

        Vector parentChromosome1 = (Vector) offspring1.getCandidateSolution();
        Vector parentChromosome2 = (Vector) offspring2.getCandidateSolution();
        Vector.Builder offspringChromosome1Builder = Vector.newBuilder();
        Vector.Builder offspringChromosome2Builder = Vector.newBuilder();
        
        for (int i = 0; i < minDimension; i++) {
            if (random.getRandomNumber() < crossoverPointProbability.getParameter()) {
                offspringChromosome1Builder.add(parentChromosome1.get(i));
                offspringChromosome2Builder.add(parentChromosome2.get(i));
            } else {
                offspringChromosome1Builder.add(parentChromosome2.get(i));
                offspringChromosome2Builder.add(parentChromosome1.get(i));
            }
        }

        offspring1.setCandidateSolution(offspringChromosome1Builder.build());
        offspring2.setCandidateSolution(offspringChromosome2Builder.build());

        return Arrays.asList(offspring1, offspring2);
    }

    public void setCrossoverPointProbability(ControlParameter crossoverPointProbability) {
        this.crossoverPointProbability = crossoverPointProbability;
    }

    public ControlParameter getCrossoverPointProbability() {
        return crossoverPointProbability;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }
}
