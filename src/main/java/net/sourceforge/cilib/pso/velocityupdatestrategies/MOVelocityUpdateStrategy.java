/**
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.GuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.iterationstrategies.GuideSelectionIterationStep;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Generic {@link VelocityUpdateStrategy} implementation for most Multi-objective PSOs.
 * This class is equivalent to the {@link StandardVelocityUpdate} class except that
 * it makes use of a local and global guide of a particle to calculate the velocity vector
 * instead of the pBest and lBest (or gBest) particle positions respectively. (see
 * {@link GuideSelectionStrategy} and {@link GuideSelectionIterationStep} on how these
 * guides are selected).
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class MOVelocityUpdateStrategy extends StandardVelocityUpdate {

    private static final long serialVersionUID = -2341493848729967941L;

    public MOVelocityUpdateStrategy() {
        super();
    }

    public MOVelocityUpdateStrategy(MOVelocityUpdateStrategy copy) {
        super(copy);
    }

    @Override
    public MOVelocityUpdateStrategy getClone() {
        return new MOVelocityUpdateStrategy(this);
    }

    @Override
    public void updateVelocity(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) particle.getProperties().get(EntityType.Particle.Guide.LOCAL_GUIDE);
        Vector globalGuide = (Vector) particle.getProperties().get(EntityType.Particle.Guide.GLOBAL_GUIDE);

        for (int i = 0; i < particle.getDimension(); ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.getReal(i) +
                    (localGuide.getReal(i) - position.getReal(i)) * this.cognitiveAcceleration.getParameter() +
                    (globalGuide.getReal(i) - position.getReal(i)) * this.socialAcceleration.getParameter();
            velocity.setReal(i, value);

            clamp(velocity, i);
        }
    }
}
