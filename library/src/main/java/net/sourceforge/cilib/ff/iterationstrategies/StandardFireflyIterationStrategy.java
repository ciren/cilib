/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.iterationstrategies;


import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.ff.FFA;
import net.sourceforge.cilib.ff.firefly.Firefly;

/**
 * Implementation of the standard iteration strategy for the Firefly algorithm.
 */
public class StandardFireflyIterationStrategy extends AbstractIterationStrategy<FFA> {

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardFireflyIterationStrategy getClone() {
        return this;
    }

    /**
     * <p>This is the standard Firefly iteration strategy:</p>
     * <ol>
     * <li>For each Firefly<sub>i</sub></li>
     * <ol>
     * <li>For each other Firefly<sub>j</sub><li>
     * <ol>
     * <li>If Intensity<sub>j</sub> &gt; Intensity<sub>i</sub></li>
     * <ol>
     * <li>Move Firefly<sub>i</sub> toward Firefly<sub>j</sub>
     * according to Attractiveness<sub>j</sub></li>
     * </ol>
     * <li>Evaluate fitness and intensity of Firefly<sub>i</sub></li>
     * </ol>
     * </ol>
     * </ol>
     * @param algorithm The algorithm to which an iteration is to be applied.
     */
    @Override
    public void performIteration(FFA algorithm) {
        Topology<Firefly> topology = algorithm.getTopology();

        for (Firefly current : topology) {
            for (Firefly other : topology) {
                if (other.isBrighter(current)) {
                    current.updatePosition(other);
                    boundaryConstraint.enforce(current);
                    current.calculateFitness();
                }
            }
        }
    }
}
