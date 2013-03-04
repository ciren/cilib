/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

public class TwoPointCrossoverStrategy implements DiscreteCrossoverStrategy {

    private ProbabilityDistributionFunction random;
    private List<Integer> crossoverPoints;

    public TwoPointCrossoverStrategy() {
        this.random = new UniformDistribution();
        this.crossoverPoints = new ArrayList<Integer>();
    }

    public TwoPointCrossoverStrategy(TwoPointCrossoverStrategy copy) {
        this.random = copy.random;
        this.crossoverPoints = new ArrayList<Integer>(copy.crossoverPoints);
    }

    @Override
    public TwoPointCrossoverStrategy getClone() {
        return new TwoPointCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "TwoPointCrossoverStrategy requires 2 parents.");

        // Select the pivot points where crossover will occur
        int maxLength = Math.min(parentCollection.get(0).getDimension(), parentCollection.get(1).getDimension());
        int p1 = Double.valueOf(random.getRandomNumber(0, maxLength + 1)).intValue();
        int p2 = Double.valueOf(random.getRandomNumber(0, maxLength + 1)).intValue();
        crossoverPoints = Arrays.asList(Math.min(p1, p2), Math.max(p1, p2));

        return crossover(parentCollection, crossoverPoints);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection, List<Integer> crossoverPoints) {
        Preconditions.checkArgument(parentCollection.size() == 2, "TwoPointCrossoverStrategy requires 2 parents.");

        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        int p1 = crossoverPoints.get(0);
        int p2 = crossoverPoints.get(1);

        Vector offspringVector1 = (Vector) offspring1.getCandidateSolution();
        Vector offspringVector2 = (Vector) offspring2.getCandidateSolution();

        Vector.Builder offspringVector1Builder = Vector.newBuilder();
        Vector.Builder offspringVector2Builder = Vector.newBuilder();

        offspringVector1Builder.copyOf(offspringVector1.copyOfRange(0, p1));
        offspringVector2Builder.copyOf(offspringVector2.copyOfRange(0, p1));

        offspringVector1Builder.copyOf(offspringVector2.copyOfRange(p1, p2));
        offspringVector2Builder.copyOf(offspringVector1.copyOfRange(p1, p2));

        offspringVector1Builder.copyOf(offspringVector1.copyOfRange(p2, offspringVector2.size()));
        offspringVector2Builder.copyOf(offspringVector2.copyOfRange(p2, offspringVector1.size()));

        offspring1.setCandidateSolution(offspringVector1Builder.build());
        offspring2.setCandidateSolution(offspringVector2Builder.build());

        return Arrays.asList(offspring1, offspring2);
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

    @Override
    public List<Integer> getCrossoverPoints() {
        return crossoverPoints;
    }

    public void setCrossoverPointProbability(double crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable.");
    }

    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }
}
