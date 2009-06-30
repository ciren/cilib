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
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.parser.ParseException;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Gary Pampara
 */
public class SolutionTest {

    @Test
    public void result() {
        Vector target = Vectors.create(1.0);
        Individual i = new Individual();
        i.getProperties().put(EntityType.CANDIDATE_SOLUTION, target);

        EC ec = new EC();
        Topology<Individual> topology = (Topology<Individual>) ec.getTopology();
        topology.add(i);

        Measurement m = new Solution();
        Assert.assertEquals(m.getValue(ec).toString(), target.toString());
    }

    @Test
    public void testSolutionDomain() throws ParseException {
        Measurement m = new Solution();

        TypeList vector = (TypeList) DomainParser.parse(m.getDomain());

        assertEquals(1, vector.size());
        assertTrue(vector.get(0) instanceof StringType);
    }

}
