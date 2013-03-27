/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An entity that maintains the context vector for a {@link CooperativeCoevolutionAlgorithm}.
 */
public class ContextEntity extends AbstractEntity {

    private static final long serialVersionUID = -3580129615323553890L;

    /**
     * Constructor
     */
    public ContextEntity(){
        getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of());
        getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * Copy constructor
     * @param other {@linkplain ContextEntity} to make a copy of.
     */
    public ContextEntity(ContextEntity other){
        super(other);
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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        ContextEntity other = (ContextEntity) object;
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
     * Clear the context vector.
     */
    public void clear(){
        getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of());
    }

    /**
     * Copy the given solution {@linkplain Vector} into a specified position
     * into this context entity's context {@linkplain Vector}. The given
     * {@linkplain DimensionAllocation} dictates which dimensions the given
     * {@linkplain Vector} should be copied into.
     *
     * @param solution The {@linkplain Vector} to copy from.
     * @param allocation The {@linkplain DimensionAllocation} which describes
     *                   which dimensions to copy the solution into.
     */
    public void copyFrom(Vector solution, DimensionAllocation allocation){
        if(solution.size() != allocation.getSize()) {
            throw new RuntimeException("Incompatible");
        }

        for(int i = 0; i < allocation.getSize(); ++i){
            ((Vector)getCandidateSolution()).set(allocation.getProblemIndex(i), solution.get(i).getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        getCandidateSolution().randomise();
    }

    /**
     * Set the context vector to the given {@linkplain StructuredType}. The type
     * has to be a {@linkplain Vector}.
     */
    @Override
    public void setCandidateSolution(StructuredType type) {
        super.setCandidateSolution(type.getClone());
    }

    @Override
    public Vector getCandidateSolution() {
        return (Vector) super.getCandidateSolution();
    }

    /**
     * Set the {@linkplain Fitness} of this context entity.
     * @param f The new {@linkplain Fitness} value.
     */
    public void setFitness(Fitness f){
        getProperties().put(EntityType.FITNESS, f);
    }

    @Override
    public void reinitialise() {
        throw new RuntimeException("This operation is not supported by a ContextEntity");
    }

}
