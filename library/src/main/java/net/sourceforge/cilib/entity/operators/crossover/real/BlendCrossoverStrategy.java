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
 * <p>Implementation of the blend cross-over strategy.</p>
 *
 * <p>BibTeX entry:</p>
 * <pre>
 * ARTICLE{00843494,
 *   title={Gradual distributed real-coded genetic algorithms},
 *   author={{Francisco Herrera} and {Manuel Lozano}},
 *   journal={IEEE Trans. Evolutionary Computation},
 *   year={2000},
 *   month={April},
 *   volume={4},
 *   number={1},
 *   pages={ 43-63},
 * }
 * </pre>
 */
public class BlendCrossoverStrategy implements CrossoverStrategy {

    private static final long serialVersionUID = -7955031193090240495L;
    private ControlParameter alpha;
    private ProbabilityDistributionFunction random;

    public BlendCrossoverStrategy() {
        this.alpha = ConstantControlParameter.of(0.5);
        this.random = new UniformDistribution();
    }

    public BlendCrossoverStrategy(BlendCrossoverStrategy copy) {
        this.alpha = copy.alpha.getClone();
        this.random = new UniformDistribution();
    }

    @Override
    public BlendCrossoverStrategy getClone() {
        return new BlendCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "BlendCrossoverStrategy requires 2 parents.");

        // How do we handle variable sizes? Resizing the entities?
        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        Vector parentChromosome1 = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector parentChromosome2 = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector.Builder offspringChromosome1 = Vector.newBuilder();
        Vector.Builder offspringChromosome2 = Vector.newBuilder();

        int sizeParent1 = parentChromosome1.size();
        int sizeParent2 = parentChromosome2.size();

        int minDimension = Math.min(sizeParent1, sizeParent2);

        for (int i = 0; i < minDimension; i++) {
            double gamma = (1 + 2 * alpha.getParameter()) * random.getRandomNumber() - alpha.getParameter();
            double value1 = (1 - gamma) * parentChromosome1.doubleValueOf(i) + gamma * parentChromosome2.doubleValueOf(i);
            double value2 = (1 - gamma) * parentChromosome2.doubleValueOf(i) + gamma * parentChromosome1.doubleValueOf(i);

            offspringChromosome1.add(Real.valueOf(value1, parentChromosome1.boundsOf(i)));
            offspringChromosome2.add(Real.valueOf(value2, parentChromosome1.boundsOf(i)));
        }

        offspring1.setCandidateSolution(offspringChromosome1.build());
        offspring2.setCandidateSolution(offspringChromosome2.build());

        return Arrays.asList(offspring1, offspring2);
    }

    public ControlParameter getAlpha() {
        return alpha;
    }

    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
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

    public void setCrossoverPointProbability(double crossoverPointProbability) {
        throw new UnsupportedOperationException("Not applicable");
    }

    public ControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Not applicable");
    }
}
