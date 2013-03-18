/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomInitialisationStrategy;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implements the Entity interface. Individual represents entities used within the EC paradigm.
 */
public class Individual extends AbstractEntity {

    protected static final long serialVersionUID = -578986147850240655L;
    protected InitialisationStrategy<Individual> initialisationStrategy;

    /**
     * Create an instance of {@linkplain Individual}.
     */
    public Individual() {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of());
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        initialisationStrategy = new RandomInitialisationStrategy<Individual>();
    }

    /**
     * Copy constructor. Creates a copy of the given {@linkplain Individual}.
     * @param copy The {@linkplain Individual} to copy.
     */
    public Individual(Individual copy) {
        super(copy);
        initialisationStrategy = copy.initialisationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Individual getClone() {
        return new Individual(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        Individual other = (Individual) object;
        return super.equals(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        return hash;
    }

    /**
     * Resets the fitness to <code>InferiorFitness</code>.
     */
    public void resetFitness() {
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        // ID initialisation is done in the clone method...
        // which is always enforced due to the semantics of the performInitialisation methods
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresentation()).build());

        this.initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);

        Vector strategy = Vector.fill(0.0, this.getCandidateSolution().size());

        this.getProperties().put(EntityType.STRATEGY_PARAMETERS, strategy);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * Create a textual representation of the current {@linkplain Individual}. The
     * returned {@linkplain String} will contain both the genotypes and phenotypes for
     * the current {@linkplain Individual}.
     * @return The textual representation of this {@linkplain Individual}.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getCandidateSolution().toString());
        str.append(getProperties().get(EntityType.STRATEGY_PARAMETERS));
        return str.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        throw new UnsupportedOperationException("Implementation is required for this method");
    }

    public InitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    public void setInitialisationStrategy(InitialisationStrategy strategy) {
        initialisationStrategy = strategy;
    }
}
