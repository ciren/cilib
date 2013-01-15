/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import java.util.concurrent.atomic.AtomicInteger;
import net.sourceforge.cilib.problem.objective.Minimise;
import net.sourceforge.cilib.problem.objective.Objective;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * <p>
 * This is a convenience class that keeps track of the number of fitness evaluations. This class can
 * be extend instead of implementing {@link net.sourceforge.cilib.problem.OptimisationProblem} directly.
 * </p>
 * <p>
 * The contract of returning an instance of {@link net.sourceforge.cilib.problem.InferiorFitness} for
 * solutions outside the problem search space is implemented by {@link #getFitness(Type, boolean)}
 * </p>
 */
public abstract class AbstractProblem implements Problem {

    private static final long serialVersionUID = -5008516277429476778L;

    protected AtomicInteger fitnessEvaluations;
    protected DomainRegistry domainRegistry;
    protected Objective objective;

    protected AbstractProblem() {
        this.fitnessEvaluations = new AtomicInteger(0);
        this.domainRegistry = new StringBasedDomainRegistry();
        this.objective = new Minimise();
    }

    protected AbstractProblem(AbstractProblem copy) {
        this.fitnessEvaluations = new AtomicInteger(copy.fitnessEvaluations.get());
        this.domainRegistry = copy.domainRegistry.getClone();
        this.objective = copy.objective;
    }

    @Override
    public abstract AbstractProblem getClone();

    /**
     * Determine the {@code Fitness} of the current {@link Problem} instance
     * based on the provided {@code solution}.
     * @param solution The {@link net.sourceforge.cilib.type.types.Type} representing the candidate solution.
     * @return The {@link Fitness} of the {@code solution} in the current {@linkplain Problem}.
     * @see OptimisationProblemAdapter#getFitness(Type, boolean)
     */
    protected abstract Fitness calculateFitness(Type solution);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Fitness getFitness(Type solution) {
        fitnessEvaluations.incrementAndGet();

        return calculateFitness(solution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getFitnessEvaluations() {
        return fitnessEvaluations.get();
    }

    @Override
    public DomainRegistry getDomain() {
        if (domainRegistry.getDomainString() == null) {
            throw new IllegalStateException("Domain has not been defined. Please define domain for function optimisation.");
        }
        return domainRegistry;
    }

    @Override
    public void setDomain(String domain) {
        this.domainRegistry.setDomainString(domain);
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
    }
}
