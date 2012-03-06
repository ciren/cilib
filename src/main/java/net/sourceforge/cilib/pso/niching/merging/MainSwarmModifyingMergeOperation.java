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
package net.sourceforge.cilib.pso.niching.merging;

import fj.P;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 *
 * @author filipe
 */
public class MainSwarmModifyingMergeOperation extends MergeOperation {
    private MergeDetection mergeDetection;
    private MergeStrategy mergeStrategy;
    
    public MainSwarmModifyingMergeOperation() {
        this.mergeDetection = new RadiusOverlapMergeDetection();
        this.mergeStrategy = new ReinitialisingMergeStrategy();
    }

    @Override
    public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(PopulationBasedAlgorithm mainSwarm, List<PopulationBasedAlgorithm> subSwarms) {
        if (subSwarms.isEmpty()) {
            return P.p(mainSwarm, fj.data.List.<PopulationBasedAlgorithm>nil());
        }
        
        if (subSwarms.length() == 1) {
            return P.p(mainSwarm, fj.data.List.single(subSwarms.head()));
        }
        
        return P.p(subSwarms.filter(mergeDetection.f(subSwarms.head())).foldLeft(mergeStrategy, mainSwarm), 
                fj.data.List.cons(subSwarms.filter(mergeDetection.f(subSwarms.head())).foldLeft(mergeStrategy, mainSwarm),
                this.f(mainSwarm, subSwarms.removeAll(mergeDetection.f(subSwarms.head())))._2()));
    }

    public MergeDetection getMergeDetection() {
        return mergeDetection;
    }

    public void setMergeDetection(MergeDetection mergeDetection) {
        this.mergeDetection = mergeDetection;
    }

    public MergeStrategy getMergeStrategy() {
        return mergeStrategy;
    }

    public void setMergeStrategy(MergeStrategy mergeStrategy) {
        this.mergeStrategy = mergeStrategy;
    }
}
