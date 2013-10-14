/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import fj.P2;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.HasNeighbourhood;
import net.sourceforge.cilib.algorithm.population.HasTopology;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

public class RandomSentryDetectionStrategy extends EnvironmentChangeDetectionStrategy {
    private static final long serialVersionUID = 6254159986113630555L;

    private int sentries;
    private double theta;
    private boolean initialised = false;
    fj.data.List<Integer> sentryIndexes;

    public RandomSentryDetectionStrategy() {
        sentries = 1;
        theta = 0.001;
    }

    public <A extends HasTopology & Algorithm> void initialise(A algorithm){
        sentryIndexes = fj.data.List.iterableList(new RandomSelector<P2<Particle,Integer>>().on(algorithm.getTopology().zipIndex()).select(Samples.first(sentries))).map(P2.<Particle,Integer>__2());
        this.initialised = true;
    }

    public RandomSentryDetectionStrategy(RandomSentryDetectionStrategy copy) {
        super(copy);
        this.sentries = copy.sentries;
        this.theta = copy.theta;
    }

    public RandomSentryDetectionStrategy getClone() {
        return new RandomSentryDetectionStrategy(this);
    }

    /** Check for environment change:
     * Pick the specified number of random particles (sentries) and evaluate their current positions.
     * If the difference between the old fitness and the newly generated one is significant (exceeds a predefined theta)
     * for one or more of the sentry particles, assume that the environment has changed.
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true if any changes are detected, false otherwise
     */
    @Override
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
        if(initialised == false){
            this.initialise(algorithm);
        }
        boolean envChangeOccured = false;

        for (Integer nextSentryIndex : sentryIndexes) {
            Particle nextSentry = (Particle) algorithm.getTopology().index(nextSentryIndex);
            double oldSentryFitness = nextSentry.getFitness().getValue();
            double newSentryFitness = algorithm.getOptimisationProblem().getFitness(nextSentry.getPosition()).getValue();

            if (Math.abs(oldSentryFitness - newSentryFitness) >=  theta) {
                envChangeOccured = true;
                break;
            }
        }
        return envChangeOccured;
    }

    /**
     * @return the sentries
     */
    public int getSentries() {
        return sentries;
    }

    /**
     * @param sentries the sentries to set
     */
    public void setSentries(int sentries) {
        this.sentries = sentries;
    }

    /**
     * @return the theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @param theta the theta to set
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }
}

