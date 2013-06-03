/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import fj.F;
import fj.Ord;
import fj.Ordering;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomInitialisationStrategy;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implements the Entity interface. Individual represents entities used within the EC paradigm.
 */
public class Individual extends AbstractEntity {

    protected static final long serialVersionUID = -578986147850240655L;
	public static Ord<Individual> ordering = Ord.ord(
			new F<Individual, F<Individual, Ordering>>() {
				@Override
				public F<Individual, Ordering> f(final Individual a) {
					return new F<Individual, Ordering>() {
						@Override
						public Ordering f(final Individual b) {
							int x = a.compareTo(b);
							return x < 0 ? Ordering.LT : x == 0 ? Ordering.EQ : Ordering.GT;
						}
					};
				}
			});
    protected InitialisationStrategy<Individual> initialisationStrategy;

    /**
     * Create an instance of {@linkplain Individual}.
     */
    public Individual() {
        put(Property.CANDIDATE_SOLUTION, Vector.of());
        put(Property.FITNESS, InferiorFitness.instance());
        initialisationStrategy = new RandomInitialisationStrategy<>();
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
     * Resets the fitness to <code>InferiorFitness</code>.
     */
    public void resetFitness() {
        put(Property.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        // ID initialisation is done in the clone method...
        // which is always enforced due to the semantics of the performInitialisation methods
        put(Property.CANDIDATE_SOLUTION, Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresentation()).build());

        this.initialisationStrategy.initialise(Property.CANDIDATE_SOLUTION, this);

        Vector strategy = Vector.fill(0.0, this.getPosition().size());

        put(Property.STRATEGY_PARAMETERS, strategy);
        put(Property.FITNESS, InferiorFitness.instance());
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
        str.append(getPosition().toString());
        str.append(get(Property.STRATEGY_PARAMETERS));
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
