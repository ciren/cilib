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

import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.measurement.Measurement;

import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.Real;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class FitnessTest {

    @Test
    public void results() {
        EC ec = new EC();
        Topology<Individual> topology = (Topology<Individual>) ec.getTopology();

        Individual i = new Individual();
        i.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
        topology.add(i);

        Measurement m = new Fitness();
        Assert.assertEquals(0.0, ((Real) m.getValue(ec)).getReal(), 0.00001);
    }

}
