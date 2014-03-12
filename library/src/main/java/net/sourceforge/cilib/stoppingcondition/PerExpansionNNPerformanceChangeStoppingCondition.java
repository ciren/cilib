/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.single.NeuronCount;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Performs a NNPerformanceChangeStoppingCondition, but only when an expansion
 * is detected in the NN architecture. When an expansion is detected, the global
 * best NN from the previous iteration is compared to the global best NN just
 * before the previous expansion.
 *
 * Please note: this stopping condition has a delay of one iteration in some
 * algorithms. User must manually remove the extra data produced in the output
 * files.
 */
public class PerExpansionNNPerformanceChangeStoppingCondition extends NNPerformanceChangeStoppingCondition {

    private ArrayList<Vector> resultsToRestore;
    private int previousNetworkSize;
    private boolean mustStop;

    public PerExpansionNNPerformanceChangeStoppingCondition() {
        resultsToRestore = null;
        previousNetworkSize = -1;
        mustStop = false;
    }

    public PerExpansionNNPerformanceChangeStoppingCondition(PerExpansionNNPerformanceChangeStoppingCondition rhs) {
        super(rhs);
        resultsToRestore = rhs.resultsToRestore;
        previousNetworkSize = rhs.previousNetworkSize;
        mustStop = rhs.mustStop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PerExpansionNNPerformanceChangeStoppingCondition getClone() {
        return new PerExpansionNNPerformanceChangeStoppingCondition(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean f(Algorithm algorithm) {
        int newNetworkSize = (new NeuronCount()).getValue(algorithm).intValue();

        //initialisation
        if (resultsToRestore == null) {
            previousNetworkSize = newNetworkSize;
            super.f(algorithm);
            resultsToRestore = previousResults;
            return false;
        }
        //expansion occurred
        else if (previousNetworkSize < newNetworkSize) {
            previousNetworkSize = newNetworkSize;
            resultsToRestore = previousResults;
            boolean resultToReturn = mustStop;
            mustStop = super.f(algorithm);
            return resultToReturn;
        }
        //no new expansion
        else {
            previousResults = resultsToRestore;
            mustStop = super.f(algorithm);
            return false;
        }
    }
}
