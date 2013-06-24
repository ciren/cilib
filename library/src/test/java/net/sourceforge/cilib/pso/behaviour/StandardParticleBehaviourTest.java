/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.behaviour;

import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StandardParticleBehaviourTest {

    @Test
    public void testPerformIteration() {

        Bounds bounds = new Bounds(-6.0, 6.0);
        Vector zero = Vector.of(0.0, 0.0);
        Vector velocity = Vector.of(2.1, 3.1);
        Vector position = Vector.of(Real.valueOf(5.2, bounds), Real.valueOf(6.2, bounds));

        StandardParticle particle = new StandardParticle();
        particle.getProperties().put(Property.VELOCITY, zero);
        particle.setPosition(zero);

        VelocityProvider vProvider = mock(VelocityProvider.class);
        when(vProvider.get(any(Particle.class))).thenReturn(velocity);

        PositionProvider pProvider = mock(PositionProvider.class);
        when(pProvider.get(any(Particle.class))).thenReturn(position);

        StandardParticleBehaviour behaviour = new StandardParticleBehaviour();
        behaviour.setVelocityProvider(vProvider);
        behaviour.setPositionProvider(pProvider);
        behaviour.setBoundaryConstraint(new ClampingBoundaryConstraint());
        
        behaviour.performIteration(particle);

        assertEquals(velocity, particle.getVelocity());
        assertEquals(5.2, ((Vector) particle.getPosition()).doubleValueOf(0), Maths.EPSILON);
        assertEquals(6.0, ((Vector) particle.getPosition()).doubleValueOf(1), Maths.EPSILON);
    }

}
