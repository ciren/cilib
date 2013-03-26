/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.Operator;
import net.sourceforge.cilib.math.ArithmeticOperator;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 *
 */
public abstract class MutationStrategy implements Operator {

    private static final long serialVersionUID = 6670947597280440404L;
    private ControlParameter mutationProbability;
    private ProbabilityDistributionFunction randomDistribution;
    private String operator;
    private ArithmeticOperator operatorStrategy;

    public MutationStrategy() {
        this.setOperator("+");

        mutationProbability = ConstantControlParameter.of(0.3);
        randomDistribution = new UniformDistribution();
    }

    public MutationStrategy(MutationStrategy copy) {
        this.operator = copy.operator;
        this.operatorStrategy = copy.operatorStrategy;
        this.mutationProbability = copy.mutationProbability.getClone();
        this.randomDistribution = copy.randomDistribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract MutationStrategy getClone();

    /**
     * Perform the mutation operation on the provided list of offspring individuals.
     * @param offspringList The list of {@linkplain Entity} instances to perform a
     *                      mutation on.
     */
    public abstract void mutate(List<? extends Entity> offspringList);

    /**
     * @return the mutation probability.
     */
    public ControlParameter getMutationProbability() {
        return mutationProbability;
    }

    /**
     *
     * @param mutationProbability
     */
    public void setMutationProbability(ControlParameter mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public ProbabilityDistributionFunction getRandomDistribution() {
        return randomDistribution;
    }

    public void setRandomDistribution(ProbabilityDistributionFunction randomNumber) {
        this.randomDistribution = randomNumber;
    }

    public String getOperator() {
        return operator;
    }

    /**
     * This sets the operator to be used within the mutation strategy.
     * The mutation can be multiplicative or additive.
     * Valid values for the operator are defined in the list below.<br>
     * <p>
     * <table border="1">
     * <tr><td>Multiplicative:</td><td>Additive:</td></tr>
     * <tr><td>*</td><td>+</td></tr>
     * <tr><td>times</td><td>plus, add</td></tr>
     * <tr><td>multiplicative</td><td>additive</td></tr>
     * </table>
     *
     * @param operator A {@link java.lang.String} defining the desired operation
     */
    public void setOperator(String operator) {
        this.operator = operator;
        this.operatorStrategy = MutationOperatorFactory.getOperatorStrategy(operator);
    }

    public ArithmeticOperator getOperatorStrategy() {
        return this.operatorStrategy;
    }
}
