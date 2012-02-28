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
/**
 *
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;

/**
 * @author leo
 * This is an implimentation of the Rand-to-best DE  target creation strategy. This implimentation is simply an extension of the {@linkplain RandCreationStrategy} that also includes the best {@linkplain Entity}'s solution vector. The influence of the best vector and the
 * random vector is determined by the greedynessParameter, which is sampled as E [0,1]. A value of 0 will ignore the contribution of the best {@linkplain Entity}, and a
 * value of 1 will ignore the controbution of the random {@linkplain Entity}.
 */
public class RandToBestCreationStrategy extends RandCreationStrategy {

    private static final long serialVersionUID = 413628791093573875L;
    private ControlParameter greedynessParameter;

    /**
     * Create a new instance of {@code RandToBestCreationStrategy}.
     */
    public RandToBestCreationStrategy() {
        super();
        greedynessParameter = ConstantControlParameter.of(0.5);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RandToBestCreationStrategy(RandToBestCreationStrategy other) {
        super(other);
        greedynessParameter = other.greedynessParameter.getClone();
    }

    /**
     * {@inheritDoc}
     */
    public Entity create(Entity targetEntity, Entity current,
            Topology<? extends Entity> topology) {
        Entity bestEntity = topology.getBestEntity();
        RandomProvider random = new MersenneTwister();
        List<Entity> participants = (List<Entity>) Selection.copyOf(topology.asList())
                .exclude(targetEntity, bestEntity, current)
                .orderBy(new RandomArrangement(random))
                .select(Samples.first((int) numberOfDifferenceVectors.getParameter()).unique());
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = ((Vector) targetEntity.getCandidateSolution()).multiply(1 - greedynessParameter.getParameter());
        Vector bestVector = ((Vector) bestEntity.getCandidateSolution()).multiply(greedynessParameter.getParameter());

        Vector trialVector = bestVector.plus(targetVector.plus(differenceVector.multiply(scaleParameter.getParameter())));

        Entity trialEntity = current.getClone();
        trialEntity.setCandidateSolution(trialVector);

        return trialEntity;
    }

    /**
     * {@inheritDoc}
     */
    public RandToBestCreationStrategy getClone() {
        return new RandToBestCreationStrategy(this);
    }

    public void setGreedynessParameter(ControlParameter greedynessParameter) {
        this.greedynessParameter = greedynessParameter;
    }
}
