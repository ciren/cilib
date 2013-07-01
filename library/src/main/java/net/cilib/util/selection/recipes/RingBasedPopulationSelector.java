/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.util.selection.recipes;

import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.util.selection.PartialSelection;
import net.cilib.util.selection.Selection;
import net.cilib.util.selection.arrangement.Arrangement;
import net.cilib.util.selection.arrangement.RingBasedArrangement;

/**
 */
public class RingBasedPopulationSelector implements Selector<SinglePopulationBasedAlgorithm> {

    private static final long serialVersionUID = 8899308548978334236L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<SinglePopulationBasedAlgorithm> on(Iterable<SinglePopulationBasedAlgorithm> iterable) {
        Arrangement ordering = new RingBasedArrangement(AbstractAlgorithm.get());
        return Selection.copyOf(iterable).orderBy(ordering);
    }
}
