/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.HasNeighbourhood;
import net.sourceforge.cilib.algorithm.population.HasTopology;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.pso.particle.Particle;

public class RandomSentryDetectionStrategy extends EnvironmentChangeDetectionStrategy {
    private static final long serialVersionUID = 6254159986113630555L;

    private int sentries;
    private double theta;
    private int[] sentryIDs;
    private boolean initialised = false;
    ArrayList<Particle> sentryList;

    public RandomSentryDetectionStrategy() {
        sentries = 1;
        theta = 0.001;
    }

    public <A extends HasTopology & Algorithm> void Initialise(A algorithm){
        sentryIDs = new int[sentries];
        int populationSize = algorithm.getTopology().length();

        //randomly select the sentries among the particles
        for(int i=0; i<sentries; i++){
            sentryIDs[i] = Math.abs(Rand.nextInt()%populationSize);
            for(int j=0;j<i;j++){//doesn't pick the same entity twice
                if(sentryIDs[j]==sentryIDs[i]){
                    --i;
                    break;
                }
            }//for
        }//for
        Arrays.sort(sentryIDs);

        fj.data.List<? extends Entity> topology = algorithm.getTopology();
        this.sentryList = new ArrayList<Particle>();
        Iterator<? extends Entity> iterator = topology.iterator();

        int sentryCounter = 0;
        while (iterator.hasNext() && sentryCounter<sentries) {
            DynamicParticle current = (DynamicParticle) iterator.next();
            if(current.getId() == (new Integer(this.sentryIDs[sentryCounter]))){
                sentryList.add(current);
                ++sentryCounter;
            }//if
        }//while

        this.initialised = true;
    }

    public RandomSentryDetectionStrategy(RandomSentryDetectionStrategy copy) {
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
            this.Initialise(algorithm);
        }
        boolean envChangeOccured = false;

        for (Particle nextSentry : sentryList) {
            double oldSentryFitness = nextSentry.getFitness().getValue();
            double newSentryFitness = algorithm.getOptimisationProblem().getFitness(nextSentry.getCandidateSolution()).getValue();

            if(Math.abs(oldSentryFitness - newSentryFitness) >=  theta) {
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

