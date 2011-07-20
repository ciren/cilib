/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.problem;

import fj.data.List;
import fj.F4;
import fj.F;
import fj.F2;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

/**
 *
 * @author filipe
 */
public class ConstrainedFunctionsTest {
    /**
     * 1-Dimensional functions
     */
    @Test
    public void continuousStepAtPoint() {
        F<Double, Double> f = ConstrainedFunctions.continuousStep;
        
        Assert.assertThat(f.f(1.0), equalTo(2.25));
    }

    @Test
    public void continuousStepSolution() {
        F<Double, Double> f = ConstrainedFunctions.continuousStep;

        Assert.assertThat(f.f(-0.5), equalTo(0.0));
    }
    
    @Test
    public void maximumDeratingFunction1AtPoint() {
        F<Double, Double> f = ConstrainedFunctions.maximumDeratingFunction1;
        
        Assert.assertThat(f.f(1.0), equalTo(1.0));
        Assert.assertThat(f.f(0.24), equalTo(0.9216));
    }

    @Test
    public void maximumDeratingFunction1Solution() {
        F<Double, Double> f = ConstrainedFunctions.maximumDeratingFunction1;

        Assert.assertThat(f.f(0.0), equalTo(0.0));
    }
    
    @Test
    public void multimodalFunction1AtPoint() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction1;
        
        Assert.assertThat(f.f(0.75), closeTo(0.125, 0.000001));
    }

    @Test
    public void multimodalFunction1Solution() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction1;

        Assert.assertThat(f.f(0.0), closeTo(0.0, 0.000001));
        Assert.assertThat(f.f(1.0), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void multimodalFunction2AtPoint() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction2;
        
        Assert.assertThat(f.f(0.75), closeTo(0.0500560236151, 0.000001));
    }

    @Test
    public void multimodalFunction2Solution() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction2;

        Assert.assertThat(f.f(0.0), closeTo(0.0, 0.000001));
        Assert.assertThat(f.f(1.0), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void multimodalFunction3AtPoint() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction3;
        
        Assert.assertThat(f.f(0.75), closeTo(0.067632572, 0.000001));
    }

    @Test
    public void multimodalFunction3Solution() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction3;

        Assert.assertThat(f.f(0.018420157), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void multimodalFunction4AtPoint() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction4;
        
        Assert.assertThat(f.f(0.75), closeTo(0.0288126384338, 0.000001));
    }

    @Test
    public void multimodalFunction4Solution() {
        F<Double, Double> f = ConstrainedFunctions.multimodalFunction4;

        Assert.assertThat(f.f(0.018420157), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void schwefelAtPoint() {
        F<Double, Double> f = ConstrainedFunctions.schwefel;
        
        Assert.assertThat(f.f(0.0), closeTo(418.98288727, 0.000001));
    }

    @Test
    public void schwefelSolution() {
        F<Double, Double> f = ConstrainedFunctions.schwefel;

        //optimum is at -420.9687 * n
        Assert.assertThat(f.f(-420.9687), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void schwefelProblem2_26AtPoint() {
        F<Double, Double> f = ConstrainedFunctions.schwefelProblem2_26;
        
        Assert.assertThat(f.f(1.0), closeTo(-0.841470985, 0.000001));
    }

    @Test
    public void schwefelProblem2_26Solution() {
        F<Double, Double> f = ConstrainedFunctions.schwefelProblem2_26;

        //optimum is 418.9829 * n
        Assert.assertThat(f.f(-420.9687), closeTo(418.9829, 0.0001));
    }
    
    @Test
    public void shekelFoxholes2dimAtPoint() {
        F2<Double, Double, Double> f = ConstrainedFunctions.shekelFoxholes2dim;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(14.563023555857152, 0.000001));
    }

    @Test
    public void shekelFoxholes2dimSolution() {
        F2<Double, Double, Double> f = ConstrainedFunctions.shekelFoxholes2dim;

        Assert.assertThat(f.f(-32.0, -32.0), closeTo(1.0, 0.01));
    }
    
    /*@Test
    public void rippleAtPoint() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ripple;
        
        Assert.assertThat(f.f(0.0, 0.0), closeTo(24.09355104, 0.000001));
    }

    Found in same paper as Ursem functions (not original)
    @Test
    public void rippleSolution() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ripple;

        Assert.assertThat(f.f(-32.0, -32.0), closeTo(1.0, 0.01));
    }*/
    
    @Test
    public void schaffer2AtPoint() {
        F2<Double, Double, Double> f = ConstrainedFunctions.schaffer2;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(3416.289849001, 0.000001));
    }

    @Test
    public void schaffer2Solution() {
        F2<Double, Double, Double> f = ConstrainedFunctions.schaffer2;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void ursemF1AtPoint() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ursemF1;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(2.996751, 0.000001));
    }

    @Test
    public void ursemF1Solution() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ursemF1;

        //Maximization
        Assert.assertThat(f.f(1.697, 0.0), closeTo(4.81681, 0.00001));
    }
    
    @Test
    public void ursemF3AtPoint() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ursemF3;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.404508, 0.000001));
    }

    @Test
    public void ursemF3Solution() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ursemF3;

        //Maximization
        Assert.assertThat(f.f(0.0, 0.0), closeTo(2.5, 0.00001));
    }
    
    @Test
    public void ursemF4AtPoint() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ursemF4;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.0, 0.000001));
    }

    @Test
    public void ursemF4Solution() {
        F2<Double, Double, Double> f = ConstrainedFunctions.ursemF4;

        //Maximization
        Assert.assertThat(f.f(0.0, 0.0), equalTo(1.5));
    }
    
    @Test
    public void shekelFoxholes4dimAtPoint() {
        F4<Double, Double, Double, Double, Double> f = ConstrainedFunctions.shekelFoxholes4dim;
        
        Assert.assertThat(f.f(1.0, 1.0, 1.0, 1.0), closeTo(-5.12847104, 0.000001));
    }

    @Test
    public void shekelFoxholes4dimSolution() {
        F4<Double, Double, Double, Double, Double> f = ConstrainedFunctions.shekelFoxholes4dim;

        Assert.assertThat(f.f(4.00075,4.00059,3.99966,3.99951), closeTo(-10.5364, 0.0001));
    }
    
    /**
     * List functions
     */
    @Test
    public void nastyBenchmarkAtPoint() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.nastyBenchmark);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(1.0));
    }
    
    @Test
    public void nastyBenchmarkSolution() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.nastyBenchmark);
        
        //solution is at 1,2,3,...,n
        Assert.assertThat(e.evaluate(List.list(1.0, 2.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void neumaier3AtPoint() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.neumaier3);

        Assert.assertThat(e.evaluate(List.list(1.0, 2.0, 3.0)).some(), equalTo(-3.0));
    }
    
    @Test
    public void neumaier3Solution() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.neumaier3);
        
        Assert.assertThat(e.evaluate(List.list(3.0, 4.0, 3.0)).some(), equalTo(-7.0));
    }
    
    @Test
    public void schwefelProblem1_2AtPoint() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.schwefelProblem1_2);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(5.0));
    }
    
    @Test
    public void schwefelProblem1_2Solution() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.schwefelProblem1_2);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void shirAtPoint() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.shir);

        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), closeTo(0.067852776, 0.000001));
    }
    
    /* Optimum is supposed to be 1, dunno where
    @Test
    public void shirSolution() {
        Evaluatable e = Evaluators.createL(ConstrainedFunctions.shir);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }*/
    
    
}
