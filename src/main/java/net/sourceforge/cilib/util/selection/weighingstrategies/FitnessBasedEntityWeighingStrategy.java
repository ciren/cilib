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

package net.sourceforge.cilib.util.selection.weighingstrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;

/**
 * @author Wiehann Matthysen
 */
public class FitnessBasedEntityWeighingStrategy implements EntityWeighingStrategy {
	private static final long serialVersionUID = -4522086384977507012L;
	
	public FitnessBasedEntityWeighingStrategy() { }
	
	public FitnessBasedEntityWeighingStrategy(FitnessBasedEntityWeighingStrategy copy) { }

	@Override
	public FitnessBasedEntityWeighingStrategy getClone() {
		return new FitnessBasedEntityWeighingStrategy(this);
	}
	
	protected Pair<Fitness, Fitness> getMinMaxFitness(Collection<? extends Entity> entities) {
		Fitness initialFitness = entities.iterator().next().getSocialBestFitness();
		Pair<Fitness, Fitness> minMaxFitness = new Pair<Fitness, Fitness>(initialFitness, initialFitness);
		for (Entity entity : entities) {
			if (entity.getSocialBestFitness().compareTo(minMaxFitness.getKey()) < 0) {
				minMaxFitness.setKey(entity.getSocialBestFitness());
			}
			else if (entity.getSocialBestFitness().compareTo(minMaxFitness.getValue()) > 0) {
				minMaxFitness.setValue(entity.getSocialBestFitness());
			}
		}
		return minMaxFitness;
	}

	@Override
	public List<Pair<Double, Entity>> weigh(Collection<? extends Entity> entities) {
		Pair<Fitness, Fitness> minMaxFitness = getMinMaxFitness(entities);
		double minMaxDifference = minMaxFitness.getValue().getValue() - minMaxFitness.getKey().getValue();
		List<Pair<Double, Entity>> weighedEntities = new ArrayList<Pair<Double, Entity>>();
		for (Entity entity : entities) {
			double entityWeight = (entity.getSocialBestFitness().getValue() - minMaxFitness.getKey().getValue()) / minMaxDifference;
			weighedEntities.add(new Pair<Double, Entity>(entityWeight, entity));
		}
		return weighedEntities;
	}
}
