/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 */
public class ParticlePositions implements Measurement<StringType> {
    private static final long serialVersionUID = -7891715753767819344L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticlePositions getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringType getValue(Algorithm algorithm) {
        StringBuilder tmp = new StringBuilder();

        PSO pso = (PSO) algorithm;
        //Iterator i = pso.getTopology().particles();
        Iterator<Particle> i = pso.getTopology().iterator();
        while (i.hasNext()) {
            Particle particle = i.next();
            tmp.append("\nParticle: ");
            tmp.append(" Current Fitness: ");
            tmp.append(particle.getFitness().getValue());
            tmp.append(" Best Fitness: ");
            tmp.append(particle.getBestFitness().getValue());
            tmp.append(" Position: ");

            Vector v = (Vector) particle.getPosition();
            for (int j = 0; j < particle.getDimension(); ++j) {
                tmp.append(v.doubleValueOf(j));
                tmp.append(" ");
            }
        }

        return new StringType(tmp.toString());
    }

}
