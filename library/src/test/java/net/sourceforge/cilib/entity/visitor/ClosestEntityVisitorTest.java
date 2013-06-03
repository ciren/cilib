/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests based on the usage of the ClosestEntityVisitor.
 */
public class ClosestEntityVisitorTest {

    /**
     * Determine the closest entity to the provided entity.
     */
    @Test
    public void closestEntity() {
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        Individual individual3 = new Individual();

        Vector vector1 = Vector.of(0.0, 0.0, 0.0, 0.0, 0.0);
        Vector vector2 = Vector.of(1.0, 1.0, 1.0, 1.0, 1.0);
        Vector vector3 = Vector.of(2.0, 2.0, 2.0, 2.0, 2.0);

        individual1.setPosition(vector1);
        individual2.setPosition(vector2);
        individual3.setPosition(vector3);

        fj.data.List<Individual> topology = fj.data.List.list(individual1, individual2, individual3);
        
        ClosestEntityVisitor<Individual> visitor = new ClosestEntityVisitor<>();
        visitor.setTargetEntity(individual1);
        Individual result = visitor.f(topology);
        
        Assert.assertTrue(individual2 == result);
    }

}
