/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

import com.google.common.collect.Lists;

/**
 * A {@link VelocityProvider} which uses a DE strategy where the trial
 * vector is the bare bones attractor point.
 *
 *  TODO: To be published by Omran and Engelbrecht
 */
public class BareBonesDEVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -8781011210069055197L;
    private ProbabilityDistributionFunction rand1;
    private ProbabilityDistributionFunction rand2;
    private ProbabilityDistributionFunction rand3;
    private ControlParameter cognitive;
    private ControlParameter social;
    private ControlParameter crossoverProbability;
    
    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;

    /**
     * Create a new instance of the {@linkplain BareBonesDEVelocityProvider}.
     */
    public BareBonesDEVelocityProvider() {
        this.rand1 = new UniformDistribution();
        this.rand2 = new UniformDistribution();
        this.rand3 = new UniformDistribution();
        this.cognitive = ConstantControlParameter.of(1);
        this.social = ConstantControlParameter.of(1);
        this.crossoverProbability = ConstantControlParameter.of(0.5);
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public BareBonesDEVelocityProvider(BareBonesDEVelocityProvider copy) {
        this.rand1 = copy.rand1;
        this.rand2 = copy.rand2;
        this.rand3 = copy.rand3;
        this.cognitive = copy.cognitive.getClone();
        this.social = copy.social.getClone();
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BareBonesDEVelocityProvider getClone() {
        return new BareBonesDEVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector localGuide = (Vector) localGuideProvider.get(particle);
        Vector globalGuide = (Vector) globalGuideProvider.get(particle);

        PSO pso = (PSO) AbstractAlgorithm.get();
        fj.data.List<Entity> positions = getRandomParentEntities(pso.getTopology());

        //select three random individuals, all different and different from particle
        ProbabilityDistributionFunction pdf = new UniformDistribution();

        Vector position1 = (Vector) positions.index(0).getPosition();
        Vector position2 = (Vector) positions.index(1).getPosition();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double r = pdf.getRandomNumber(0, 1);
            double attractor = r * localGuide.doubleValueOf(i) + (1 - r) * globalGuide.doubleValueOf(i);
            double stepSize = this.rand3.getRandomNumber(0, 1) * (position1.doubleValueOf(i) - position2.doubleValueOf(i));

            if (this.rand2.getRandomNumber(0, 1) > this.crossoverProbability.getParameter()) {
                builder.add(attractor + stepSize);
            } else {
                builder.add(((Vector) particle.getPosition()).doubleValueOf(i));
            }
        }
        return builder.build();
    }

    /**
     * Get a list of individuals that are suitable to be used within
     * the recombination arithmetic operator.
     *
     * @param topology The list containing the entities.
     * @return A list of unique entities.
     */
    public static fj.data.List<Entity> getRandomParentEntities(fj.data.List<? extends Entity> topology) {
    	List<Entity> collection = Lists.newArrayList(topology.toCollection());
    	Collections.shuffle(collection);
        return fj.data.List.iterableList(collection.subList(0, 3));
    }

    /**
     * Get the first {@code RandomNumber}.
     * @return The first {@code RandomNumber}.
     */
    public ProbabilityDistributionFunction getRand1() {
        return this.rand1;
    }

    /**
     * Set the first {@code RandomNumber}.
     * @param rand1 The value to set.
     */
    public void setRand1(ProbabilityDistributionFunction rand1) {
        this.rand1 = rand1;
    }

    /**
     * Get the second{@code RandomNumber}.
     * @return The second {@code RandomNumber}.
     */
    public ProbabilityDistributionFunction getRand2() {
        return this.rand2;
    }

    /**
     * Set the second {@code RandomNumber}.
     * @param rand2 The value to set.
     */
    public void setRand2(ProbabilityDistributionFunction rand2) {
        this.rand2 = rand2;
    }

    /**
     * Get the third {@code RandomNumber}.
     * @return The third {@code RandomNumber}.
     */
    public ProbabilityDistributionFunction getRand3() {
        return this.rand3;
    }

    /**
     * Set the third {@code RandomNumber}.
     * @param rand3 The value to set.
     */
    public void setRand3(ProbabilityDistributionFunction rand3) {
        this.rand3 = rand3;
    }

    /**
     * Get the cognitive component.
     * @return The cognitive component.
     */
    public ControlParameter getCognitive() {
        return this.cognitive;
    }

    /**
     * Set the cognitive component.
     * @param cognitive The value to set.
     */
    public void setCognitive(ControlParameter cognitive) {
        this.cognitive = cognitive;
    }

    /**
     * Get the social component.
     * @return The social component.
     */
    public ControlParameter getSocial() {
        return this.social;
    }

    /**
     * Set the social component {@linkplain ControlParameter}.
     * @param social The value to set.
     */
    public void setSocial(ControlParameter social) {
        this.social = social;
    }

    /**
     * Get the cross-over probability.
     * @return The cross over probability {@linkplain ControlParameter}.
     */
    public ControlParameter getCrossoverProbability() {
        return this.crossoverProbability;
    }

    /**
     * Set the crossover probability.
     * @param crossoverProbability The value to set.
     */
    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's global guide.
     * @param globalGuideProvider The guide provider to set.
     */
    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's local guide.
     * @param localGuideProvider The guide provider to set.
     */
    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }
}
