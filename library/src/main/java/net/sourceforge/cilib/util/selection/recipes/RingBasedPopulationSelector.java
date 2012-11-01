/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.Arrangement;
import net.sourceforge.cilib.util.selection.arrangement.RingBasedArrangement;

/**
 */
public class RingBasedPopulationSelector implements Selector<PopulationBasedAlgorithm> {

    private static final long serialVersionUID = 8899308548978334236L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<PopulationBasedAlgorithm> on(Iterable<PopulationBasedAlgorithm> iterable) {
        Arrangement ordering = new RingBasedArrangement(AbstractAlgorithm.get());
        return Selection.copyOf(iterable).orderBy(ordering);
    }
}
