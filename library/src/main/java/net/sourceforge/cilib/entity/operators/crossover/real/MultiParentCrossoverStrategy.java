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
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

public class MultiParentCrossoverStrategy implements CrossoverStrategy {
    
    public MultiParentCrossoverStrategy() {}
    
    @Override
    public MultiParentCrossoverStrategy getClone() {
        return this;
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 4, "MultiParentCrossoverStrategy requires 4 parents.");
        
        double a1 = Rand.nextDouble();
        double a2 = Rand.nextDouble();
        double a3 = Rand.nextDouble();
        double a4 = Rand.nextDouble();
        double sum = a1 + a2 + a3 + a4;
        
        a1 = 5 * (a1/sum) - 1;
        a2 = 5 * (a2/sum) - 1;
        a3 = 5 * (a3/sum) - 1;
        a4 = 5 * (a4/sum) - 1;
        
        Vector v1 = (Vector) parentCollection.get(0).getPosition();
        Vector v2 = (Vector) parentCollection.get(1).getPosition();
        Vector v3 = (Vector) parentCollection.get(2).getPosition();
        Vector v4 = (Vector) parentCollection.get(3).getPosition();
        
        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setPosition(v1.multiply(a1).plus(v2.multiply(a2)).plus(v3.multiply(a3)).plus(v4.multiply(a4)));

        return Arrays.asList(offspring);
    }

    @Override
    public int getNumberOfParents() {
        return 4;
    }

}
