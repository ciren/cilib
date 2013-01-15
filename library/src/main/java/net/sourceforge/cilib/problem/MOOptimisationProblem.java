/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.Fitnesses;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

public class MOOptimisationProblem extends ForwardingList<Problem> implements Problem {

    private static final long serialVersionUID = 4997914969290350571L;
    protected final List<Problem> problems;

    public MOOptimisationProblem() {
        this.problems = Lists.newArrayList();
    }

    public MOOptimisationProblem(MOOptimisationProblem copy) {
        this.problems = Lists.newArrayList();
        for (Problem optimisationProblem : copy.problems) {
            this.problems.add(optimisationProblem.getClone());
        }
    }

    @Override
    public MOOptimisationProblem getClone() {
        return new MOOptimisationProblem(this);
    }

    public MOFitness getFitness(Type[] solutions) {
        return Fitnesses.create(this, solutions);
    }

    @Override
    public MOFitness getFitness(Type solution) {
        return Fitnesses.create(this, solution);
    }

    public Fitness getFitness(int index, Type solution) {
        return this.problems.get(index).getFitness(solution);
    }

    @Override
    public int getFitnessEvaluations() {
        int sum = 0;
        for (Problem problem : this.problems) {
            sum += problem.getFitnessEvaluations();
        }
        return sum;
    }

    @Override
    public DomainRegistry getDomain() {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public void setDomain(String domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected List<Problem> delegate() {
        return this.problems;
    }
}
