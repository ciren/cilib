/*
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
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;

/**
 * Velocity update strategy for QSO (Quantum PSO). Implemented according
 * to paper by Blackwell and Branke, "Multiswarms, Exclusion, and Anti-
 * Convergence in Dynamic Environments."
 *
 * @author Anna Rakitianskaia
 *
 */
public class QuantumVelocityUpdateStrategy extends StandardVelocityUpdate {

    private static final long serialVersionUID = -940568473388702506L;
    private static final double EPSILON = 0.000000001;

    /**
     * Create a new instance of {@linkplain QuantumPositionUpdateStrategy}.
     */
    public QuantumVelocityUpdateStrategy() {
    }

    /**
     * Create an copy of the provided instance.
     * @param copy The instance to copy.
     */
    public QuantumVelocityUpdateStrategy(StandardVelocityUpdate copy) {
        super(copy);
    }

    /**
     * Update particle velocity; do it in a standard way if the particle is neutral, and
     * do not update it if the particle is quantum (charged), since quantum particles do
     * not use the velocity to update their positions.
     * @param particle the particle to update position of
     */
    public void updateVelocity(Particle particle) {
        ChargedParticle checkChargeParticle = (ChargedParticle) particle;
        if(checkChargeParticle.getCharge() < EPSILON) {    // the particle is neutral
            super.updateVelocity(particle);
        }
    }
}
