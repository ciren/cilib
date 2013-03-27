/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.moo.archive.Archive;

/**
 * This strategy responds to a change by removing all solutions from the archive.
 * This may be required when dealing with dynamic MOO functions where the POF
 * changes from concave to convex or from convex to concave.
 */
public class ArchiveAlwaysClearingResponseStrategy<E extends PopulationBasedAlgorithm> extends
        EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm>{

    private static final long serialVersionUID = -3042868407937040175L;

    /**
     * {@inheritDoc}
     */
    @Override
    public EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> getClone() {
        return this;
    }

    /**
     * Responds to a change by removing all solutions from the
     * {@linkplain Archive}.
     *
     * @param algorithm The {@linkplain PopulationBasedAlgorithm} to perform
     *                  the response on.
     */
    @Override
    protected void performReaction(PopulationBasedAlgorithm algorithm) {
        //clearing all solutions from the archive
    	Archive.Provider.get().clear();
    }
}

