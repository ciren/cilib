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
package net.sourceforge.cilib.algorithm;

import java.util.List;
import net.sourceforge.cilib.problem.OptimisationSolution;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EmptyStackException;

import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;

import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class GenericAlgorithmTest {

    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    @SuppressWarnings("static-access")
    @Test(expected=EmptyStackException.class)
    public void serialisationAndInitialisation() {
        // Any algorithm can be used, just need a concrete class
        AbstractAlgorithm algorithm = new PSO();
        ByteArrayOutputStream byteArray = null;

        try {
            byteArray = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(byteArray);
            o.writeObject(algorithm);
            o.close();
        } catch (IOException io) {}

        byte [] classData = byteArray.toByteArray();
        AbstractAlgorithm target = null;

        try {
            ObjectInputStream i = new ObjectInputStream(new ByteArrayInputStream(classData));
            target = (AbstractAlgorithm) i.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            algorithm.get();
        }
        catch (EmptyStackException e) {}

        // We have to make sure that the AlgorithmStack is in fact created and not null, stack should be empty
        assertNotNull(target.get());
    }

    @Test
    public void stoppingConditionInitialization() {
        final StoppingCondition stoppingCondition = context.mock(StoppingCondition.class);
        final OptimisationProblem problem = context.mock(OptimisationProblem.class);
        AbstractAlgorithm algorithm = new AbstractAlgorithm() {
            public AbstractAlgorithm getClone() { return null; }
            protected void algorithmIteration() {}
            public OptimisationSolution getBestSolution() { return null; }
            public List<OptimisationSolution> getSolutions() { return null; }
        };

        algorithm.addStoppingCondition(stoppingCondition);
        algorithm.setOptimisationProblem(problem);

        context.checking(new Expectations() {{
            ignoring(problem);
            oneOf(stoppingCondition).setAlgorithm(with(any(Algorithm.class)));
        }});

        algorithm.initialise();
    }

}
