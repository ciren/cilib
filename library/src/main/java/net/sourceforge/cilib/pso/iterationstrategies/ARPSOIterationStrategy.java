/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.behaviour.StandardParticleBehaviour;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.single.diversity.Diversity;
import net.sourceforge.cilib.measurement.single.diversity.normalisation.DiagonalSpaceNormalisation;
import net.sourceforge.cilib.type.types.Real;

/**
 * Implementation of the attraction-repulsion (ARPSO) iteration strategy for PSO.
 * <p>
 * Reference:
 * <p>
 * R. Jacques, J.S. Vesterstr??m. "A diversity-guided particle swarm optimizer-
 * the ARPSO." Deptartment of Computer Science, University of Aarhus, Aarhus,
 * Denmark, Tech. Rep 2 (2002): 2002.
 */
public class ARPSOIterationStrategy extends AbstractIterationStrategy<PSO> {
    protected boolean attracting;
    protected IterationStrategy<PSO> delegate;
    protected Measurement<Real> diversityMeasure;
    protected ControlParameter minDiversity;
    protected ControlParameter maxDiversity;
    protected StandardParticleBehaviour attractionBehavior;
    protected StandardParticleBehaviour repulsionBehavior;

    public ARPSOIterationStrategy() {
        attracting = true;
        delegate = new SynchronousIterationStrategy();
        diversityMeasure = new Diversity();
        ((Diversity)diversityMeasure).setNormalisationParameter(
            new DiagonalSpaceNormalisation());
        minDiversity = ConstantControlParameter.of(5e-6);
        maxDiversity = ConstantControlParameter.of(0.25);

        attractionBehavior = new StandardParticleBehaviour();
        repulsionBehavior = new StandardParticleBehaviour();
        StandardVelocityProvider repulsionVelocity = new StandardVelocityProvider();
        repulsionVelocity.setCognitiveAcceleration(ConstantControlParameter.of(-1.496180));
        repulsionVelocity.setSocialAcceleration(ConstantControlParameter.of(-1.496180));
        repulsionBehavior.setVelocityProvider(repulsionVelocity);
    }

    public ARPSOIterationStrategy(ARPSOIterationStrategy copy) {
        attracting = copy.attracting;
        delegate = copy.delegate.getClone();
        diversityMeasure = copy.diversityMeasure.getClone();
        minDiversity = copy.minDiversity.getClone();
        maxDiversity = copy.maxDiversity.getClone();
        attractionBehavior = copy.attractionBehavior.getClone();
        repulsionBehavior = copy.repulsionBehavior.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ARPSOIterationStrategy getClone() {
        return new ARPSOIterationStrategy(this);
    }

    /**
     * Before performing the delegate's {@link #performIteration}, determine
     * which phase the algorithm is currently in (attraction/repulsion) by
     * examining the diversity of the swarm. If there is a phase change, update
     * the {@link VelocityProvider}s of each {@link Particle} accordingly.
     *
     * @param pso   the {@link PSO} to have an iteration applied.
     */
    @Override
    public void performIteration(PSO pso) {
        if (switchPhase(pso)) {
            StandardParticleBehaviour phaseBehavior = attracting ? attractionBehavior : repulsionBehavior;
        for (Particle current : pso.getTopology()) {
                current.setBehaviour(phaseBehavior);
            }
        }
        delegate.performIteration(pso);
    }

    /**
     * Switch the phase of the algorithm based on the current phase and the
     * diversity of the swarm.
     *
     * @param pso   the {@link PSO} on which the diversity measurement is
     *              applied
     * @return      true if a phase change occured, false otherwise
     */
    private boolean switchPhase(PSO pso) {
        double diversity = diversityMeasure.getValue(pso).doubleValue();
        if ((attracting && diversity < minDiversity.getParameter())
            || (!attracting && diversity > maxDiversity.getParameter())) {

            attracting = !attracting;
            return true;
        }
        return false;
    }

    /**
     * Set the {@linkplain ControlParameter} that controls the minimum diversity
     * at which the attraction phase will end.
     * @param minDiversity  the {@code minDiversity}
     *                      {@linkplain ControlParameter} to set
     */
    public void setMinDiversity(ControlParameter minDiversity) {
        this.minDiversity = minDiversity;
    }

    /**
     * Set the {@linkplain ControlParameter} that controls the maximum diversity
     * at which the repulsion phase will end.
     * @param maxDiversity  the {@code maxDiversity}
     *                      {@linkplain ControlParameter} to set
     */
    public void setMaxDiversity(ControlParameter maxDiversity) {
        this.maxDiversity = maxDiversity;
    }

    /**
     * Set the {@linkplain Measurement} that is used to determine the diversity
     * of the swarm.
     * @param diversityMeasure  the {@code diversityMeasure}
     *                          {@linkplain Measurement} to set
     */
    public void setDiversityMeasure(Measurement<Real> diversityMeasure) {
        this.diversityMeasure = diversityMeasure;
    }

    /**
     * Set the {@linkplain ParticleBehavior} that is ued during the attraction
     * phase.
     *
     * @param behavior  the {@code attraction} {@link ParticleBehavior}
     */
    public void setAttractionBehavior(StandardParticleBehaviour behavior) {
        this.attractionBehavior = behavior;
    }

    /**
     * Set the {@linkplain ParticleBehavior} that is ued during the repulsion
     * phase.
     *
     * @param behavior  the {@code repulsion} {@link ParticleBehavior}
     */
    public void setRepulsionBehavior(StandardParticleBehaviour behavior) {
        this.repulsionBehavior = behavior;
    }
}
