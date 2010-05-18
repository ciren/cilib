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
package net.sourceforge.cilib.util.selection.weighing.entity;

import net.sourceforge.cilib.util.selection.weighing.*;
import java.util.Collection;
import java.util.List;
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
    public EntityWeighing<E> getClone() {
        return new EntityWeighing<E>(this);
    }

    /**
     * Obtain the minimum and maximum fitness values.
     * @param entities The entity objects to inspect.
     * @return A {@code FitnessTuple2} holding the minimum and maximum fitness values.
     */
    private FitnessTuple2 getMinMaxFitness(Collection<Selection.Entry<E>> entities) {
        FitnessTuple2 minMaxFitness = new FitnessTuple2(InferiorFitness.instance(), InferiorFitness.instance());
        for (Selection.Entry<E> entity : entities) {
            Fitness fitness = this.entityFitness.getFitness(entity.getElement());
            if (minMaxFitness.getFirst() == InferiorFitness.instance() || fitness.compareTo(minMaxFitness.getFirst()) < 0) {
                minMaxFitness = new FitnessTuple2(fitness, minMaxFitness.getSecond());
            }
            if (minMaxFitness.getSecond() == InferiorFitness.instance() || fitness.compareTo(minMaxFitness.getSecond()) > 0) {
                minMaxFitness = new FitnessTuple2(minMaxFitness.getFirst(), fitness);
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
        FitnessTuple2 minMaxFitness = getMinMaxFitness(entities);

        if (minMaxFitness.getFirst() == InferiorFitness.instance()
                || minMaxFitness.getSecond() == InferiorFitness.instance()) {
            throw new UnsupportedOperationException("Cannot weigh entities where all entities have Inferior fitness.");
        }

        double minMaxDifference = minMaxFitness.getSecond().getValue() - minMaxFitness.getFirst().getValue();

        for (Selection.Entry<E> entity : entities) {
            double weight = (this.entityFitness.getFitness(entity.getElement()).getValue() - minMaxFitness.getFirst().getValue()) / minMaxDifference;
            entity.setWeight(weight);
        }

        return true;
    }

    private static final class FitnessTuple2 {

        private final Fitness f1;
        private final Fitness f2;

        FitnessTuple2(Fitness f1, Fitness f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        Fitness getFirst() {
            return f1;
        }

        Fitness getSecond() {
            return f2;
        }
    }
}
