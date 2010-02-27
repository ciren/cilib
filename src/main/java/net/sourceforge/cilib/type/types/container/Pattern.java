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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.util.Vectors;

public class Pattern<S extends StructuredType<Numeric>> extends ForwardingStructuredType<Numeric> {
    private static final long serialVersionUID = 3018524182531891291L;

    private S data;
    private String classification;

    public Pattern(S data, String classification) {
        this.data = data;
        this.classification = classification;
    }

    public Pattern(Pattern<S> rhs) {
        this.data = (S) rhs.data.getClone();
        this.classification = rhs.classification;
    }

    @Override
    public S delegate() {
        return this.data;
    }

    /**
     * Convenience method.
     */
    public S getData() {
        return this.data;
    }

    @Override
    public Pattern<S> getClone() {
        return new Pattern<S>(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pattern<S> other = (Pattern<S>) obj;
        if ((this.delegate() == null) ? (other.delegate() != null) : !this.delegate().equals(other.delegate())) {
            return false;
        }
        if ((this.classification == null) ? (other.classification != null) : !this.classification.equals(other.classification)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.delegate() != null ? this.delegate().hashCode() : 0);
        hash = 97 * hash + (this.classification != null ? this.classification.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return Vectors.toString(this.data, "", "", "\t");
    }

    public String getClassification() {
        return this.classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
