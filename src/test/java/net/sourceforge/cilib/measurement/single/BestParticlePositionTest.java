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

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.util.Vectors;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class BestParticlePositionTest {

    @Test
    public void testBestParticlePositionDomain() {
        Particle p = new StandardParticle();
        p.getProperties().put(EntityType.Particle.BEST_POSITION, Vectors.create(4.0));
        p.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());

        PSO pso = new PSO();
        pso.getTopology().add(p);

        Measurement measurement = new BestParticlePosition();
        measurement.getValue(pso);

        Assert.assertEquals(p.getBestPosition().toString(), measurement.getValue(pso).toString());
    }

}
