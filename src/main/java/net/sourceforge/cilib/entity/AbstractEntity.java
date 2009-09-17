/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;

/**
 * Abstract class definition for all concrete {@linkplain Entity} objects.
 * This class defines the {@linkplain Entity} main data structure for the
 * values stored within the {@linkplain Entity} itself.
 */
public abstract class AbstractEntity implements Entity, CandidateSolution {
    private static final long serialVersionUID = 3104817182593047611L;

    private long id;
    private final CandidateSolution candidateSolution;
    private FitnessCalculator fitnessCalculator;

    /**
     * Initialise the candidate solution of the {@linkplain Entity}.
     */
    protected AbstractEntity() {
        this.id = EntityIdFactory.getNextId();

        this.candidateSolution = new CandidateSolutionMixin();
        this.fitnessCalculator = new EntityBasedFitnessCalculator();
    }

    /**
     * Copy constructor. Instantiate and copy the given instance.
     * @param copy The instance to copy.
     */
    protected AbstractEntity(AbstractEntity copy) {
        this.id = EntityIdFactory.getNextId();

        this.candidateSolution = (CandidateSolution) copy.candidateSolution.getClone();
        this.fitnessCalculator = copy.fitnessCalculator.getClone();
    }

    /**
     * {@inheritDoc}
     *
     * It doesn;t make sense to compare the meta data of the entity.
     * In other words, the properties of the entity may vary, but the entity
     * is still the same entity.
     *
     * @param object The object to compare equality.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

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
        return this.candidateSolution.getProperties();
    }

    /**
     * Set the properties for the current <code>Entity</code>.
     * @param properties The {@linkplain Blackboard} containing the new properties.
     */
    @Override
    public final void setProperties(Blackboard<Enum<?>, Type> properties) {
        this.candidateSolution.setProperties(properties);
    }

    /**
     * Get the value of the {@linkplain CandidateSolution} maintained by this
     * {@linkplain Entity}.
     * @return The candidate solution as a {@linkplain Type}.
     */
    @Override
    public StructuredType getCandidateSolution() {
        return this.candidateSolution.getCandidateSolution();
    }

    /**
     * Get the fitness of the {@linkplain CandidateSolution} maintained by this
     * {@linkplain Entity}.
     * @return The {@linkplain Fitness} of the candidate solution.
     */
    @Override
    public Fitness getFitness() {
        return this.candidateSolution.getFitness();
    }

    /**
     * Set the {@linkplain Type} maintained by this {@linkplain Entity}s
     * {@linkplain CandidateSolution}.
     * @param candidateSolution The {@linkplain Type} that will be the new value of the
     *        {@linkplain Entity} {@linkplain CandidateSolution}.
     */
    @Override
    public void setCandidateSolution(StructuredType candidateSolution) {
        this.candidateSolution.setCandidateSolution(candidateSolution);
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
    public FitnessCalculator getFitnessCalculator() {
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

}
