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
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.Problem;
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
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

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
    public void calculateFitness() {
        Fitness fitness = getFitnessCalculator().getFitness(this);
        this.getProperties().put(EntityType.FITNESS, fitness);
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
    public int getDimension() {
        return getCandidateSolution().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        StructuredType harmony = problem.getDomain().getBuiltRepresentation().getClone();
        harmony.randomize(new MersenneTwister());

        setCandidateSolution(harmony);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
//        TypeUtil.randomize(getCandidateSolution());
        throw new UnsupportedOperationException("Not implemetned yet.");
    }

}
