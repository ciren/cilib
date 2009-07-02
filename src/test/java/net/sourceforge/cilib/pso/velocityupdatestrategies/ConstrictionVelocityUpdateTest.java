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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for the constriction velocity update.
 * @author andrich
 */
public class ConstrictionVelocityUpdateTest {

    /**
     * Test cloning and implicetly the copy constructor.
     */
    @Test
    public void testClone() {
        ConstrictionVelocityUpdate original = new ConstrictionVelocityUpdate();
        ConstrictionVelocityUpdate copy = (ConstrictionVelocityUpdate) original.getClone();
        
        Assert.assertEquals(original.getKappa().getParameter(), copy.getKappa().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getVMax().getParameter(), copy.getVMax().getParameter(), Maths.EPSILON);
        Assert.assertEquals(((RandomizingControlParameter)original.cognitiveAcceleration).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.cognitiveAcceleration).getControlParameter().getParameter(), Maths.EPSILON);
        Assert.assertEquals(((RandomizingControlParameter)original.socialAcceleration).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.socialAcceleration).getControlParameter().getParameter(), Maths.EPSILON);

        copy.setKappa(new ConstantControlParameter(0.7));
        copy.setVMax(new ConstantControlParameter(0.7));
        RandomizingControlParameter randomizingControlParameter = new RandomizingControlParameter();
        randomizingControlParameter.setParameter(4.0);
        copy.setSocialAcceleration(new RandomizingControlParameter(randomizingControlParameter.getClone()));
        copy.setCognitiveAcceleration(new RandomizingControlParameter(randomizingControlParameter.getClone()));

        Assert.assertFalse(Double.compare(original.getKappa().getParameter(), copy.getKappa().getParameter()) == 0);
        Assert.assertFalse(Double.compare(original.getVMax().getParameter(), copy.getVMax().getParameter()) == 0);
        Assert.assertFalse(Double.compare(((RandomizingControlParameter)original.cognitiveAcceleration).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.cognitiveAcceleration).getControlParameter().getParameter()) == 0);
        Assert.assertFalse(Double.compare(((RandomizingControlParameter)original.socialAcceleration).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.socialAcceleration).getControlParameter().getParameter()) == 0);
    }

    /**
     * Test the velocity update as well as the constraint assertion.
     */
    @Test
    public void testUpdateAndConstraintAssertion() {
        MersenneTwister twister = new MersenneTwister();
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setFunction(new Spherical());

        StandardParticle nBest = new StandardParticle();
        StandardParticle particle = new StandardParticle();
        particle.initialise(problem);
        nBest.initialise(problem);
        ((Vector)particle.getVelocity()).randomize(twister);
        particle.setNeighbourhoodBest(nBest);

        ConstrictionVelocityUpdate constrictionVelocityUpdate = new ConstrictionVelocityUpdate();

        boolean assertionExceptionOccured = false;
        try {
            constrictionVelocityUpdate.updateVelocity(particle);
        }
        catch(UnsupportedOperationException ex) {
            assertionExceptionOccured = true;
        }

        Assert.assertFalse(assertionExceptionOccured);

        RandomizingControlParameter randomizingControlParameter = new RandomizingControlParameter();
        randomizingControlParameter.setParameter(1.0);
        constrictionVelocityUpdate.setSocialAcceleration(new RandomizingControlParameter(randomizingControlParameter.getClone()));
        constrictionVelocityUpdate.setCognitiveAcceleration(new RandomizingControlParameter(randomizingControlParameter.getClone()));

        assertionExceptionOccured = false;
        try {
            constrictionVelocityUpdate.updateVelocity(particle);
        }
        catch(UnsupportedOperationException ex) {
            assertionExceptionOccured = true;
        }

        Assert.assertTrue(assertionExceptionOccured);
        
    }

    /**
     * Test velocity clamping.
     */
    @Test
    public void testClamping() {
        MersenneTwister twister = new MersenneTwister();
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setFunction(new Spherical());
        
        StandardParticle nBest = new StandardParticle();
        StandardParticle particle = new StandardParticle();
        particle.initialise(problem);
        nBest.initialise(problem);
        ((Vector)particle.getVelocity()).randomize(twister);
        particle.setNeighbourhoodBest(nBest);

        ConstrictionVelocityUpdate constrictionVelocityUpdate = new ConstrictionVelocityUpdate();
        constrictionVelocityUpdate.setVMax(new ConstantControlParameter(0.5));
        constrictionVelocityUpdate.updateVelocity(particle);
        Vector velocity = particle.getVelocity();
        for (Numeric number : velocity) {
            Assert.assertTrue(Double.compare(number.getReal(), 0.5) <= 0);
            Assert.assertTrue(Double.compare(number.getReal(), -0.5) >= 0);
        }

    }

}
