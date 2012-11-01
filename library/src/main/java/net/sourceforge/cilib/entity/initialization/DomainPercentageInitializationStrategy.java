/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: this class should be refactored to use the RandomInitialVelocityStrategy or to be a compound
 * operation where the velocity is first randomised and then scaled by a percentage.
 * @param <E> The entity type.
 */
public class DomainPercentageInitializationStrategy<E extends Entity> implements
        InitializationStrategy<E> {

    private static final long serialVersionUID = -7178323673738508287L;
    private InitializationStrategy velocityInitialisationStrategy;
    private double percentage;

    public DomainPercentageInitializationStrategy() {
        this.velocityInitialisationStrategy = new RandomInitializationStrategy();
        this.percentage = 0.1;
    }

    public DomainPercentageInitializationStrategy(DomainPercentageInitializationStrategy copy) {
        this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.getClone();
        this.percentage = copy.percentage;
    }

    @Override
    public DomainPercentageInitializationStrategy getClone() {
        return new DomainPercentageInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, E entity) {
        this.velocityInitialisationStrategy.initialize(EntityType.Particle.VELOCITY, entity);
        Type type = entity.getProperties().get(key);
        Vector vector = (Vector) type;

        for (int i = 0; i < vector.size(); ++i) {
            vector.setReal(i, vector.doubleValueOf(i) * percentage);
        }
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public InitializationStrategy getVelocityInitialisationStrategy() {
        return velocityInitialisationStrategy;
    }

    public void setVelocityInitialisationStrategy(InitializationStrategy velocityInitialisationStrategy) {
        this.velocityInitialisationStrategy = velocityInitialisationStrategy;
    }
}
