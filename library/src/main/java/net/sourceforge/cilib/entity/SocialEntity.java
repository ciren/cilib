/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.positionprovider.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * An Entity that can recieve information from other Entities.
 * </p>
 * <p>
 * Social infomation sharing is very important. This is a marker interface
 * that will identify Entity objects that are able to transfer knowledge to
 * other Entities.
 * </p>
 *
 */
public interface SocialEntity extends Cloneable {

     /**
     * Get the current social best fitness. This {@linkplain Fitness} value is dependent
     * on the current {@linkplain NeighbourhoodBestUpdateStrategy}.
     * @return The fitness based on the currently set {@linkplain NeighbourhoodBestUpdateStrategy}.
     */
    Fitness getSocialFitness();

    /**
     * Get the reference to the currently employed <code>NeighbourhoodBestUpdateStrategy</code>.
     * @return A reference to the current <code>NeighbourhoodBestUpdateStrategy</code> object
     */
    NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy();

    /**
     * Set the <code>NeighbourhoodBestUpdateStrategy</code> to be used by the {@linkplain Entity}.
     * @param neighbourhoodBestUpdateStrategy The <code>NeighbourhoodBestUpdateStrategy</code> to be used
     */
    void setNeighbourhoodBestUpdateStrategy(NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy);

}
