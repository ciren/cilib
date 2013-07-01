/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.multiple.moo;

import net.cilib.algorithm.Algorithm;
import net.cilib.measurement.Measurement;
import net.cilib.moo.archive.Archive;
import net.cilib.problem.solution.Fitness;
import net.cilib.problem.solution.MOFitness;
import net.cilib.problem.solution.OptimisationSolution;
import net.cilib.type.types.container.TypeList;

/**
 * <p>
 * Determines the hv reference vector and prints it out.
 * The reference vector contains the worst values for each objective for the
 * specific iteration.
 *
 * This is used for the offline hypervolume calculations
 * </p>
 *
 */
public class HypervolumeRefVector implements Measurement {


    public HypervolumeRefVector() {
    }

    public HypervolumeRefVector (HypervolumeRefVector copy) {
    }

    @Override
    public HypervolumeRefVector getClone() {
        return new HypervolumeRefVector(this);
    }


    public String getDomain() {
        return "T";
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList refVec = new TypeList();

        Archive archive = Archive.Provider.get();

        int counter = 0;
        for (OptimisationSolution solution : archive) {
            MOFitness fitnesses = (MOFitness) solution.getFitness();
            if (counter == 0) {
                for (int k=0; k < fitnesses.getDimension(); k++)
                    refVec.add(fitnesses.getFitness(k));
                counter = 1;
            }
            else {
                TypeList temp = new TypeList();
                for (int kk=0; kk < fitnesses.getDimension(); kk++) {
                    if (fitnesses.getFitness(kk).getValue().doubleValue() > ((Fitness)refVec.get(kk)).getValue().doubleValue())
                        temp.add(fitnesses.getFitness(kk));
                    else temp.add(refVec.get(kk));
                }
                refVec.clear();
                refVec.addAll(temp);
            }
        }

        return refVec;
    }
}
