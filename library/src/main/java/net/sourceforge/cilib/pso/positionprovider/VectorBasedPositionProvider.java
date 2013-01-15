/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import fj.P1;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public class VectorBasedPositionProvider implements PositionProvider {

    private ControlParameter granularity;
    private PositionProvider delegate;

    public VectorBasedPositionProvider() {
        this.granularity = ConstantControlParameter.of(0.5);
        this.delegate = new StandardPositionProvider();
    }

    public VectorBasedPositionProvider(VectorBasedPositionProvider copy) {
        this.granularity = copy.granularity.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public PositionProvider getClone() {
        return new VectorBasedPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector newPos = delegate.get(particle);

        Particle tmp = particle.getClone();
        tmp.setCandidateSolution(newPos);
        Fitness newFitness = particle.getFitnessCalculator().getFitness(tmp);

        final UniformDistribution uniform = new UniformDistribution();
        Vector newPBest = newPos.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return uniform.getRandomNumber(-granularity.getParameter(), granularity.getParameter());
            }
        }).plus(newPos);
        tmp.setCandidateSolution(newPos);
        Fitness newPBestFitness = particle.getFitnessCalculator().getFitness(tmp);

        if (newPBestFitness.compareTo(newFitness) < 0) {
            Vector tmpVector = Vector.copyOf(newPos);
            newPos = newPBest;
            newPBest = tmpVector;
        }

        double dot = ((Vector) particle.getNeighbourhoodBest().getBestPosition())
                .subtract(newPos).dot(newPBest.subtract(newPos));

        if (dot < 0) {
            return (Vector) particle.getPosition();
        }

        return newPos;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public PositionProvider getDelegate() {
        return delegate;
    }

    public void setGranularity(ControlParameter granularity) {
        this.granularity = granularity;
    }

    public ControlParameter getGranularity() {
        return granularity;
    }
}
