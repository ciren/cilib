/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

/**
 * The defined types for all properties within {@linkplain Entity} objects.
 */
public enum EntityType {
    CANDIDATE_SOLUTION,
    PREVIOUS_SOLUTION,
    FITNESS,
    PREVIOUS_FITNESS,
    STRATEGY_PARAMETERS;

    /**
     * {@linkplain Particle} specific constants.
     */
    public enum Particle {
        BEST_POSITION,
        BEST_FITNESS,
        VELOCITY;

        public enum Count {
            PBEST_STAGNATION_COUNTER;
        }
    }

    /**
     * Coevolution constants... This is probably going to be refactored to another location.
     * TODO: Check this
     */
    public enum Coevolution { // Not sure about this... has a funky smell to it.
        DISTANCE,
        BOARD,
        POPULATION_ID;
    }
}
