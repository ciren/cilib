/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;

/**
 * Initialise a specialised collection of {@link Entity}s.
 */
public class SpecialisedPopulationInitialisationStrategy implements PopulationInitialisationStrategy<Entity> {
    private static final long serialVersionUID = -9146471282965793922L;
    private List<Entity> entityList;

    /**
     * Create an instance of {@code SpecialisedPopulationInitialisationStrategy}.
     */
    public SpecialisedPopulationInitialisationStrategy() {
        this.entityList = new ArrayList<Entity>(40);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public SpecialisedPopulationInitialisationStrategy(SpecialisedPopulationInitialisationStrategy copy) {
        this.entityList = new ArrayList<Entity>(copy.entityList.size());
        for (Entity entity : copy.entityList) {
            this.entityList.add(entity.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SpecialisedPopulationInitialisationStrategy getClone() {
        return new SpecialisedPopulationInitialisationStrategy(this);
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
    public Iterable<Entity> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        Preconditions.checkState(!entityList.isEmpty(), "No entities have been defined!");

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
     * Add {@code n} amount of {@code Entity}s to the list of entities.
     *
     * @param entity    The type of {@code Entity} to add to the list.
     * @param n         The amount of entities to add.
     */
    public void setEntityTypes(Entity entity, int n) {
        for (int i = 0; i < n; i++) {
            this.entityList.add(entity.getClone());
        }
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
