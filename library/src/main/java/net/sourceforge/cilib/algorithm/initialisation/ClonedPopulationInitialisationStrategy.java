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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;

/**
 * Create a collection of {@linkplain net.sourceforge.cilib.entity.Entity entities}
 * by cloning the given prototype {@link net.sourceforge.cilib.entity.Entity}.
 *
 * @param <E> The {@code Entity} type.
 */
public class ClonedPopulationInitialisationStrategy<E extends Entity> implements PopulationInitialisationStrategy<E> {

    private static final long serialVersionUID = -7354579791235878648L;
    private Entity prototypeEntity;
    private ControlParameter entityNumber;

    /**
     * Create an instance of the {@code ClonedPopulationInitialisationStrategy}.
     */
    public ClonedPopulationInitialisationStrategy() {
        entityNumber = ConstantControlParameter.of(20);
        prototypeEntity = null; // This has to be manually set as Individuals are used in GAs etc...
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public ClonedPopulationInitialisationStrategy(ClonedPopulationInitialisationStrategy copy) {
        this.entityNumber = copy.entityNumber.getClone();

        if (copy.prototypeEntity != null) {
            this.prototypeEntity = copy.prototypeEntity.getClone();
        }
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
    public Iterable<E> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        Preconditions.checkNotNull(prototypeEntity, "No prototype Entity object has been defined for the clone operation in the entity construction process.");

        List<E> clones = new ArrayList<E>();

        for (int i = 0; i < entityNumber.getParameter(); ++i) {
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
     * Gets the {@link Entity} that has been defined as to copy.
     *
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
        return (int) this.entityNumber.getParameter();
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        this.entityNumber = ConstantControlParameter.of(entityNumber);
    }

    public void setEntityNumberControlParameter(ControlParameter entityNumber) {
        this.entityNumber = entityNumber;
    }
}
