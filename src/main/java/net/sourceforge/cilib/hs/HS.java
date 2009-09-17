/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.hs;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.container.SortedList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Harmony;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Harmony Search as published in K.S. Lee and Z.W. Geem, "A New Meta-Heuristic
 * Algorithm for Continuous Engineering Optimization: Harmony Search Theory and
 * Practice", Computer Methods in Applied Mechanics and Engineering, volume 194,
 * pages 3902--3933, 2005
 *
 * @author Andries Engelbrecht
 */
public class HS extends AbstractAlgorithm implements SingularAlgorithm {
    private static final long serialVersionUID = 8019668923312811974L;
    private RandomNumber random1;
    private RandomNumber random2;
    private RandomNumber random3;
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
        this.random1 = new RandomNumber();
        this.random2 = new RandomNumber();
        this.random3 = new RandomNumber();

        this.harmonyMemorySize = new ConstantControlParameter(20); //should be equal to number of individuals
        this.harmonyMemoryConsideringRate = new ConstantControlParameter(0.9);
        this.pitchAdjustingRate = new ConstantControlParameter(0.35);
        this.distanceBandwidth = new ConstantControlParameter(0.5);

        this.harmonyMemory = new SortedList<Harmony>();
    }

    /**
     * Copy constructor.
     * @param copy The instance to copy.
     */
    public HS(HS copy) {
        this.random1 = copy.random1.getClone();
        this.random2 = copy.random2.getClone();
        this.random3 = copy.random3.getClone();

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
    public void performInitialisation() {
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

        OptimisationProblem problem = getOptimisationProblem();
//        Real newHarmonyValue;
        for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
            if (random1.getUniform() < harmonyMemoryConsideringRate.getParameter()) {
                Harmony selectedHarmony = this.harmonyMemory.get((int) random2.getUniform(0, harmonyMemory.size()-1));
                Vector selectedHarmonyContents = (Vector) selectedHarmony.getCandidateSolution();
                Real newHarmonyValue = (Real) selectedHarmonyContents.get(i).getClone();
                if (random1.getUniform() < pitchAdjustingRate.getParameter()) {
                    double pitchedValue = newHarmonyValue.getReal() + random3.getUniform(-1, 1) * distanceBandwidth.getParameter();
                    if ((pitchedValue > newHarmonyValue.getBounds().getLowerBound()) && (pitchedValue < newHarmonyValue.getBounds().getUpperBound()))
                        newHarmonyValue.setReal(pitchedValue);
                }

                newHarmonyVector.set(i, newHarmonyValue);
            }
            else {
                double upper = ((Vector) problem.getDomain().getBuiltRepresenation()).get(i).getBounds().getUpperBound();
                double lower = ((Vector) problem.getDomain().getBuiltRepresenation()).get(i).getBounds().getLowerBound();
                newHarmonyVector.set(i, new Real(random3.getUniform(lower, upper)));
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
