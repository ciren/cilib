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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.DiscreteCrossoverStrategy;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

public class UniformCrossoverStrategy implements DiscreteCrossoverStrategy {

    private static final long serialVersionUID = 8912494112973025634L;  
    private ControlParameter crossoverPointProbability;
    private ArrayList<Integer> crossoverPoints;

    public UniformCrossoverStrategy() {
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
        this.crossoverPoints = new ArrayList<>();
    }

    public UniformCrossoverStrategy(UniformCrossoverStrategy copy) {
        this.crossoverPointProbability = copy.crossoverPointProbability.getClone();
        this.crossoverPoints = new ArrayList<>(copy.crossoverPoints);
    }

    @Override
    public UniformCrossoverStrategy getClone() {
        return new UniformCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "UniformCrossoverStrategy requires 2 parents.");
        int minDimension = Math.min(parentCollection.get(0).getDimension(), parentCollection.get(1).getDimension());
        crossoverPoints.clear();

        for (int i = 0; i < minDimension; i++) {
            if (Rand.nextDouble() < crossoverPointProbability.getParameter()) {
                crossoverPoints.add(i);
            }
        }

        return crossover(parentCollection, crossoverPoints);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection, List<Integer> crossoverPoints) {
        Preconditions.checkArgument(parentCollection.size() == 2, "UniformCrossoverStrategy requires 2 parents.");

        //How do we handle variable sizes? Resizing the entities?
        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        int minDimension = Math.min(offspring1.getDimension(), offspring2.getDimension());

        Vector parentChromosome1 = (Vector) offspring1.getPosition();
        Vector parentChromosome2 = (Vector) offspring2.getPosition();
        Vector.Builder offspringChromosome1Builder = Vector.newBuilder();
        Vector.Builder offspringChromosome2Builder = Vector.newBuilder();

        for (int i = 0; i < minDimension; i++) {
            if (crossoverPoints.contains(i)) {
                offspringChromosome1Builder.add(parentChromosome1.get(i));
                offspringChromosome2Builder.add(parentChromosome2.get(i));
            } else {
                offspringChromosome1Builder.add(parentChromosome2.get(i));
                offspringChromosome2Builder.add(parentChromosome1.get(i));
            }
        }

        offspring1.setPosition(offspringChromosome1Builder.build());
        offspring2.setPosition(offspringChromosome2Builder.build());

        return Arrays.asList(offspring1, offspring2);
    }

    @Override
    public void setCrossoverPointProbability(ControlParameter crossoverPointProbability) {
        this.crossoverPointProbability = crossoverPointProbability;
    }

    @Override
    public ControlParameter getCrossoverPointProbability() {
        return crossoverPointProbability;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    @Override
    public List<Integer> getCrossoverPoints() {
        return crossoverPoints;
    }

}
