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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Implementation of the simulated binary cross-over strategy.</p>
 */
public class SimulatedBinaryCrossoverStrategy implements CrossoverStrategy {

    private ControlParameter alpha;
    private ControlParameter eta;
    private ProbabilityDistributionFunction random;

    public SimulatedBinaryCrossoverStrategy() {
        this.alpha = ConstantControlParameter.of(0.5);
        this.eta = ConstantControlParameter.of(0.5); //What should the default value for eta be?
        this.random = new UniformDistribution();
    }

    public SimulatedBinaryCrossoverStrategy(SimulatedBinaryCrossoverStrategy copy) {
        this.alpha = copy.alpha.getClone();
        this.eta = copy.eta.getClone();
        this.random = new UniformDistribution();
    }

    @Override
    public SimulatedBinaryCrossoverStrategy getClone() {
        return new SimulatedBinaryCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "SimulatedBinaryCrossoverStrategy requires 2 parents.");

        // How do we handle variable sizes? Resizing the entities?
        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        Vector parentChromosome1 = (Vector) parentCollection.get(0).getPosition();
        Vector parentChromosome2 = (Vector) parentCollection.get(1).getPosition();
        Vector.Builder offspringChromosome1 = Vector.newBuilder();
        Vector.Builder offspringChromosome2 = Vector.newBuilder();

        int sizeParent1 = parentChromosome1.size();
        int sizeParent2 = parentChromosome2.size();

        int minDimension = Math.min(sizeParent1, sizeParent2);

        for (int i = 0; i < minDimension; i++) {
            double r = this.random.getRandomNumber();
            double gamma = Math.pow(1/(2*(1-r)), 1/(this.eta.getParameter() + 1));
            
            if (r <= this.alpha.getParameter()) {
                gamma = Math.pow(2*r, 1/(this.eta.getParameter() + 1));
            }
            
            double value1 = 0.5*((1 + gamma) * parentChromosome1.doubleValueOf(i) + (1 - gamma) * parentChromosome2.doubleValueOf(i));
            double value2 = 0.5*((1 - gamma) * parentChromosome1.doubleValueOf(i) + (1 + gamma) * parentChromosome2.doubleValueOf(i));

            offspringChromosome1.add(Real.valueOf(value1, parentChromosome1.boundsOf(i)));
            offspringChromosome2.add(Real.valueOf(value2, parentChromosome1.boundsOf(i)));
        }

        offspring1.setPosition(offspringChromosome1.build());
        offspring2.setPosition(offspringChromosome2.build());

        return Arrays.asList(offspring1, offspring2);
    }

    public ControlParameter getAlpha() {
        return alpha;
    }

    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }
    
    public ControlParameter getEta() {
        return eta;
    }

    public void setEta(ControlParameter eta) {
        this.eta = eta;
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
