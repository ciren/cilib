/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public class FIPSVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 6391914534943249737L;

    private ControlParameter inertiaWeight;
    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;

    public FIPSVelocityProvider() {
        this.inertiaWeight = ConstantControlParameter.of(0.729844);
        this.socialAcceleration = ConstantControlParameter.of(1.496180);
        this.cognitiveAcceleration = ConstantControlParameter.of(1.496180);
    }

    public FIPSVelocityProvider(FIPSVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
    }

    @Override
    public FIPSVelocityProvider getClone() {
        return new FIPSVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double informationSum = 0.0;
            int numberOfNeighbours = 0;

            for (Particle currentTarget : topology.neighbourhood(particle)) {
                Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                double randomComponent = (this.cognitiveAcceleration.getParameter() + this.socialAcceleration.getParameter()) * Rand.nextDouble();

                informationSum += randomComponent * (currentTargetPosition.doubleValueOf(i) - position.doubleValueOf(i));

                numberOfNeighbours++;
            }

            double value = this.inertiaWeight.getParameter() * (velocity.doubleValueOf(i) + (informationSum / numberOfNeighbours));

            builder.add(value);
        }

        return builder.build();
    }
}
