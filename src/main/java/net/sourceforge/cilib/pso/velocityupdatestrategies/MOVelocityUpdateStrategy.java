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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.GuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.NBestGuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.PBestGuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.guideupdatestrategies.GuideUpdateStrategy;
import net.sourceforge.cilib.pso.moo.guideupdatestrategies.StandardGuideUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * With Multi-objective {@link PSO}s the pBest and lBest (or gBest) particles are replaced with the
 * concept of local and global guides respectively. This class is a generic {@link VelocityUpdateStrategy}
 * implementation for most Multi-objective PSOs. It makes use of two {@link GuideSelectionStrategy}
 * instances that is responsible for selecting these guides for every particle. After the guides have
 * been selected, different criteria can be used to determine if the particle's guides should be updated.
 * This is achieved by making use of two {@link GuideUpdateStrategy} instances.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class MOVelocityUpdateStrategy extends StandardVelocityUpdate {

    private static final long serialVersionUID = -2341493848729967941L;

    private GuideSelectionStrategy localGuideSelectionStrategy;
    private GuideSelectionStrategy globalGuideSelectionStrategy;
    private GuideUpdateStrategy localGuideUpdateStrategy;
    private GuideUpdateStrategy globalGuideUpdateStrategy;

    public MOVelocityUpdateStrategy() {
        super();
        this.localGuideSelectionStrategy = new PBestGuideSelectionStrategy();
        this.globalGuideSelectionStrategy = new NBestGuideSelectionStrategy();
        this.localGuideUpdateStrategy = new StandardGuideUpdateStrategy();
        this.globalGuideUpdateStrategy = new StandardGuideUpdateStrategy();
    }

    public MOVelocityUpdateStrategy(MOVelocityUpdateStrategy copy) {
        super(copy);
        this.localGuideSelectionStrategy = copy.localGuideSelectionStrategy.getClone();
        this.globalGuideSelectionStrategy = copy.globalGuideSelectionStrategy.getClone();
        this.localGuideUpdateStrategy = copy.localGuideUpdateStrategy.getClone();
        this.globalGuideUpdateStrategy = copy.globalGuideUpdateStrategy.getClone();
    }

    @Override
    public MOVelocityUpdateStrategy getClone() {
        return new MOVelocityUpdateStrategy(this);
    }

    public void setLocalGuideSelectionStrategy(GuideSelectionStrategy localGuideSelectionStrategy) {
        this.localGuideSelectionStrategy = localGuideSelectionStrategy;
    }

    public GuideSelectionStrategy getLocalGuideSelectionStrategy() {
        return this.localGuideSelectionStrategy;
    }

    public void setGlobalGuideSelectionStrategy(GuideSelectionStrategy globalGuideSelectionStrategy) {
        this.globalGuideSelectionStrategy = globalGuideSelectionStrategy;
    }

    public GuideSelectionStrategy getGlobalGuideSelectionStrategy() {
        return this.globalGuideSelectionStrategy;
    }

    public void setLocalGuideUpdateStrategy(GuideUpdateStrategy localGuideUpdateStrategy) {
        this.localGuideUpdateStrategy = localGuideUpdateStrategy;
    }

    public GuideUpdateStrategy getLocalGuideUpdateStrategy() {
        return this.localGuideUpdateStrategy;
    }

    public void setGlobalGuideUpdateStrategy(GuideUpdateStrategy globalGuideUpdateStrategy) {
        this.globalGuideUpdateStrategy = globalGuideUpdateStrategy;
    }

    public GuideUpdateStrategy getGlobalGuideUpdateStrategy() {
        return this.globalGuideUpdateStrategy;
    }

    protected void selectGuides(Particle particle) {
        Vector localGuide = this.localGuideSelectionStrategy.selectGuide(particle);
        this.localGuideUpdateStrategy.updateGuide(particle, EntityType.Particle.Guide.LOCAL_GUIDE, localGuide);
        Vector globalGuide = this.globalGuideSelectionStrategy.selectGuide(particle);
        this.globalGuideUpdateStrategy.updateGuide(particle, EntityType.Particle.Guide.GLOBAL_GUIDE, globalGuide);
    }

    @Override
    public void updateVelocity(Particle particle) {
        selectGuides(particle);

        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector gbest = (Vector) particle.getNeighbourhoodBest().getCandidateSolution();
        Vector localGuide = (Vector) particle.getProperties().get(EntityType.Particle.Guide.LOCAL_GUIDE);
        Vector globalGuide = (Vector) particle.getProperties().get(EntityType.Particle.Guide.GLOBAL_GUIDE);

        int min = Math.min(localGuide.getDimension(), globalGuide.getDimension());
        int i = 0;

        for (; i < min; ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.getReal(i) +
                    (localGuide.getReal(i) - position.getReal(i)) * this.cognitiveAcceleration.getParameter() +
                    (globalGuide.getReal(i) - position.getReal(i)) * this.socialAcceleration.getParameter();
            velocity.setReal(i, value);

            clamp(velocity, i);
        }

        for (; i < particle.getDimension(); ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.getReal(i) +
                    (localGuide.getReal(i) - position.getReal(i)) * this.cognitiveAcceleration.getParameter() +
                    (gbest.getReal(i) - position.getReal(i)) * this.socialAcceleration.getParameter();
            velocity.setReal(i, value);

            clamp(velocity, i);
        }
    }
}
