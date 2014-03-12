/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import fj.Ord;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.niching.VectorBasedFunctions;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

public class LIPSVelocityProvider implements VelocityProvider {

    private ControlParameter inertiaWeight;
    private ControlParameter nSize;

    public LIPSVelocityProvider() {
        this.inertiaWeight = ConstantControlParameter.of(0.729844);
        this.nSize = new LinearlyVaryingControlParameter(2, 5);
    }

    public LIPSVelocityProvider(LIPSVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.nSize = copy.nSize.getClone();
    }

    @Override
    public LIPSVelocityProvider getClone() {
        return new LIPSVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        PSO algorithm = (PSO) AbstractAlgorithm.get();
        int ns = (int) nSize.getParameter();
        fj.data.List<Particle> neighbours = algorithm.getTopology()
                .sort(Ord.ord(VectorBasedFunctions.sortByDistance(particle, new EuclideanDistanceMeasure())))
                .take(ns);

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double informationSum = 0.0;
            double randomSum=0;

            for (Particle currentTarget : neighbours) {
                Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();
                double randomComponent = Rand.nextDouble()*(4.1/ns);
                informationSum += randomComponent * currentTargetPosition.doubleValueOf(i);
                randomSum += randomComponent;
            }

            double value = inertiaWeight.getParameter() * (velocity.doubleValueOf(i) + randomSum * ((informationSum / (ns * randomSum) - position.doubleValueOf(i))));
            builder.add(value);
        }

        return builder.build();
    }
    
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    public void setnSize(ControlParameter nSize) {
        this.nSize = nSize;
    }
}
