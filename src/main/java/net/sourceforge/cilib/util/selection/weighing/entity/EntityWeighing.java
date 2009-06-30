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
package net.sourceforge.cilib.util.selection.weighing.entity;

import net.sourceforge.cilib.util.selection.weighing.*;
import java.util.Collection;
import java.util.List;
import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.util.selection.Selection;

/**
 * Apply a weighing based on the {@link Fitness} of {@link Entity} instances.
 * @param <E> The entity type.
 * @author Wiehann Matthysen
 */
public class EntityWeighing<E extends Entity> implements Weighing<E> {

    private static final long serialVersionUID = 5906567326258195932L;
    private EntityFitness<E> entityFitness;

    /**
     * Create a new instance.
     */
    public EntityWeighing() {
        this.entityFitness = new CurrentFitness();
    }

    /**
     * Create a new instance with the provided {@link EntityFitness} strategy.
     * @param entityFitness The value to set.
     */
    public EntityWeighing(EntityFitness<E> entityFitness) {
        this.entityFitness = entityFitness;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public EntityWeighing(EntityWeighing copy) {
        this.entityFitness = copy.entityFitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityWeighing getClone() {
        return new EntityWeighing(this);
    }

    /**
     * Obtain the minimum and maximum fitness values.
     * @param entities The entity objects to inspect.
     * @return A {@link Pair} holding the minimum and maximum fitness values.
     */
    private Pair<Fitness, Fitness> getMinMaxFitness(Collection<Selection.Entry<E>> entities) {
        Pair<Fitness, Fitness> minMaxFitness = new Pair<Fitness, Fitness>(InferiorFitness.instance(), InferiorFitness.instance());
        for (Selection.Entry<E> entity : entities) {
            Fitness fitness = this.entityFitness.getFitness(entity.getElement());
            if (minMaxFitness.getKey() == InferiorFitness.instance() || fitness.compareTo(minMaxFitness.getKey()) < 0) {
                minMaxFitness.setKey(fitness);
            }
            if (minMaxFitness.getValue() == InferiorFitness.instance() || fitness.compareTo(minMaxFitness.getValue()) > 0) {
                minMaxFitness.setValue(fitness);
            }
        }
        return minMaxFitness;
    }

    /**
     * {@inheritDoc}
     * @param entities The entities to weigh.
     */
    @Override
    public boolean weigh(List<Selection.Entry<E>> entities) {
        Pair<Fitness, Fitness> minMaxFitness = getMinMaxFitness(entities);

        if (minMaxFitness.getKey() == InferiorFitness.instance() ||
                minMaxFitness.getValue() == InferiorFitness.instance()) {
            throw new UnsupportedOperationException("Cannot weigh entities where all entities have Inferior fitness.");
        }

        double minMaxDifference = minMaxFitness.getValue().getValue() - minMaxFitness.getKey().getValue();

        for (Selection.Entry<E> entity : entities) {
            double weight = (this.entityFitness.getFitness(entity.getElement()).getValue() - minMaxFitness.getKey().getValue()) / minMaxDifference;
            entity.setWeight(weight);
        }

        return true;
    }
}
