/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.hs;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.type.types.container.SortedList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Harmony Search as published in K.S. Lee and Z.W. Geem, "A New Meta-Heuristic
 * Algorithm for Continuous Engineering Optimization: Harmony Search Theory and
 * Practice", Computer Methods in Applied Mechanics and Engineering, volume 194,
 * pages 3902--3933, 2005
 *
 */
public class HS extends AbstractAlgorithm implements SingularAlgorithm {

    private static final long serialVersionUID = 8019668923312811974L;
    private ProbabilityDistributionFunction uniform1;
    private ProbabilityDistributionFunction uniform2;
    private ProbabilityDistributionFunction uniform3;
    private ControlParameter harmonyMemorySize;
    private ControlParameter harmonyMemoryConsideringRate;
    private ControlParameter pitchAdjustingRate;
    private ControlParameter distanceBandwidth;
    private SortedList<Harmony> harmonyMemory;

    /**
     * Default constructor.
     * <p>
     * Set the parameters for the algorithm up as:
     * <ul>
     *   <li>Memory size: 20</li>
     *   <li>Memory considering rate: 0.9</li>
     *   <li>Pitch adjustment rate: 0.35</li>
     *   <li>Distance bandwidth: 0.5</li>
     * </ul>
     */
    public HS() {
        this.uniform1 = new UniformDistribution();
        this.uniform2 = new UniformDistribution();
        this.uniform3 = new UniformDistribution();

        this.harmonyMemorySize = ConstantControlParameter.of(20); //should be equal to number of individuals
        this.harmonyMemoryConsideringRate = ConstantControlParameter.of(0.9);
        this.pitchAdjustingRate = ConstantControlParameter.of(0.35);
        this.distanceBandwidth = ConstantControlParameter.of(0.5);

        this.harmonyMemory = new SortedList<Harmony>();
    }

    /**
     * Copy constructor.
     * @param copy The instance to copy.
     */
    public HS(HS copy) {
        this.uniform1 = copy.uniform1;
        this.uniform2 = copy.uniform2;
        this.uniform3 = copy.uniform3;

        this.harmonyMemorySize = copy.harmonyMemorySize.getClone();
        this.harmonyMemoryConsideringRate = copy.harmonyMemoryConsideringRate.getClone();
        this.pitchAdjustingRate = copy.pitchAdjustingRate.getClone();
        this.distanceBandwidth = copy.distanceBandwidth.getClone();

        this.harmonyMemory = copy.harmonyMemory.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HS getClone() {
        return new HS(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmInitialisation() {
        for (int i = 0; i < harmonyMemorySize.getParameter(); i++) {
            Harmony harmony = new Harmony();
            harmony.initialise(getOptimisationProblem());
            this.harmonyMemory.add(harmony);
        }
    }

    /**
     * Get the considering rate for the harmony memory.
     * @return The {@linkplain ControlParameter} for the considering rate.
     */
    public ControlParameter getHarmonyMemoryConsideringRate() {
        return harmonyMemoryConsideringRate;
    }

    /**
     * Set the considering rate for the harmony memory.
     * @param harmonyMemoryConsideringRate The {@linkplain ControlParameter} for the
     *        memory considering rate.
     */
    public void setHarmonyMemoryConsideringRate(ControlParameter harmonyMemoryConsideringRate) {
        this.harmonyMemoryConsideringRate = harmonyMemoryConsideringRate;
    }

    /**
     * Get the size of the harmony memory.
     * @return The size of the harmony memory.
     */
    public ControlParameter getHarmonyMemorySize() {
        return harmonyMemorySize;
    }

    /**
     * Set the size of the harmony memory.
     * @param harmonyMemorySize The memory size to use.
     */
    public void setHarmonyMemorySize(ControlParameter harmonyMemorySize) {
        this.harmonyMemorySize = harmonyMemorySize;
    }

    /**
     * Get the current pitch adjusting rate as a {@linkplain ControlParameter}.
     * @return The pitch adjusting rate as a {@linkplain ControlParameter}.
     */
    public ControlParameter getPitchAdjustingRate() {
        return pitchAdjustingRate;
    }

    /**
     * Set the pitch adjusting rate.
     * @param pitchAdjustingRate The {@linkplain ControlParameter} to use.
     */
    public void setPitchAdjustingRate(ControlParameter pitchAdjustingRate) {
        this.pitchAdjustingRate = pitchAdjustingRate;
    }

    /**
     * Get the distance bandwidth.
     * @return The {@linkplain ControlParameter} for the distance bandwidth.
     */
    public ControlParameter getDistanceBandwidth() {
        return distanceBandwidth;
    }

    /**
     * Set the distance bandwidth.
     * @param distanceBandwidth The {@linkplain ControlParameter} to use.
     */
    public void setDistanceBandwidth(ControlParameter distanceBandwidth) {
        this.distanceBandwidth = distanceBandwidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        //TO-DO: Make sure that all fitnesses are evaluated initially, and
        //that FE is incremented only once per iteration

        //calculate a new harmony
        Harmony newHarmony = new Harmony();
        newHarmony.initialise(getOptimisationProblem());
        Vector newHarmonyVector = (Vector) newHarmony.getCandidateSolution();

        Problem problem = getOptimisationProblem();
//        Real newHarmonyValue;
        for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
            if (uniform1.getRandomNumber() < harmonyMemoryConsideringRate.getParameter()) {
                Harmony selectedHarmony = this.harmonyMemory.get((int) uniform2.getRandomNumber(0, harmonyMemory.size() - 1));
                Vector selectedHarmonyContents = (Vector) selectedHarmony.getCandidateSolution();
                double newHarmonyValue = selectedHarmonyContents.doubleValueOf(i);
                Bounds bounds = selectedHarmonyContents.boundsOf(i);
                if (uniform1.getRandomNumber() < pitchAdjustingRate.getParameter()) {
                    double pitchedValue = newHarmonyValue + uniform3.getRandomNumber(-1, 1) * distanceBandwidth.getParameter();
                    if ((pitchedValue > bounds.getLowerBound()) && (pitchedValue < bounds.getUpperBound())) {
                        newHarmonyValue = pitchedValue;
                    }
                }

                newHarmonyVector.setReal(i, newHarmonyValue);
            } else {
                double upper = ((Vector) problem.getDomain().getBuiltRepresentation()).boundsOf(i).getUpperBound();
                double lower = ((Vector) problem.getDomain().getBuiltRepresentation()).boundsOf(i).getLowerBound();
                newHarmonyVector.setReal(i, uniform3.getRandomNumber(lower, upper));
            }
        }

        newHarmony.calculateFitness();
        harmonyMemory.add(newHarmony);
        harmonyMemory.remove(harmonyMemory.get(0)/*getFirst()*/); // Remove the worst harmony in the memory
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(this.harmonyMemory.get/*First()*/(0).getCandidateSolution(), this.harmonyMemory.get(0).getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }
}
