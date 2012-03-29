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

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.measurement.single.diversity.Diversity;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.GBestCenterInitialisationStrategy;

/**
 *
 */
public class DiversityBasedMergeDetection extends MergeDetection {
    private MergeDetection mergeDetector;
    private Diversity diversityMeasure;
    private ControlParameter threshold;
    
    /**
     * Default constructor.
     */
    public DiversityBasedMergeDetection() {
        this.threshold = ConstantControlParameter.of(0.1);
        this.diversityMeasure = new Diversity();
        this.diversityMeasure.setPopulationCenter(new GBestCenterInitialisationStrategy());
        this.mergeDetector = new RadiusOverlapMergeDetection();
    }
    
    /**
     * Determines if two swarms should be merged based on whether they overlap and
     * if the diversity of the first swarm is below a threshold.
     * @param a
     * @param b
     * @return 
     */
    @Override
    public Boolean f(PopulationBasedAlgorithm a, PopulationBasedAlgorithm b) {
        if (diversityMeasure.getValue(a).doubleValue() < threshold.getParameter() && mergeDetector.f(a, b)) {
            return true;
        }
        
        return false;
    }

    public MergeDetection getMergeDetector() {
        return mergeDetector;
    }

    public void setMergeDetector(MergeDetection mergeDetector) {
        this.mergeDetector = mergeDetector;
    }

    public Diversity getDiversityMeasure() {
        return diversityMeasure;
    }

    public void setDiversityMeasure(Diversity diversityMeasure) {
        this.diversityMeasure = diversityMeasure;
    }

    public ControlParameter getThreshold() {
        return threshold;
    }

    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }
}
