/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging.detection;

import fj.F;
import fj.data.List;
import fj.function.Booleans;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

public class CompositeMergeDetection extends MergeDetection {

    private List<MergeDetection> detectors;

    public CompositeMergeDetection() {
        MergeDetection m = new AlwaysMergeDetection();
        this.detectors = List.list(m);
    }

    @Override
    public Boolean f(final PopulationBasedAlgorithm a, final PopulationBasedAlgorithm b) {
        return Booleans.and(detectors.map(new F<MergeDetection, Boolean>() {
            @Override
            public Boolean f(MergeDetection c) {
                return c.f(a, b);
            }
        }));
    }

    public void addDetector(MergeDetection m) {
        detectors = detectors.cons(m);
    }
}
