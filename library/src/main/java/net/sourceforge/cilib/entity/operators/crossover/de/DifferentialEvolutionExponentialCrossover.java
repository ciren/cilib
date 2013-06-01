/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.de;

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

public class DifferentialEvolutionExponentialCrossover implements DiscreteCrossoverStrategy {

    private static final long serialVersionUID = -4811879014933329926L;

    private ControlParameter crossoverPointProbability;
    private List<Integer> crossoverPoints;

    public DifferentialEvolutionExponentialCrossover() {
        this.crossoverPoints = new ArrayList<>();
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
    }

    public DifferentialEvolutionExponentialCrossover(DifferentialEvolutionExponentialCrossover copy) {
        this.crossoverPoints = new ArrayList<>(copy.crossoverPoints);
        this.crossoverPointProbability = copy.crossoverPointProbability.getClone();
    }

    @Override
    public DifferentialEvolutionExponentialCrossover getClone() {
        return new DifferentialEvolutionExponentialCrossover(this);
    }

    /**
     * Perform the cross-over based on the exponential method for recombination. The given
     * <code>parentCollection</code> should only contain two {@linkplain Entity} objects,
     * as the crossover operator is only defined for two {@linkplain Entity}s.
     *
     * <p>
     * It is VERY important that the order in which the parents are presented is consistent.
     * The first {@linkplain Entity} within the collection MUST be the <code>trialVector</code>
     * {@linkplain Entity}, followed by the target parent {@linkplain Entity}.
     *
     * @throws UnsupportedOperationException if the number of parents does not equal the size value of 2.
     * @return A list consisting of the offspring. This operator only returns a single offspring {@linkplain Entity}.
     */
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionExponentialCrossover requires 2 parents.");

        Vector parentVector = (Vector) parentCollection.get(0).getPosition();
        Vector trialVector = (Vector) parentCollection.get(1).getPosition();

        int dimension = Math.min(trialVector.size(), parentVector.size());
        int j = Rand.nextInt(dimension);

        crossoverPoints.clear();
        do {
            crossoverPoints.add(j);
            j = (j + 1) % dimension;
        } while (Rand.nextDouble() < crossoverPointProbability.getParameter() && (crossoverPoints.size() < dimension));


        return crossover(parentCollection, crossoverPoints);
    }
    
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection, List<Integer> crossoverPoints) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionExponentialCrossover requires 2 parents.");

        Vector parentVector = (Vector) parentCollection.get(0).getPosition();
        Vector trialVector = (Vector) parentCollection.get(1).getPosition();

        Vector.Builder offspringVector = Vector.newBuilder();
        int dimension = Math.min(trialVector.size(), parentVector.size());

        for (int i = 0; i < dimension; i++) {
            if (crossoverPoints.contains(i)) {
                offspringVector.add(trialVector.get(i));
            } else {
                offspringVector.add(parentVector.get(i));
            }
        }

        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setPosition(offspringVector.build());

        return Arrays.asList(offspring);
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
