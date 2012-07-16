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
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Randomizable;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 * @param <E>
 */
public class RandomInitializationStrategy<E extends Entity> implements InitializationStrategy<E> {
    private static final long serialVersionUID = 5630272366805104400L;

    private RandomProvider random;

    public RandomInitializationStrategy() {
        this.random = new MersenneTwister();
    }

    public RandomInitializationStrategy(RandomInitializationStrategy copy) {
//        this.random = copy.random.getClone();
        this.random = new MersenneTwister();
    }

    @Override
    public RandomInitializationStrategy getClone() {
        return new RandomInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);

        if (type instanceof Randomizable) {
            Randomizable randomizable = (Randomizable) type;
            randomizable.randomize(random);
            return;
        }

        throw new UnsupportedOperationException("Cannot initialize a non Randomizable instance.");
    }

}
