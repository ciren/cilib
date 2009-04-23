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
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;

/**
 * @author Wiehann Matthysen
 */
public class FitnessBasedEntityWeighingStrategy implements EntityWeighingStrategy {
    private static final long serialVersionUID = -4522086384977507012L;

    public FitnessBasedEntityWeighingStrategy() {}

    public FitnessBasedEntityWeighingStrategy(FitnessBasedEntityWeighingStrategy copy) { }

    @Override
    public FitnessBasedEntityWeighingStrategy getClone() {
        return new FitnessBasedEntityWeighingStrategy(this);
    }

    // Hmmm: We need to look at this... needs to be cleaner.
    protected Pair<Fitness, Fitness> getMinMaxFitness(Collection<? extends Entity> entities) {
        Entity initial = entities.iterator().next();
        Pair<Fitness, Fitness> minMaxFitness = new Pair<Fitness, Fitness>(InferiorFitness.instance(), InferiorFitness.instance());

        if (initial instanceof SocialEntity) {
            for (Entity entity : entities) {
                SocialEntity socialEntity = (SocialEntity) entity;
                if (socialEntity.getSocialBestFitness().compareTo(minMaxFitness.getKey()) < 0) {
                    minMaxFitness.setKey(socialEntity.getSocialBestFitness());
                }
                else if (socialEntity.getSocialBestFitness().compareTo(minMaxFitness.getValue()) > 0) {
                    minMaxFitness.setValue(socialEntity.getSocialBestFitness());
                }
            }
        }
        else {
            for (Entity entity : entities) {
                if (entity.getFitness().compareTo(minMaxFitness.getKey()) < 0) {
                    minMaxFitness.setKey(entity.getFitness());
                }
                else if (entity.getFitness().compareTo(minMaxFitness.getValue()) > 0) {
                    minMaxFitness.setValue(entity.getFitness());
                }
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
            double entityWeight = 0.0;

            if (entity instanceof SocialEntity)
                entityWeight = (((SocialEntity) entity).getSocialBestFitness().getValue() - minMaxFitness.getKey().getValue()) / minMaxDifference;
            else entityWeight = (entity.getFitness().getValue() - minMaxFitness.getKey().getValue()) / minMaxDifference;

            weighedEntities.add(new Pair<Double, Entity>(entityWeight, entity));
        }
        return weighedEntities;
    }
}
