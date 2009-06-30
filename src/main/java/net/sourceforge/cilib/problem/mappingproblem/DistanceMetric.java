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
package net.sourceforge.cilib.problem.mappingproblem;

/**
 * Represents a method for calculating the structure distance between two
 * vectors in a MappingProblem.
 *
 * @author jkroon
 */
public interface DistanceMetric {
    /**
     * Gets called by CurvilinearDistEvaluator to indicate the precice problem
     * to make use of.  It is recommended that the results of the distances
     * be calculated and placed into some kind of matrix at this point to allow
     * for faster execution.
     *
     * @param prob The underlying MappingProblem.
     */
    public void setMappingProblem(MappingProblem prob);

    /**
     * Gets called by CurvilinearDistEvaluator to request the distance between
     * two vectors to be returned.
     *
     * @param i The index of the "from" vector.
     * @param j The index of the "to" vector.
     *
     * @return The distance between to two vectors.
     */
    public double getDistance(int i, int j);
}
