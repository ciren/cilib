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
package net.sourceforge.cilib.util.selection.weighting;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 *
 * @author gpampara
 */
public class EntityWeighting implements Weighting {

    private EntityFitness<Entity> entityFitness;

    public EntityWeighting() {
        this.entityFitness = new CurrentFitness();
    }

    public EntityWeighting(EntityFitness<Entity> entityFitness) {
        this.entityFitness = entityFitness;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        Preconditions.checkArgument(Iterables.get(iterable, 0) instanceof Entity);
        MinMaxFitness minMaxFitness = getMinMaxFitness(Lists.newArrayList(iterable));
        List<WeightedObject> result = Lists.newArrayList();

        if (minMaxFitness.getFirst() == InferiorFitness.instance()
                || minMaxFitness.getSecond() == InferiorFitness.instance()) {
            throw new UnsupportedOperationException("Cannot weigh entities where all entities have Inferior fitness.");
        }

        double minMaxDifference = minMaxFitness.getSecond().getValue() - minMaxFitness.getFirst().getValue();

        for (T t : iterable) {
            double weight = (this.entityFitness.getFitness((Entity) t).getValue() - minMaxFitness.getFirst().getValue()) / minMaxDifference;
            result.add(new WeightedObject(t, weight));
        }

        return result;
    }

    private <T> MinMaxFitness getMinMaxFitness(List<T> entities) {
        MinMaxFitness minMaxFitness = new MinMaxFitness(InferiorFitness.instance(), InferiorFitness.instance());
        for (T entity : entities) {
            Fitness fitness = this.entityFitness.getFitness((Entity) entity);
            if (minMaxFitness.getFirst() == InferiorFitness.instance() || fitness.compareTo(minMaxFitness.getFirst()) < 0) {
                minMaxFitness = new MinMaxFitness(fitness, minMaxFitness.getSecond());
            }
            if (minMaxFitness.getSecond() == InferiorFitness.instance() || fitness.compareTo(minMaxFitness.getSecond()) > 0) {
                minMaxFitness = new MinMaxFitness(minMaxFitness.getFirst(), fitness);
            }
        }
        return minMaxFitness;
    }

    private static final class MinMaxFitness {

        private final Fitness f1;
        private final Fitness f2;

        MinMaxFitness(Fitness f1, Fitness f2) {
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
