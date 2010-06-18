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

        int min = Math.min(localGuide.size(), globalGuide.size());
        int i = 0;

        for (; i < min; ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.doubleValueOf(i) +
                    (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.cognitiveAcceleration.getParameter() +
                    (globalGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.socialAcceleration.getParameter();
            velocity.setReal(i, value);

            clamp(velocity, i);
        }

        for (; i < particle.getDimension(); ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.doubleValueOf(i) +
                    (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.cognitiveAcceleration.getParameter() +
                    (gbest.doubleValueOf(i) - position.doubleValueOf(i)) * this.socialAcceleration.getParameter();
            velocity.setReal(i, value);

            clamp(velocity, i);
        }
    }
}
