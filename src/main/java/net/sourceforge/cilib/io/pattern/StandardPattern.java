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
package net.sourceforge.cilib.io.pattern;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents a data pattern: A feature vector and it's corresponding target.
 * The target type is a cloneable object, thereby being very unrestricted.
 *
 */
public class StandardPattern implements Cloneable {

    private Vector vector;
    private Type target;

    /**
     * Default constructor.
     */
    public StandardPattern() {
        vector = new Vector();
    }

    /**
     * Convenience constructor setting the vector and target.
     * @param vector the new pattern's vector.
     * @param target the new pattern's target.
     */
    public StandardPattern(Vector vector, Type target) {
        this.vector = Vector.copyOf(vector);
        this.target = target.getClone();
    }

    /**
     * Copy constructor.
     * @param orig the pattern to be copied.
     */
    public StandardPattern(StandardPattern orig) {
        this.vector = orig.vector.getClone();
        this.target = orig.target.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Object getClone() {
        return new StandardPattern(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StandardPattern other = (StandardPattern) obj;
        if (this.vector != other.vector && (this.vector == null || !this.vector.equals(other.vector))) {
            return false;
        }
        if (this.target != other.target && (this.target == null || !this.target.equals(other.target))) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.vector != null ? this.vector.hashCode() : 0);
        hash = 97 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }

    /**
     * Gets the pattern's target.
     * @return the pattern's target.
     */
    public Type getTarget() {
        return target;
    }

    /**
     * Sets the pattern's target.
     * @param target the pattern's new target.
     */
    public void setTarget(Type target) {
        this.target = target;
    }

    /**
     * Gets the pattern's feature vector.
     * @return the pattern's feature vector.
     */
    public Vector getVector() {
        return vector;
    }

    /**
     * Sets the pattern's feature vector.
     * @param vector the pattern's feature vector.
     */
    public void setVector(Vector vector) {
        this.vector = vector;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return this.vector.toString() +" "+ this.target.toString();
    }


}
