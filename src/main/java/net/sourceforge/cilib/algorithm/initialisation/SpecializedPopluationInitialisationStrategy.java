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
 * Initialise a specialised collection of {@linkplain net.sourceforge.cilib.entity.Entity entity} objects.
 */
public class SpecializedPopluationInitialisationStrategy implements PopulationInitialisationStrategy<Entity> {
    private static final long serialVersionUID = -9146471282965793922L;
    private List<Entity> entityList;

    /**
     * Create an instance of {@code SpecializedPopluationInitialisationStrategy}.
     */
    public SpecializedPopluationInitialisationStrategy() {
        this.entityList = new ArrayList<Entity>(40);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public SpecializedPopluationInitialisationStrategy(SpecializedPopluationInitialisationStrategy copy) {
        this.entityList = new ArrayList<Entity>(copy.entityList.size());
        for (Entity entity : copy.entityList) {
            this.entityList.add(entity.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpecializedPopluationInitialisationStrategy getClone() {
        return new SpecializedPopluationInitialisationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity getEntityType() {
        // this needs to be looked at... generalisation breaks here
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Entity> initialise(OptimisationProblem problem) {
        if (problem == null)
            throw new InitialisationException("No problem has been specified");

        if (this.entityList.size() == 0)
            throw new InitialisationException("No prototype Entity object has been defined for the clone operation in the entity constrution process.");

        List<Entity> entities = new ArrayList<Entity>();
        for (Entity entity : entityList) {
            entity.initialise(problem);
            entities.add(entity);
        }

        return entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEntityType(Entity entity) {
        this.entityList.add(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEntityNumber() {
        return this.entityList.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
