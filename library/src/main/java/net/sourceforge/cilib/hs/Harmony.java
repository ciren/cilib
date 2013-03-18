/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.hs;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Entity definition for a Harmony.
 */
public class Harmony extends AbstractEntity {
    private static final long serialVersionUID = -4941414797957384798L;

    public Harmony() {
    }

    public Harmony(Harmony copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Harmony getClone() {
        return new Harmony(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param object The object to compare.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        Harmony other = (Harmony) object;
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
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        StructuredType harmony = problem.getDomain().getBuiltRepresentation().getClone();
        harmony.randomise();

        setCandidateSolution(harmony);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
//        TypeUtil.randomise(getCandidateSolution());
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
