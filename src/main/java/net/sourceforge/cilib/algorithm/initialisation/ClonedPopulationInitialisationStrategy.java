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
package net.sourceforge.cilib.algorithm.initialisation;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * Create a collection of {@linkplain net.sourceforge.cilib.entity.Entity entities}
 * by cloning the given prototype {@link net.sourceforge.cilib.entity.Entity}.
 *
 * @param <E> The {@code Entity} type.
 * @author Gary Pampara
 */
public class ClonedPopulationInitialisationStrategy<E extends Entity> implements PopulationInitialisationStrategy<E> {
    private static final long serialVersionUID = -7354579791235878648L;
    private Entity prototypeEntity;
    private int entityNumber;

    /**
     * Create an instance of the {@code ClonedPopulationInitialisationStrategy}.
     */
    public ClonedPopulationInitialisationStrategy() {
        entityNumber = 20;
        prototypeEntity = null; // This has to be manually set as Individuals are used in GAs etc...
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public ClonedPopulationInitialisationStrategy(ClonedPopulationInitialisationStrategy copy) {
        this.entityNumber = copy.entityNumber;
        this.prototypeEntity = copy.prototypeEntity.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClonedPopulationInitialisationStrategy getClone() {
        return new ClonedPopulationInitialisationStrategy(this);
    }

    /**
     * Perform the required initialisation, using the provided <tt>Topology</tt> and
     * <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     * @throws InitialisationException if the initialisation cannot take place.
     */
    @Override
    public Iterable<E> initialise(OptimisationProblem problem) {
        if (problem == null)
            throw new InitialisationException("No problem has been specified");

        if (prototypeEntity == null)
            throw new InitialisationException("No prototype Entity object has been defined for the clone operation in the entity constrution process.");

        List<E> clones = new ArrayList<E>();

        for (int i = 0; i < entityNumber; ++i) {
            E entity = (E) prototypeEntity.getClone();

            entity.initialise(problem);
            clones.add(entity);
        }

        return clones;
    }

    /**
     * Set the prototype {@linkplain net.sourceforge.cilib.entity.Entity entity} for the copy process.
     * @param entityType The {@code Entity} to use for the cloning process.
     */
    @Override
    public void setEntityType(Entity entityType) {
        this.prototypeEntity = entityType;
    }

    /**
     * Get the {@linkplain net.sourceforge.cilib.entity.Entity entity} that has been defined as
     * the prototype to for the copies.
     * @see ClonedPopulationInitialisationStrategy#getPrototypeEntity()
     * @return The prototype {@code Entity}.
     */
    @Override
    public Entity getEntityType() {
        return this.prototypeEntity;
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return this.entityNumber;
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        this.entityNumber = entityNumber;
    }

}
