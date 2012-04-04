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
package net.sourceforge.cilib.util;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Utility methods for Entities.
 */
public class Entities {
    /**
     * Gets the candidate solutions from a list of entities.
     * @param entities The list of entities.
     * @return The list of candidate solutions.
     */
    public static <T extends StructuredType> List<T> getCandidateSolutions(List<? extends Entity> entities) {
        List<T> solutions = new ArrayList<T>();
        
        for (Entity e : entities) {
            solutions.add((T) e.getCandidateSolution());
        }
        
        return solutions;
    }
}
