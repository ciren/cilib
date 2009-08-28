/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.measurement.single;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Function to calculate the normalised diversity of a swarm according to the definition of Riget et al
 * </p><p>
 * References:
 * </p><p><ul><li>
 * J Riget, J.S. Vesterstrom, "A Diversity-Guided Particle Swarm Optimizer -- The ARPSO",
 * Technical Report, Department of Computer Science, University of Aarhus, 2002.
 * </li><li>
 * AP Engelbrecht, "Fundamentals of Computational Swarm Intelligence",
 * Wiley & Sons, pages 125, 2005.
 * </li></ul></p>
 * @author Andries Engelbrecht
 */

/**
 * TODO: Rather let this extend Diversity, call super.getValue, but how
 * to type-cast this back to a double?
 */

public class NormalisedDiversity implements Measurement<Real> {
    private static final long serialVersionUID = 93751729329230145L;

    /**
     * {@inheritDoc}
     */
    @Override
    public NormalisedDiversity getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "R";
    }

    /**
     * {@inheritDoc}
     */
    public Real getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;

        int numberParticles = pso.getTopology().size();

        Iterator<Particle> k = pso.getTopology().iterator();
        Particle particle = k.next();
        Vector averageParticlePosition = (Vector) particle.getPosition().getClone();
        while (k.hasNext()) {
            particle = k.next();
            Vector v = (Vector) particle.getPosition();
            for (int j = 0; j < averageParticlePosition.getDimension(); ++j)
               averageParticlePosition.setReal(j, averageParticlePosition.getReal(j)+v.getReal(j));
        }
        for (int j = 0; j < averageParticlePosition.getDimension(); ++j)
           averageParticlePosition.setReal(j, averageParticlePosition.getReal(j)/numberParticles);

        Iterator<Particle> i = pso.getTopology().iterator();
        double particleSum = 0.0;
        while (i.hasNext()) {
            particle = i.next();

            double dimensionSum = 0.0;
            Vector v = (Vector) particle.getPosition();
            for (int j = 0; j < particle.getDimension(); ++j) {
                dimensionSum += (v.getReal(j)-averageParticlePosition.getReal(j))*(v.getReal(j)-averageParticlePosition.getReal(j));

            }
            particleSum += Math.sqrt(dimensionSum);
        }

        double diversity = particleSum/numberParticles;

        DiameterVisitor diameterVisitor = new DiameterVisitor();
        pso.accept(diameterVisitor);
        double diameter = diameterVisitor.getResult();

        return new Real(diversity/diameter);
    }

}
