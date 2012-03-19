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
package net.sourceforge.cilib.problem.dataset;

import java.util.ArrayList;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * All datasets that will be clustered have to implement this interface.
 */
public interface ClusterableDataSet {

    int getNumberOfPatterns();

    Pattern getPattern(int index);

    ArrayList<Pattern> getPatterns();
    Vector getMean();
    double getVariance();
    double getCachedDistance(int x, int y);
    void initialise();

    /**
     * TODO: Complete this javadoc.
     */
    public class Pattern implements Cloneable {
        private static final long serialVersionUID = 8831874859964777328L;
        private String clas = "<not set>";
        public Vector data = null;

        public Pattern(String c, Vector d) {
            clas = c;
            data = d;
        }

        public Pattern(Pattern rhs) {
            clas = rhs.clas;
            data = rhs.data;
        }

        public Pattern getClone() {
            return new Pattern(this);
        }

        public String toString() {
            return clas + " -> " + data;
        }
    }
}
