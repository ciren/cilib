/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.niching.creation;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.pso.niching.NichingTest;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author filipe
 */
public class StandardNicheDetectionTest {
    
    @Test
    public void testDetection() {
        Particle p1 = NichingTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        Particle p2 = NichingTest.createParticle(new MinimisationFitness(3.0), Vector.of(0.0, 1.0));
        
        StandardNicheDetection detection = new StandardNicheDetection();
        Assert.assertFalse(detection.f(p1));
        p1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999999));
        p1 = p1.getClone();
        Assert.assertFalse(detection.f(p1));
        p1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999998));
        p1 = p1.getClone();
        Assert.assertTrue(detection.f(p1));
        
        Assert.assertFalse(detection.f(p2));
        p2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999999));
        p2 = p2.getClone();
        Assert.assertFalse(detection.f(p2));
        p2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.999998));
        p2 = p2.getClone();
        Assert.assertTrue(detection.f(p2));
        p2 = p2.getClone();
        detection.f(p2);
        Assert.assertEquals(3, ((TypeList) p2.getProperties().get(StandardNicheDetection.NicheEnum.NICHE_DETECTION_FITNESSES)).size());
    }

}
