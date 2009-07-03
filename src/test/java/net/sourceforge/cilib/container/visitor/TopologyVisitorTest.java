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
package net.sourceforge.cilib.container.visitor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.pso.PSO;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class TopologyVisitorTest {
    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    /**
     * Initialize a dummy algorithm and then test if the visitor knows
     * which algorithm it used for it's evaluation.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void currentAlgorithmUsed() {
        final Topology<Particle> topology = context.mock(Topology.class);
        final TopologyVisitor visitor = new RadiusVisitor();

        PSO pso = new PSO();
        pso.setTopology(topology);

        context.checking(new Expectations() {{
            one(topology).accept(visitor);
        }});

        pso.accept(visitor);

        assertThat(pso, is(visitor.getCurrentAlgorithm()));
    }

}
