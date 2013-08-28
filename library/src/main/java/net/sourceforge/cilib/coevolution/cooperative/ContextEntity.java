/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative;

import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.behaviour.DoNothingBehaviour;
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
        put(Property.CANDIDATE_SOLUTION, Vector.of());
        put(Property.FITNESS, InferiorFitness.instance());
        behaviour = new DoNothingBehaviour();
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
     * Clear the context vector.
     */
    public void clear(){
        put(Property.CANDIDATE_SOLUTION, Vector.of());
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
            getPosition().set(allocation.getProblemIndex(i), solution.get(i).getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        put(Property.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        getPosition().randomise();
    }

    /**
     * Set the context vector to the given {@linkplain StructuredType}. The type
     * has to be a {@linkplain Vector}.
     */
    @Override
    public void setPosition(StructuredType type) {
        super.setPosition(type.getClone());
    }

    @Override
    public Vector getPosition() {
        return (Vector) super.getPosition();
    }

    /**
     * Set the {@linkplain Fitness} of this context entity.
     * @param f The new {@linkplain Fitness} value.
     */
    public void setFitness(Fitness f){
        put(Property.FITNESS, f);
    }

    @Override
    public void reinitialise() {
        throw new RuntimeException("This operation is not supported by a ContextEntity");
    }

}
