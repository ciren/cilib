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
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public class FIPSVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 6391914534943249737L;

    private ControlParameter inertiaWeight;
    private ControlParameter phi;

    public FIPSVelocityProvider() {
        this.inertiaWeight = ConstantControlParameter.of(0.729844);
        this.phi = ConstantControlParameter.of(1.496180);
    }

    public FIPSVelocityProvider(FIPSVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.phi = copy.phi.getClone();
    }

    @Override
    public FIPSVelocityProvider getClone() {
        return new FIPSVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        PSO algorithm = (PSO) AbstractAlgorithm.get();
        fj.data.List<Particle> topology = algorithm.getTopology();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double informationSum = 0.0;
            int numberOfNeighbours = 0;

            for (Particle currentTarget : algorithm.getNeighbourhood().f(topology, particle)) {
                Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                double randomComponent = (this.phi.getParameter()) * Rand.nextDouble();

                informationSum += randomComponent * (currentTargetPosition.doubleValueOf(i) - position.doubleValueOf(i));

                numberOfNeighbours++;
            }

            double value = this.inertiaWeight.getParameter() * (velocity.doubleValueOf(i) + (informationSum / numberOfNeighbours));

            builder.add(value);
        }

        return builder.build();
    }

    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    public void setPhi(ControlParameter phi) {
        this.phi = phi;
    }
}
