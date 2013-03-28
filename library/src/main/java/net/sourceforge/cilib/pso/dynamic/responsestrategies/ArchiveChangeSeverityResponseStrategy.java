/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.LinkedList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 * When a change occurs in the environment, a specified number of sentries are
 * selected from the archive. If these sentries' solutions varies more than a
 * specified threshold, the solutions from the archive within a specified radius
 * is removed from the archive. All solutions within the archive is also
 * re-evaluated.
 */
public class ArchiveChangeSeverityResponseStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> {

    private static final long serialVersionUID = 3044874503105791208L;
    protected ControlParameter numberOfSentries;
    protected double radiusFactor;

    /**
     * Creates a new instance of ArchiveChangeSeverityResponseStrategy.
     */
    public ArchiveChangeSeverityResponseStrategy() {
        this.numberOfSentries = ConstantControlParameter.of(10.0);
        this.radiusFactor = 1.5;
    }

    /**
     * Creates a copy of provided instance.
     * @param asrs Instance to copy
     */
    public ArchiveChangeSeverityResponseStrategy(ArchiveChangeSeverityResponseStrategy asrs) {
        super(asrs);
        this.numberOfSentries = asrs.numberOfSentries.getClone();
        this.radiusFactor = asrs.radiusFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> getClone() {
        return this;
    }

    /**
     * When a change occurs in the environment, a specified number of sentries
     * are selected from the archive. If these sentries' solutions varies less
     * than a specified threshold, the solutions from the archive within a
     * specified radius are removed from the archive. All solutions within the
     * archive are also re-evaluated.
     * @param algorithm The algorithm to perform the response on.
     */
    @Override
    protected void performReaction(PopulationBasedAlgorithm algorithm) {
        //check whether archive size is bigger than the number of sentries
        if (Archive.Provider.get().size() <= this.numberOfSentries.getParameter()) {
            this.setNumberOfSentries(ConstantControlParameter.of(Archive.Provider.get().size() / 2));
        }
        //old archive values
        List<OptimisationSolution> oldList = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution solution : Archive.Provider.get()) {
            oldList.add(solution);
        }

        //re-evaluating entities' fitness
        for (Entity entity : algorithm.getTopology()) {
            entity.getProperties().put(EntityType.Particle.BEST_FITNESS, entity.getFitnessCalculator().getFitness(entity));
            entity.calculateFitness();
        }

        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        Problem problem = populationBasedAlgorithm.getOptimisationProblem();

        //re-evaluating archive solutions
        List<OptimisationSolution> newList = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution solution : Archive.Provider.get()) {
            OptimisationSolution os = new OptimisationSolution(solution.getPosition(), problem.getFitness(solution.getPosition()));
            newList.add(os);
        }

        //average change in the objective space that the sentries experienced
        double avgChanges = 0.0;
        // select random sentry entities
        List<Double> changes = new LinkedList<Double>();
        List<Double> sentries = new LinkedList<Double>();

        for (int sentryCount = 0; sentryCount < this.numberOfSentries.getParameter(); sentryCount++) {
            int random = Rand.nextInt(Archive.Provider.get().size());
            sentries.add((double) random);
            OptimisationSolution solution1 = oldList.get(random);
            OptimisationSolution solution2 = newList.get(random);
            MOFitness fitness1 = (MOFitness) (solution1.getFitness());
            MOFitness fitness2 = (MOFitness) (solution2.getFitness());

            double change = 0.0;
            for (int i = 0; i < fitness1.getDimension(); i++) {
                change += Math.pow(fitness1.getFitness(i).getValue() - fitness2.getFitness(i).getValue(), 2);
            }
            changes.add(Math.sqrt(change));
            avgChanges += Math.sqrt(change);
        }
        avgChanges = avgChanges / (double) numberOfSentries.getParameter();

        List<OptimisationSolution> clearedSolutions = new LinkedList<OptimisationSolution>();
        //checking sentries' change and respond
        for (int k = 0; k < sentries.size(); k++) {
            double change = changes.get(k);
            if (change < this.radiusFactor * avgChanges * 0.5) {
                clearedSolutions.add(newList.get(k));
                //find closest sentry to calculate the radius
                double minSentryDistance = 0.0;
                for (int kk = 0; kk < sentries.size(); kk++) {
                    if (kk != k) {
                        OptimisationSolution sol = oldList.get(sentries.get(kk).intValue());
                        MOFitness sentry1 = (MOFitness) (sol.getFitness());
                        OptimisationSolution sol2 = oldList.get(sentries.get(kk).intValue());
                        MOFitness sentry2 = (MOFitness) (sol2.getFitness());

                        double distance = 0.0;
                        for (int j = 0; j < sentry1.getDimension(); j++) {
                            distance += Math.pow(sentry1.getFitness(j).getValue() - sentry2.getFitness(j).getValue(), 2);
                        }
                        distance = Math.sqrt(distance);

                        if (kk == 0) {
                            minSentryDistance = distance;
                        } else if (distance < minSentryDistance) {
                            minSentryDistance = distance;
                        }
                    } //end of if kk
                } //end for kk

                //remove solutions from Archive that is within the radius
                for (int i = 0; i < oldList.size(); i++) {
                    if (i != k) {
                        OptimisationSolution sol = oldList.get(i);
                        MOFitness sentry1 = (MOFitness) (sol.getFitness());
                        OptimisationSolution sol2 = oldList.get(sentries.get(k).intValue());
                        MOFitness sentry2 = (MOFitness) (sol2.getFitness());

                        double distance = 0.0;
                        for (int ii = 0; ii < sentry1.getDimension(); ii++) {
                            distance += Math.pow(sentry1.getFitness(ii).getValue() - sentry2.getFitness(ii).getValue(), 2);
                        }
                        distance = Math.sqrt(distance);

                        if (distance < (0.5 * minSentryDistance)) {
                            clearedSolutions.add(newList.get(i));
                        }
                    }
                }
            } //end if

        } //end for k

        newList.removeAll(clearedSolutions);
        Archive.Provider.get().clear();
        Archive.Provider.get().addAll(newList);
    }

    /**
     * Set the number of sentries that are selected from the archive
     * @param parameter Number of sentries
     */
    public void setNumberOfSentries(ControlParameter parameter) {
        if (parameter.getParameter() <= 0) {
            throw new IllegalArgumentException("It doesn't make sense to have <= 0 sentry points");
        }

        this.numberOfSentries = parameter;
    }

    /**
     * Returns the number of sentries
     * @return numberOfSentries The number of sentries that are used
     */
    public ControlParameter getNumberOfSentries() {
        return this.numberOfSentries;
    }

    /**
     * Set the radius factor used to calculate which sentries to remove
     * @param parameter radiusFactor The radius factor
     */
    public void setRadiusFactor(double parameter) {
        if (parameter <= 0) {
            throw new IllegalArgumentException("It doesn't make sense to have a radius factor <= 0");
        }

        this.radiusFactor = parameter;
    }

    /**
     * Returns the radius factor
     * @return radiusFactor The radius factor
     */
    public double getRadiusFactor() {
        return this.radiusFactor;
    }

}
