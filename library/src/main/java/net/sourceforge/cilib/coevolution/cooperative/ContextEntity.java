/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;

/**
 * An entity that maintains the context vector for a {@linkplain CooperativeCoevolutionAlgorithm}.
 *
 */
public class ContextEntity implements Entity {
    private static final long serialVersionUID = -3580129615323553890L;
    private FitnessCalculator<Entity> fitnessCalculator;
    private Fitness fitness;
    private Vector context;

    /**
     * Constructor
     *
     */
    public ContextEntity(){
        fitnessCalculator = new EntityBasedFitnessCalculator();
        fitness = InferiorFitness.instance();
        context = Vector.of();
    }

    /**
     * Copy constructor
     * @param other {@linkplain ContextEntity} to make a copy of
     */
    public ContextEntity(ContextEntity other){
        fitnessCalculator = other.fitnessCalculator.getClone();
        fitness = other.fitness.getClone();
        context = Vector.copyOf(other.context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        fitness = fitnessCalculator.getFitness(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Entity o) {
        return getFitness().compareTo(o.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getBestFitness() {
        return fitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getCandidateSolution() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextEntity getClone() {
        return new ContextEntity(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return context.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getFitness() {
        return fitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FitnessCalculator getFitnessCalculator() {
        return fitnessCalculator;
    }

    /**
     * Clear the context vector
     */
    public void clear(){
        context = Vector.of();
    }

    /**
     * Append the given {@linkplain Type} to the current context {@linkplain Vector}
     * @param value The value to append, it can either be a {@linkplain Numeric} type, or a {@linkplain Vector} of values.
     */
    public void append(Type value) {
        if(value instanceof Vector) {
            context = Vector.newBuilder().copyOf(context).copyOf((Vector) value.getClone()).build();
        } else {
            context = Vector.newBuilder().copyOf(context).add((Numeric) value.getClone()).build();
        }
    }

    /**
     * Append the candidate solution of the given {@linkplain Entity} to the current context {@linkplain Vector}
     * @param entity The entity who's candidate solution should be appended.
     */
    public void append(Entity entity) {
        append(entity.getCandidateSolution());
    }

    /**
     * Copy the given solution {@linkplain Vector} into a specified position into this context entity's context {@linkplain Vector}.
     * The given {@linkplain DimensionAllocation} dictates which dimensions the given {@linkplain Vector} should be copied into.
     * @param solution The {@linkplain Vector} to copy from.
     * @param allocation The {@linkplain DimensionAllocation} which describes which dimensions to copy the solution into.
     */
    public void copyFrom(Vector solution, DimensionAllocation allocation){
        if(solution.size() != allocation.getSize())
            throw new RuntimeException("Incompatible");

        for(int i = 0; i < allocation.getSize(); ++i){
            context.set(allocation.getProblemIndex(i), solution.get(i).getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        context = (Vector) problem.getDomain().getBuiltRepresentation().getClone();
    }

    /**
     * Set the context vector to the given {@linkplain StructuredType}. The type has to be
     * of type {@linkplain Vector}.
     */
    @Override
    public void setCandidateSolution(StructuredType type) {
        context = (Vector)type.getClone();
    }

    /**
     * Set the fitness of this context entity.
     * @param f The new {@linkplain Fitness} value.
     */
    public void setFitness(Fitness f){
        fitness = f;
    }

    @Override
    public void reinitialise() {
        throw new RuntimeException("This operation is not supported by a ContextEntity");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blackboard<Enum<?>, Type> getProperties() {
        throw new RuntimeException("This operation is not supported by a ContextEntity");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getId() {
        throw new RuntimeException("This operation is not supported by a ContextEntity");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        ContextEntity other = (ContextEntity) object;
        return super.equals(other) &&
            (this.context.equals(other.context)) &&
            (this.fitness.equals(other.fitness));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + (this.context == null ? 0 : this.context.hashCode());
        hash = 31 * hash + (this.fitness == null ? 0 : this.fitness.hashCode());
        return hash;
    }

    /**
     * Change the {@linkplain FitnessCalculator} of this context entity.
     * @param fitnessCalculator The new {@linkplain FitnessCalculator}.
     */
    public void setFitnessCalculator(FitnessCalculator<Entity> fitnessCalculator) {
        this.fitnessCalculator = fitnessCalculator;
    }
}
