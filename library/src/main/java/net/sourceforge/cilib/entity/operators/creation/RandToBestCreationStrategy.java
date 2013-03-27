/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;

/**
 * This is an implementation of the Rand-to-best DE target creation strategy.
 * <p>
 * This implementation is simply an extension of the
 * {@link RandCreationStrategy} that also includes the best
 * {@link Entity}'s solution vector. The influence of the best vector and the
 * random vector is determined by the {@link #greedynessParameter}, which is
 * sampled as E [0,1]. A value of 0 will ignore the contribution of the best
 * {@link Entity}, and a value of 1 will ignore the contribution of the random
 * {@link Entity}.
 */
public class RandToBestCreationStrategy extends RandCreationStrategy {

    private static final long serialVersionUID = 413628791093573875L;
    private ControlParameter greedynessParameter;

    /**
     * Create a new instance of {@code RandToBestCreationStrategy}.
     */
    public RandToBestCreationStrategy() {
        greedynessParameter = ConstantControlParameter.of(0.5);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RandToBestCreationStrategy(RandToBestCreationStrategy copy) {
        super(copy);
        greedynessParameter = copy.greedynessParameter.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> T create(T targetEntity, T current, Topology<T> topology) {
        T bestEntity = Topologies.getBestEntity(topology);
        List<T> participants = Selection.copyOf(topology)
                .exclude(targetEntity, bestEntity, current)
                .orderBy(new RandomArrangement())
                .select(Samples.first((int) numberOfDifferenceVectors.getParameter()).unique());
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = ((Vector) targetEntity.getCandidateSolution()).multiply(1 - greedynessParameter.getParameter());
        Vector bestVector = ((Vector) bestEntity.getCandidateSolution()).multiply(greedynessParameter.getParameter());

        Vector trialVector = bestVector.plus(targetVector.plus(differenceVector.multiply(scaleParameter.getParameter())));

        T trialEntity = (T) current.getClone();
        trialEntity.setCandidateSolution(trialVector);

        return trialEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandToBestCreationStrategy getClone() {
        return new RandToBestCreationStrategy(this);
    }

    public void setGreedynessParameter(ControlParameter greedynessParameter) {
        this.greedynessParameter = greedynessParameter;
    }

    public ControlParameter getGreedynessParameter() {
        return greedynessParameter;
    }
}
