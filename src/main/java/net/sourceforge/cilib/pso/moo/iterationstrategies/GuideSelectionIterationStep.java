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

package net.sourceforge.cilib.pso.moo.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.CompositeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.moo.iterationstrategies.ArchivingIterationStep;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.GuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.NBestGuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.guideselectionstrategies.PBestGuideSelectionStrategy;
import net.sourceforge.cilib.pso.moo.guideupdatestrategies.GuideUpdateStrategy;
import net.sourceforge.cilib.pso.moo.guideupdatestrategies.StandardGuideUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * With Multi-objective {@link PSO}s the pBest and lBest (or gBest) particles are replaced with the
 * concept of local and global guides respectively. This class is a concrete implementation of
 * {@link IterationStrategy} and makes use of two {@link GuideSelectionStrategy} instances that is
 * responsible for selecting these guides for every particle. After the guides have been selected,
 * different criteria can be used to determine if the particle's guides should be updated. This
 * is achieved by making use of two {@link GuideUpdateStrategy} instances.
 * </p>
 *
 * <p>
 * {@code GuideSelectionIterationStep} is best used as sub-component of a
 * {@link CompositeIterationStrategy} where it can be combined with the normal iteration strategies
 * (like PSO's {@link SynchronousIterationStrategy} etc.). In addition to this class you might also
 * want to look at {@link ArchivingIterationStep} to store the solutions in an {@link Archive}.
 * </p>
 * 
 * @author Wiehann Matthysen
 */
public class GuideSelectionIterationStep implements IterationStrategy<PSO> {
	private static final long serialVersionUID = 4326469501919309078L;
	
	private GuideSelectionStrategy localGuideSelectionStrategy;
	private GuideSelectionStrategy globalGuideSelectionStrategy;
	private GuideUpdateStrategy localGuideUpdateStrategy;
	private GuideUpdateStrategy globalGuideUpdateStrategy;
	
	public GuideSelectionIterationStep() {
		this.localGuideSelectionStrategy = new PBestGuideSelectionStrategy();
		this.globalGuideSelectionStrategy = new NBestGuideSelectionStrategy();
		this.localGuideUpdateStrategy = new StandardGuideUpdateStrategy();
		this.globalGuideUpdateStrategy = new StandardGuideUpdateStrategy();
	}
	
	public GuideSelectionIterationStep(GuideSelectionIterationStep copy) {
		this.localGuideSelectionStrategy = copy.localGuideSelectionStrategy.getClone();
		this.globalGuideSelectionStrategy = copy.globalGuideSelectionStrategy.getClone();
		this.localGuideUpdateStrategy = copy.localGuideUpdateStrategy.getClone();
		this.globalGuideUpdateStrategy = copy.globalGuideUpdateStrategy.getClone();
	}

	@Override
	public GuideSelectionIterationStep getClone() {
		return new GuideSelectionIterationStep(this);
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
	
	@Override
	public void performIteration(PSO algorithm) {
		selectGuides(algorithm);
	}
	
	protected void selectGuides(PSO currentSwarm) {
		for (Particle particle : currentSwarm.getTopology()) {
			Vector localGuide = this.localGuideSelectionStrategy.selectGuide(particle);
			this.localGuideUpdateStrategy.updateGuide(particle, EntityType.Particle.Guide.LOCAL_GUIDE, localGuide);
			Vector globalGuide = this.globalGuideSelectionStrategy.selectGuide(particle);
			this.globalGuideUpdateStrategy.updateGuide(particle, EntityType.Particle.Guide.GLOBAL_GUIDE, globalGuide);
		}
	}
}
