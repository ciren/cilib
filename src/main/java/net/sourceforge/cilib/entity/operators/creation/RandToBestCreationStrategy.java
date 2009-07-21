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
/**
 *
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Selection;

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
        greedynessParameter = new ConstantControlParameter(0.5);
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
        Random random = new MersenneTwister();
        List<Entity> participants = Selection.from(topology.asList()).exclude(targetEntity, bestEntity, current).unique().random(random, (int)numberOfDifferenceVectors.getParameter()).select();
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = ((Vector) targetEntity.getCandidateSolution()).multiply(1 - greedynessParameter.getParameter());
        Vector bestVector =   ((Vector) bestEntity.getCandidateSolution()).multiply(greedynessParameter.getParameter());

        Vector trialVector =  bestVector.plus(targetVector.plus(differenceVector.multiply(scaleParameter.getParameter())));

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void performOperation(TopologyHolder holder) {
        throw new UnsupportedOperationException("Not supported yet. This may need some more refactoring. May require looping operator?");
    }

    public void setGreedynessParameter(ControlParameter greedynessParameter) {
        this.greedynessParameter = greedynessParameter;
    }

}
