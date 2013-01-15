/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.de;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Binomial crossover operator.
 */
public class DifferentialEvolutionBinomialCrossover implements CrossoverStrategy {

    private static final long serialVersionUID = -2939023704055943968L;
    private ProbabilityDistributionFunction random;
    private SettableControlParameter crossoverPointProbability;

    public DifferentialEvolutionBinomialCrossover() {
        this.random = new UniformDistribution();
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
    }

    public DifferentialEvolutionBinomialCrossover(DifferentialEvolutionBinomialCrossover copy) {
        this.random = copy.random;
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

        Vector parentVector = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector trialVector = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector.Builder offspringVector = Vector.newBuilder();

        //this is the index of the dimension that will always be included
        int i = Double.valueOf(random.getRandomNumber(0, parentVector.size())).intValue();

        for (int j = 0; j < parentVector.size(); j++) {
            if (random.getRandomNumber() < crossoverPointProbability.getParameter() || j == i) {
                offspringVector.add(trialVector.get(j));
            } else {
                offspringVector.add(parentVector.get(j));
            }
        }

        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setCandidateSolution(offspringVector.build());

        return Arrays.asList(offspring);
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setCrossoverPointProbability(SettableControlParameter crossoverPointProbability) {
        this.crossoverPointProbability = crossoverPointProbability;
    }

    public SettableControlParameter getCrossoverPointProbability() {
        return crossoverPointProbability;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    public void setCrossoverPointProbability(double crossoverPointProbability) {
        this.crossoverPointProbability.setParameter(crossoverPointProbability);
    }

    public void setCrossoverProbabilityParameter(SettableControlParameter crossoverPointProbability) {
        this.crossoverPointProbability = crossoverPointProbability;
    }

}
