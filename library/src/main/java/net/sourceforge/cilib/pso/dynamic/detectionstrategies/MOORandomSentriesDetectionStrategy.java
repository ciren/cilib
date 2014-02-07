/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import fj.data.Java;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.HasNeighbourhood;
import net.sourceforge.cilib.algorithm.population.HasTopology;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;

/**
 * This class is similar to {@linkplain RandomSentriesDetectionStrategy}, but
 * uses MOFitness to compare sentries' current and previous values. This is
 * necessary for cases * where a higher-level algorithm handles the archive and
 * therefore the archive is not handled on the sub-algorithm level.
 *
 */
public class MOORandomSentriesDetectionStrategy extends EnvironmentChangeDetectionStrategy {

    private static final long serialVersionUID = 4572728741093545926L;
    
    protected ControlParameter numberOfSentries;
    
    /**
     * Creates a new instance of RandomMOOSentriesDetectionStrategy.
     */
    public MOORandomSentriesDetectionStrategy() {
        numberOfSentries = ConstantControlParameter.of(1.0);
    }

    /**
     * Creates a copy of the provided instance.
     * @param copy The instance that should be copied when creating the new
     * instance.
     */
    public MOORandomSentriesDetectionStrategy(MOORandomSentriesDetectionStrategy copy) {
        super(copy);
        numberOfSentries = copy.numberOfSentries.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MOORandomSentriesDetectionStrategy getClone() {
        return new MOORandomSentriesDetectionStrategy(this);
    }

    /**
     * After every {@link #interval} iteration, pick {@link #numberOfSentries a number of}
     * random entities from the given {@link Algorithm algorithm's} topology and
     * compare their previous fitness values with their current fitness values.
     * An environment change is detected when the difference between the
     * previous and current fitness values are &gt;= the specified {@link #epsilon}
     * value.
     * @param algorithm used to get hold of topology of entities and number of
     * iterations
     * @return true if a change has been detected, false otherwise
     */
    @Override
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
    	if ((AbstractAlgorithm.get().getIterations() % interval == 0) && (AbstractAlgorithm.get().getIterations() != 0)) {
            List all = Java.List_ArrayList().f(algorithm.getTopology());

            for (int i = 0; i < numberOfSentries.getParameter(); i++) {
                // select random sentry entity
                int random = Rand.nextInt(all.size());
                StandardParticle sentry = (StandardParticle) all.get(random);

                // check for change
                //double previousFitness = sentry.getFitness().getValue();

                boolean detectedChange = false;

                if (sentry.getFitness().getClass().getName().matches("MinimisationFitness")) {
                    Fitness previousFitness = sentry.getFitness();
                    sentry.updateFitness(sentry.getBehaviour().getFitnessCalculator().getFitness(sentry));
                    Fitness currentFitness = sentry.getFitness();

                    if (Math.abs(previousFitness.getValue() - currentFitness.getValue()) >= epsilon) {
                        detectedChange = true;

                    }
                }
                else if (sentry.getFitness().getClass().getName().matches("StandardMOFitness")) {
                        MOFitness previousFitness = (MOFitness)sentry.getFitness();
                        sentry.updateFitness(sentry.getBehaviour().getFitnessCalculator().getFitness(sentry));
                        MOFitness currentFitness = (MOFitness)sentry.getFitness();

                        for (int k=0; k < previousFitness.getDimension(); k++)
                            if (Math.abs(previousFitness.getFitness(k).getValue() -
                		currentFitness.getFitness(k).getValue()) >= epsilon) {
                		detectedChange = true;
                                break;
                            }
                }
                if (detectedChange) {
                    System.out.println("Detected a change");
                    return true;
                }
                
                // remove the selected element from the all list preventing it from being selected again
                all.remove(random);
            }
        }
        return false;
    }

    public void setNumberOfSentries(ControlParameter parameter) {
        if (parameter.getParameter() <= 0) {
            throw new IllegalArgumentException("It doesn't make sense to have <= 0 sentry points");
        }

        numberOfSentries = parameter;
    }

    public ControlParameter getNumberOfSentries() {
        return numberOfSentries;
    }
}
