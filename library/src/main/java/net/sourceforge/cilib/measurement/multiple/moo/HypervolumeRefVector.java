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
package net.sourceforge.cilib.measurement.multiple.moo;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * <p>
 * Determines the hv reference vector and prints it out.
 * The reference vector contains the worst values for each objective for the
 * specific iteration.
 *
 * This is used for the offline hypervolume calculations
 * </p>
 *
 * @author Marde Greeff
 */
public class HypervolumeRefVector implements Measurement {

    
    public HypervolumeRefVector() {
    }

    public HypervolumeRefVector (HypervolumeRefVector copy) {
    }

    @Override
    public HypervolumeRefVector getClone() {
        return new HypervolumeRefVector(this);
    }

    
    public String getDomain() {
        return "T";
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList refVec = new TypeList();
        
        Archive archive = Archive.Provider.get();         
        
        int counter = 0;
        for (OptimisationSolution solution : archive) {
            MOFitness fitnesses = (MOFitness) solution.getFitness();
            if (counter == 0) {
                for (int k=0; k < fitnesses.getDimension(); k++)
                    refVec.add(fitnesses.getFitness(k));
                counter = 1;
            }
            else {
                TypeList temp = new TypeList();
                for (int kk=0; kk < fitnesses.getDimension(); kk++) {
                    if (fitnesses.getFitness(kk).getValue().doubleValue() > ((Fitness)refVec.get(kk)).getValue().doubleValue())
                        temp.add(fitnesses.getFitness(kk));
                    else temp.add(refVec.get(kk));
                }
                refVec.clear();
                refVec.addAll(temp);                
            }            
        }        
        
        return refVec;
    }
}
