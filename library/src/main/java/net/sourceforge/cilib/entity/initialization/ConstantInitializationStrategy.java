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
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @param <E>
 */
public class ConstantInitializationStrategy<E extends Entity> implements InitializationStrategy<E> {
    private static final long serialVersionUID = 4198258321374130337L;

    private double constant;

    public ConstantInitializationStrategy() {
        this.constant = 0.0;
    }

    public ConstantInitializationStrategy(double value) {
        this.constant = value;
    }

    public ConstantInitializationStrategy(ConstantInitializationStrategy copy) {
        this.constant = copy.constant;
    }

    @Override
    public ConstantInitializationStrategy getClone() {
        return new ConstantInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);

        if (type instanceof Vector) {
            Vector vector = (Vector) type;

            for (int i = 0; i < vector.size(); i++) {
                vector.setReal(i, constant);
            }

            return;
        }

        throw new UnsupportedOperationException("Cannot perfrom initialization on non Vector type.");
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

}
