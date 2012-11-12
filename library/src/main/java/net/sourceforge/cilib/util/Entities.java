/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Utility methods for Entities.
 */
public class Entities {
    /**
     * Gets the candidate solutions from a list of entities.
     * @param entities The list of entities.
     * @return The list of candidate solutions.
     */
    public static <T extends StructuredType> List<T> getCandidateSolutions(List<? extends Entity> entities) {
        List<T> solutions = new ArrayList<T>();
        
        for (Entity e : entities) {
            solutions.add((T) e.getCandidateSolution());
        }
        
        return solutions;
    }
}
