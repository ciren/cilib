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

/**
 * Binomial crossover operator.
 */
public class DifferentialEvolutionBinomialCrossover implements DiscreteCrossoverStrategy {

    private static final long serialVersionUID = -2939023704055943968L;
    
    private ControlParameter crossoverPointProbability;
    private List<Integer> crossoverPoints;

    public DifferentialEvolutionBinomialCrossover() {
        this.crossoverPoints = new ArrayList<>();
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
    }

    public DifferentialEvolutionBinomialCrossover(DifferentialEvolutionBinomialCrossover copy) {
        this.crossoverPoints = new ArrayList<>(copy.crossoverPoints);
        this.crossoverPointProbability = copy.crossoverPointProbability.getClone();
    }

    @Override
    public DifferentialEvolutionBinomialCrossover getClone() {
        return new DifferentialEvolutionBinomialCrossover(this);
    }

    /**
     * <p>
     * Perform the cross-over based on the binomial method for recombination. The given
     * <code>parentCollection</code> should only contain two {@linkplain Entity} objects,
     * as the crossover operator is only defined for two {@linkplain Entity}s.
     * </p>
     * <p>
     * It is VERY important that the order in which the parents are presented is consistent.
     * The first {@linkplain Entity} within the collection MUST be the <code>trialVector</code>
     * {@linkplain Entity}, followed by the target parent {@linkplain Entity}.
     * </p>
     * <p>This method implements the following logic:</p>
     * <pre>
     * for j = 1, ..., x_n:
     *   if ( (U(0,1) < P_c) || (j == i) )
     *     x'_{i,j}(t) = trialVector_{i,j}(t)
     *   else
     *     x'_{i,j}(t) = x_{i,j}(t)
     * </pre>
     *
     * @param parentCollection the collection of parent {@linkplain Entity} objects.
     * @throws UnsupportedOperationException if the number of parents does not equal the size value of 2.
     * @return A list consisting of the offspring. This operator only returns a single offspring {@linkplain Entity}.
     */
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionBinomialCrossover requires 2 parents.");

        Vector parentVector = (Vector) parentCollection.get(0).getPosition();
        Vector trialVector = (Vector) parentCollection.get(1).getPosition();

        //this is the index of the dimension that will always be included
        int i = Rand.nextInt(Math.min(parentVector.size(), trialVector.size()));

        crossoverPoints.clear();
        for (int j = 0; j < parentVector.size(); j++) {
            if (Rand.nextDouble() < crossoverPointProbability.getParameter() || j == i) {
                crossoverPoints.add(j);
            }
        }

        return crossover(parentCollection, crossoverPoints);
    }
    
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection, List<Integer> crossoverPoints) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionBinomialCrossover requires 2 parents.");

        Vector parentVector = (Vector) parentCollection.get(0).getPosition();
        Vector trialVector = (Vector) parentCollection.get(1).getPosition();
        Vector.Builder offspringVector = Vector.newBuilder();

        for (int j = 0; j < parentVector.size(); j++) {
            if (crossoverPoints.contains(j)) {
                offspringVector.add(trialVector.get(j));
            } else {
                offspringVector.add(parentVector.get(j));
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
