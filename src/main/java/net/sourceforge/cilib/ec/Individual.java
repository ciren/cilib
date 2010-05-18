/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Resetable;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author otter
 * Implements the Entity interface. Individual represents entities used within the EC paradigm.
 */
public class Individual extends AbstractEntity {
    private static final long serialVersionUID = -578986147850240655L;

    /**
     * Create an instance of {@linkplain Individual}.
     */
    public Individual() {
        setCandidateSolution(new Vector());
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * Copy constructor. Creates a copy of the given {@linkplain Individual}.
     * @param copy The {@linkplain Individual} to copy.
     */
    public Individual(Individual copy) {
        super(copy);
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
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

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
     public void initialise(OptimisationProblem problem) {
         // ID initialization is done in the clone method...
         // which is always enforced due to the semantics of the performInitialisation methods
         MersenneTwister random = new MersenneTwister();

         this.setCandidateSolution(problem.getDomain().getBuiltRepresenation().getClone());
         this.getCandidateSolution().randomize(random);

         this.getProperties().put(EntityType.STRATEGY_PARAMETERS, getCandidateSolution().getClone());
         ((Resetable) this.getProperties().get(EntityType.STRATEGY_PARAMETERS)).reset();

         this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
     }

     /**
      * {@inheritDoc}
      */
     @Override
     public int compareTo(Entity o) {
         return this.getFitness().compareTo(o.getFitness());
     }

     /**
      * {@inheritDoc}
      */
     @Override
     public void setCandidateSolution(StructuredType type) {
         super.setCandidateSolution(type);
     }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        this.getProperties().put(EntityType.FITNESS, this.getFitnessCalculator().getFitness(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return getCandidateSolution().size();
    }

    /**
     * Create a textual representation of the current {@linkplain Individual}. The
     * returned {@linkplain String} will contain both the genotypes and penotypes for
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
}
