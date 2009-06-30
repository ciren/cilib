/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.Operator;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 *
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public abstract class MutationStrategy implements Operator {
    private static final long serialVersionUID = 6670947597280440404L;

    private ControlParameter mutationProbability;
    private RandomNumber randomNumber;
    private String operator;
    private MutationOperatorStrategy operatorStrategy;

    public MutationStrategy() {
        this.setOperator("+");

        mutationProbability = new ConstantControlParameter(0.3);
        randomNumber = new RandomNumber();
    }

    public MutationStrategy(MutationStrategy copy) {
        this.operator = copy.operator;
        this.operatorStrategy = copy.operatorStrategy;
        this.mutationProbability = copy.mutationProbability.getClone();
        this.randomNumber = copy.randomNumber.getClone();
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
     *
     * @return
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

    public RandomNumber getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(RandomNumber randomNumber) {
        this.randomNumber = randomNumber;
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

    /**
     * Get the defined {@link net.sourceforge.cilib.offspringList.operators.mutation.MutationOperatorStrategy}.
     * @return
     */
    public MutationOperatorStrategy getOperatorStrategy() {
        return this.operatorStrategy;
    }

    public void setOperatorStrategy(MutationOperatorStrategy operatorStrategy) {
        this.operatorStrategy = operatorStrategy;
    }

}
