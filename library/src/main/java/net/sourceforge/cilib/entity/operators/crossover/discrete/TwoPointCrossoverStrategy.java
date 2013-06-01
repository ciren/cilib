/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.discrete;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.DiscreteCrossoverStrategy;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

public class TwoPointCrossoverStrategy implements DiscreteCrossoverStrategy {

    private List<Integer> crossoverPoints;

    public TwoPointCrossoverStrategy() {
        this.crossoverPoints = new ArrayList<>();
    }

    public TwoPointCrossoverStrategy(TwoPointCrossoverStrategy copy) {
        this.crossoverPoints = new ArrayList<>(copy.crossoverPoints);
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
        int p1 = Rand.nextInt(maxLength);
        int p2 = Rand.nextInt(maxLength);
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

        Vector offspringVector1 = (Vector) offspring1.getPosition();
        Vector offspringVector2 = (Vector) offspring2.getPosition();

        Vector.Builder offspringVector1Builder = Vector.newBuilder();
        Vector.Builder offspringVector2Builder = Vector.newBuilder();

        offspringVector1Builder.copyOf(offspringVector1.copyOfRange(0, p1));
        offspringVector2Builder.copyOf(offspringVector2.copyOfRange(0, p1));

        offspringVector1Builder.copyOf(offspringVector2.copyOfRange(p1, p2));
        offspringVector2Builder.copyOf(offspringVector1.copyOfRange(p1, p2));

        offspringVector1Builder.copyOf(offspringVector1.copyOfRange(p2, offspringVector2.size()));
        offspringVector2Builder.copyOf(offspringVector2.copyOfRange(p2, offspringVector1.size()));

        offspring1.setPosition(offspringVector1Builder.build());
        offspring2.setPosition(offspringVector2Builder.build());

        return Arrays.asList(offspring1, offspring2);
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    @Override
    public List<Integer> getCrossoverPoints() {
        return crossoverPoints;
    }

    @Override
    public void setCrossoverPointProbability(ControlParameter crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable.");
    }

    @Override
    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }
    
}
