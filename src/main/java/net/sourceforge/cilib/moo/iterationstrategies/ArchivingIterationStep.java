/**
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.moo.iterationstrategies;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.CompositeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.type.types.Type;

/**
 * <p>
 * This class is a generic Multi-objective optimisation class that is responsible for populating
 * the {@link Archive} of Pareto optimal solutions used during the search process of a Multi-
 * objective optimisation algorithm. {@code ArchivingIterationStep} is best used as sub-component
 * of a {@link CompositeIterationStrategy} where it can be combined with the normal iteration
 * strategies (like PSO's {@link SynchronousIterationStrategy} etc.)
 * </p>
 * 
 * @author Wiehann Matthysen
 * @param <E> The {@link PopulationBasedAlgorithm} that will have it's entities' positions added to
 * the archive as potential solutions.
 */
public class ArchivingIterationStep<E extends PopulationBasedAlgorithm> implements IterationStrategy<E> {

    private static final long serialVersionUID = 4029628616324259998L;

    public ArchivingIterationStep() {
    }

    public ArchivingIterationStep(ArchivingIterationStep<E> copy) {
    }

    @Override
    public ArchivingIterationStep<E> getClone() {
        return new ArchivingIterationStep<E>(this);
    }

    @Override
    public void performIteration(E algorithm) {
        updateArchive(algorithm.getTopology());
    }

    public void setArchive(Archive archive) {
        Archive.set(archive);
    }

    public Archive getArchive() {
        return Archive.get();
    }

    protected void updateArchive(Topology<? extends Entity> population) {
        Algorithm topLevelAlgorithm = (Algorithm) Algorithm.getAlgorithmList().get(0);
        List<OptimisationSolution> optimisationSolutions = new ArrayList<OptimisationSolution>();
        for (Entity entity : population) {
            Type solution = entity.getCandidateSolution().getClone();
            optimisationSolutions.add(new OptimisationSolution(solution,
                    topLevelAlgorithm.getOptimisationProblem().getFitness(solution, false)));
        }
        Archive.get().accept(optimisationSolutions);
    }
}