/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.guideprovider;

import java.util.Iterator;

import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.entity.Topologies;
import net.cilib.entity.comparator.SocialBestFitnessComparator;
import net.cilib.math.random.UniformDistribution;
import net.cilib.pso.PSO;
import net.cilib.pso.particle.Particle;
import net.cilib.type.types.Bounds;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.StructuredType;
import net.cilib.type.types.container.Vector;
import fj.P;
import fj.P1;

public class ArithmeticGuideProvider implements GuideProvider {

    private boolean perDimension;

    public ArithmeticGuideProvider() {
        perDimension = false;
    }

    public GuideProvider getClone() {
        return this;
    }

    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        fj.data.List<Particle> topology = pso.getTopology();
        Particle gbestParticle = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());
        Vector gbest = (Vector) gbestParticle.getBestPosition();
        Vector pbest = (Vector) particle.getBestPosition();
        P1 p1, p2;

        if (!perDimension) {
            UniformDistribution uniform = new UniformDistribution();
            double r = uniform.getRandomNumber();

            p1 = P.p(r);
            p2 = P.p(1.0 - r);
        } else {
            Vector r = Vector.newBuilder().copyOf(
                Vector.fill(Real.valueOf(0.0, new Bounds(0.0, 1.0)), particle.getDimension())
            ).buildRandom();

            Vector oneMinusR = Vector.fill(1.0, particle.getDimension()).subtract(r);

            p1 = new VectorP(r);
            p2 = new VectorP(oneMinusR);
        }

        return pbest.multiply(p1).plus(gbest.multiply(p2));
    }

    private static class VectorP extends P1<Number> {

        private Iterator<Numeric> i;

        public VectorP(Vector v) {
            i = v.iterator();
        }

        @Override
        public Number _1() {
            return i.next().doubleValue();
        }

    }

    public boolean getPerDimension() {
        return perDimension;
    }

    public void setPerDimension(boolean perDimension) {
        this.perDimension = perDimension;
    }
}
