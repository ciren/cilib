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
package net.sourceforge.cilib.pso.niching;

import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.MinimisationFitness;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StandardNicheIdentificationStrategyTest {

    /**
     * Identify a niche when the fitness of an Entity does not change
     * for a period of time.
     */
    @Test
    public void simpleIdentification() {
        Individual i1 = new Individual();

        // Set the fitness directly
        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));

        Topology<Individual> topology = new GBestTopology<Individual>();
        topology.add(i1);

        StandardNicheIdentificationStrategy nicheIdentificationStrategy = new StandardNicheIdentificationStrategy();
        nicheIdentificationStrategy.setThreshold(ConstantControlParameter.of(0.1E-6));

        List<Entity> results = nicheIdentificationStrategy.identify(topology);
        Assert.assertEquals(0, results.size());
        results = nicheIdentificationStrategy.identify(topology);
        Assert.assertEquals(0, results.size());

        results = nicheIdentificationStrategy.identify(topology);
        Assert.assertEquals(1, results.size());
    }

    /**
     * Identify a niche when the fitness of an entity is changed midway.
     */
    @Test
    public void identificationWithChange() {
        Individual i1 = new Individual();

        // Set the fitness directly
        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));

        Topology<Individual> topology = new GBestTopology<Individual>();
        topology.add(i1);

        StandardNicheIdentificationStrategy nicheIdentificationStrategy = new StandardNicheIdentificationStrategy();
        nicheIdentificationStrategy.setThreshold(ConstantControlParameter.of(1.0));

        nicheIdentificationStrategy.identify(topology);

        // Now apply a change to the fitness of the entity. This will
        // reset the niche identification to use the new fitness value.
        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(4.0));

        List<Entity> results = nicheIdentificationStrategy.identify(topology);
        Assert.assertEquals(0, results.size());
        results = nicheIdentificationStrategy.identify(topology);
        Assert.assertEquals(0, results.size());

        results = nicheIdentificationStrategy.identify(topology);
        Assert.assertEquals(1, results.size());
    }

}
