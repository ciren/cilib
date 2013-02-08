/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;

/**
 * Abstract class definition for all concrete {@linkplain Entity} objects.
 * This class defines the {@linkplain Entity} main data structure for the
 * values stored within the {@linkplain Entity} itself.
 */
public abstract class AbstractEntity implements Entity {

    private static final long serialVersionUID = 3104817182593047611L;

    private long id;
    private final Blackboard<Enum<?>, Type> properties;
    private FitnessCalculator<Entity> fitnessCalculator;

    /**
     * Initialise the candidate solution of the {@linkplain Entity}.
     */
    protected AbstractEntity() {
        this.id = EntityIdFactory.getNextId();
        this.properties = new Blackboard();
        this.fitnessCalculator = new EntityBasedFitnessCalculator();
    }

    /**
     * Copy constructor. Instantiate and copy the given instance.
     * @param copy The instance to copy.
     */
    protected AbstractEntity(AbstractEntity copy) {
        this.id = EntityIdFactory.getNextId();
        this.properties = copy.properties.getClone();
        this.fitnessCalculator = copy.fitnessCalculator.getClone();
    }

    /**
     * {@inheritDoc}
     *
     * It doesn't make sense to compare the meta data of the entity.
     * In other words, the properties of the entity may vary, but the entity
     * is still the same entity.
     *
     * @param object The object to compare equality.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        AbstractEntity other = (AbstractEntity) object;
        return this.id == other.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int)(id ^ (id >>> 32));
        return hash;
    }

    /**
     * Get the properties associate with the <code>Entity</code>.
     * @return The properties within a {@linkplain Blackboard}.
     */
    @Override
    public final Blackboard<Enum<?>, Type> getProperties() {
        return properties;
    }

    /**
     * Get the value of the candidate solution maintained by this
     * {@linkplain Entity}.
     * @return The candidate solution as a {@linkplain Type}.
     */
    @Override
    public StructuredType getCandidateSolution() {
        return (StructuredType) properties.get(EntityType.CANDIDATE_SOLUTION);
    }

    /**
     * Get the fitness of the candidate solution maintained by this
     * {@linkplain Entity}.
     * @return The {@linkplain Fitness} of the candidate solution.
     */
    @Override
    public Fitness getFitness() {
        return (Fitness) properties.get(EntityType.FITNESS);
    }

    /**
     * Set the {@linkplain Type} maintained by this {@linkplain Entity}s
     * candidate solution
     * @param candidateSolution The {@linkplain Type} that will be the new value of the
     *        {@linkplain Entity} candidate solution.
     */
    @Override
    public void setCandidateSolution(StructuredType candidateSolution) {
        properties.put(EntityType.CANDIDATE_SOLUTION, candidateSolution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getBestFitness() {
        return getFitness();
    }

    /**
     * Get the current {@code FitnessCalculator} for the current {@code Entity}.
     * @return The {@code FitnessCalculator} associated with this {@code Entity}.
     */
    @Override
    public FitnessCalculator<Entity> getFitnessCalculator() {
        return fitnessCalculator;
    }

    /**
     * Set the {@code FitnessCalculator} for the current {@code Entity}.
     * @param fitnessCalculator The value to set.
     */
    public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
        this.fitnessCalculator = fitnessCalculator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public abstract AbstractEntity getClone();

    @Override
    public void calculateFitness() {
        properties.put(EntityType.PREVIOUS_FITNESS, getFitness().getClone());
        properties.put(EntityType.FITNESS, fitnessCalculator.getFitness(this));
    }

    @Override
    public abstract void initialise(Problem problem);

    @Override
    public int getDimension() {
        return getCandidateSolution().size();
    }

    @Override
    public abstract void reinitialise();

    @Override
    public int compareTo(Entity o) {
        return getFitness().compareTo(o.getFitness());
    }

}
