/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import fj.F2;
import fj.Ord;
import fj.Ordering;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ExponentiallyVaryingControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Vectors;

public class FERGuideProvider implements GuideProvider {

    @Override
    public FERGuideProvider getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StructuredType get(Particle particle) {
        Vector position = (Vector) particle.getBestPosition();
        double ssLength = Vectors.upperBoundVector(position).subtract(Vectors.lowerBoundVector(position)).length();

        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology()
            .sort(Ord.<Particle>ord(new F2<Particle, Particle, Ordering>() {
                @Override
                public Ordering f(Particle p1, Particle p2) {
                    return Ordering.values()[-p1.getBestFitness().compareTo(p2.getBestFitness()) + 1];
                }
            }.curry()));

        Fitness bestFitness = topology.head().getBestFitness();
        Fitness worstFitness = topology.last().getBestFitness();
        double alpha = ssLength / Math.abs(bestFitness.getValue() - worstFitness.getValue()) * 
                       bestFitness.compareTo(worstFitness);

        Particle ferMaximizer = topology.head();
        double maxFER = Double.NEGATIVE_INFINITY;

        for (Particle p : topology) {
            if (p != particle) {
                Fitness f = p.getBestFitness();
                Vector pos = (Vector) p.getBestPosition();

                double testFER = alpha * Math.abs(f.getValue() - particle.getBestFitness().getValue()) * 
                                 f.compareTo(particle.getBestFitness()) / 
                                 pos.subtract(position).length();

                if (testFER > maxFER) {
                    maxFER = testFER;
                    ferMaximizer = p;
                }
            }
        }

        return ferMaximizer.getBestPosition();
    }

}
