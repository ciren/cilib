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
package net.sourceforge.cilib.util.selection.arrangement;

import java.util.Comparator;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * A Comparator that orders {@linkplain OptimisationSolutions} according to
 * their average Euclidean distance from two closest solutions in the archive.
 * We assume that the selection type will be OptimisationSolution.
 *
 * @author Marde Greeff
 *
 * @param <E> The selection type.
 */
public class DistanceComparator<E extends Comparable> implements Comparator<OptimisationSolution>{

	/**
	 * Two @link{OptimisationSolution} entries are compared based on the sum of their
	 * Euclidean distance to each of the solutions in the @link{Archive}. The
	 * distance is calculated based on the @link{MOFitness}.
	 *
	 * If o1's distance > o2's distance, -1 is returned.
	 * If o1's distance < o2's distance, 1 is returned.
	 * If o1's distance == o2's distance, 0 is returned.
	 */
    
    @Override
    public int compare(OptimisationSolution o1, OptimisationSolution o2) {
        double minDistance1_1 = 0.0;
        double minDistance1_2 = 0.0;
        double minDistance2_1 = 0.0;
        double minDistance2_2 = 0.0;
        //get solutions for o1 and o2
        
        MOFitness fitness1 = (MOFitness)(o1.getFitness());
        MOFitness fitness2 = (MOFitness)(o2.getFitness());

        int solutionIt = 0;
        //for each solution in the archive
		for (OptimisationSolution solution : Archive.Provider.get()) {
			MOFitness fitnesses = (MOFitness) solution.getFitness();

			double value1 = 0.0;
			double value2 = 0.0;

			for (int k=0; k<fitnesses.getDimension(); k++) {
				//calculate distance1
				value1 += Math.pow(fitnesses.getFitness(k).getValue() - fitness1.getFitness(k).getValue(), 2);
				//calculate distance2
				value2 += Math.pow(fitnesses.getFitness(k).getValue() - fitness2.getFitness(k).getValue(), 2);
			}

            //determine lowest 2 distances
            if (solutionIt == 0) {
                minDistance1_1 =  Math.sqrt(value1);
                minDistance2_1 = Math.sqrt(value2);

            }
            else if (solutionIt == 1) {
                minDistance1_2 =  Math.sqrt(value1);
                minDistance2_2 = Math.sqrt(value2);
            }
            else {
                double max1 = Math.max(minDistance1_1, minDistance1_2);
                double max2 = Math.max(minDistance2_1, minDistance2_2);

                if (minDistance1_1 == max1) {
                    if (Math.sqrt(value1) < max1)
                        minDistance1_1 = Math.sqrt(value1);
                }
                else if (minDistance1_2 == max1) {
                    if (Math.sqrt(value1) < max1)
                        minDistance1_2 = Math.sqrt(value1);
                }

               if (minDistance2_1 == max2) {
                    if (Math.sqrt(value2) < max2)
                        minDistance2_1 = Math.sqrt(value2);
                }
                else if (minDistance2_2 == max2) {
                    if (Math.sqrt(value2) < max2)
                        minDistance2_2 = Math.sqrt(value2);
                }

            }
            solutionIt++;
			
	}

        double distance1 = (double)((minDistance1_1 + minDistance1_2)/2.0);
        double distance2 = (double)((minDistance2_1 + minDistance2_2)/2.0);

	//always order them with the lowest distance last
	if (distance1 < distance2)
            return 1;
	else if (distance1 > distance2)
            return -1;
	else return 0;
    }
}

