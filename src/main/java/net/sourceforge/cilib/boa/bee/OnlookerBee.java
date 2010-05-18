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
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.boa.ABC;


/**
 * Represents an onlooker bee in the hive.
 * @author Andrich
 *
 */
public class OnlookerBee extends AbstractBee {
    private static final long serialVersionUID = -4714791530850285930L;

    /**
     * Default constructor.
     */
    public OnlookerBee() {
    }

    /**
     * Copy constructor.
     * @param bee the original bee to copy.
     */
    public OnlookerBee(AbstractBee bee) {
        super(bee);
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy reference that is deep copied.
     */
    public OnlookerBee(OnlookerBee copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OnlookerBee getClone() {
        return new OnlookerBee(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        ABC algorithm = (ABC) AbstractAlgorithm.get();
        HoneyBee target = targetSelectionStrategy.select(algorithm.getWorkerBees());

        while (target == this) {
            target = targetSelectionStrategy.select(algorithm.getWorkerBees());
        }

        this.positionUpdateStrategy.updatePosition(this, target);
    }

}
