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

import fj.F;
import fj.F4;
import fj.F2;
import org.junit.Assert;
import fj.data.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

/**
 *
 */
public class UnconstrainedFunctionsTest {
    /**
     * 1-Dimensional functions
     */
    @Test
    public void alpineAtPoint() {
        F<Double, Double> f = UnconstrainedFunctions.alpine;
        
        Assert.assertThat(f.f(1.0), closeTo(0.941470985, 0.000001));
    }

    @Test
    public void alpineSolution() {
        F<Double, Double> f = UnconstrainedFunctions.alpine;

        Assert.assertThat(f.f(0.0), equalTo(0.0));
    }
    
    @Test
    public void quarticAtPoint() {
        F<Double, Double> f = UnconstrainedFunctions.quartic;
        
        Assert.assertThat(f.f(1.0), equalTo(1.0));
        Assert.assertThat(f.f(2.0), equalTo(16.0));
    }

    @Test
    public void quarticSolution() {
        F<Double, Double> f = UnconstrainedFunctions.quartic;

        Assert.assertThat(f.f(0.0), equalTo(0.0));
    }
    
    @Test
    public void sphericalAtPoint() {
        F<Double, Double> f = UnconstrainedFunctions.spherical;
        
        Assert.assertThat(f.f(1.0), equalTo(1.0));
        Assert.assertThat(f.f(2.0), equalTo(4.0));
    }

    @Test
    public void sphericalSolution() {
        F<Double, Double> f = UnconstrainedFunctions.spherical;

        Assert.assertThat(f.f(0.0), equalTo(0.0));
    }
    
    @Test
    public void rastriginAtPoint() {
        F<Double, Double> f = UnconstrainedFunctions.rastrigin;
        
        Assert.assertThat(f.f(1.0), equalTo(1.0));
    }

    @Test
    public void rastriginSolution() {
        F<Double, Double> f = UnconstrainedFunctions.rastrigin;

        Assert.assertThat(f.f(0.0), equalTo(0.0));
    }
    
    @Test
    public void stepAtPoint() {
        F<Double, Double> f = UnconstrainedFunctions.step;
        
        Assert.assertThat(f.f(1.0), equalTo(1.0));
    }

    @Test
    public void stepSolution() {
        F<Double, Double> f = UnconstrainedFunctions.step;

        Assert.assertThat(f.f(-0.5), equalTo(0.0));
        Assert.assertThat(f.f(0.49999999), equalTo(0.0));
    }
    
    /**
     * 2-Dimensional functions
     */
    @Test
    public void bealeAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.beale;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(14.203125, 0.000001));
    }

    @Test
    public void bealeSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.beale;

        Assert.assertThat(f.f(3.0, 0.5), equalTo(0.0));
    }
    
    @Test
    public void birdAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bird;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(1.593530491, 0.000001));
    }

    @Test
    public void birdSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bird;

        Assert.assertThat(f.f(4.701055751981055, 3.152946019601391), closeTo(-106.764537, 0.000001));
        Assert.assertThat(f.f(-1.582142172055011, -3.130246799635430), closeTo(-106.764537, 0.000001));
    }
    
    @Test
    public void bohachevsky1AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bohachevsky1;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(3.6, 0.000001));
    }

    @Test
    public void bohachevsky1Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bohachevsky1;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void bohachevsky2AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bohachevsky2;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(3.6, 0.000001));
    }

    @Test
    public void bohachevsky2Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bohachevsky2;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void bohachevsky3AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bohachevsky3;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(3.6, 0.000001));
    }

    @Test
    public void bohachevsky3Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bohachevsky3;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void boothAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.booth;
        
        Assert.assertThat(f.f(1.0, 1.0), equalTo(20.0));
    }

    @Test
    public void boothSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.booth;

        Assert.assertThat(f.f(1.0, 3.0), equalTo(0.0));
    }
    
    @Test
    public void braninAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.branin;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(27.7029055485, 0.000001));
    }

    @Test
    public void braninSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.branin;

        Assert.assertThat(f.f(-Math.PI, 12.274999), closeTo(0.397887, 0.000001));
        Assert.assertThat(f.f(Math.PI, 2.275), closeTo(0.397887, 0.000001));
        Assert.assertThat(f.f(9.424777, 2.474999), closeTo(0.397887, 0.000001));
    }
    
    @Test
    public void bukin4AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bukin4;
        
        Assert.assertThat(f.f(1.0, 1.0), equalTo(100.11));
    }

    @Test
    public void bukin4Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bukin4;

        Assert.assertThat(f.f(-10.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void bukin6AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bukin6;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(99.608743711, 0.000001));
    }

    @Test
    public void bukin6Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.bukin6;

        Assert.assertThat(f.f(-10.0, 1.0), equalTo(0.0));
    }
    
    @Test
    public void damavandiAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.damavandi;
        
        Assert.assertThat(f.f(1.0, 1.0), equalTo(110.0));
    }

    @Test
    public void damavandiSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.damavandi;

        Assert.assertThat(f.f(2.0000001, 2.0000001), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void easomAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.easom;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(-0.000030308, 0.000001));
    }

    @Test
    public void easomSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.easom;

        Assert.assertThat(f.f(Math.PI, Math.PI), equalTo(-1.0));
    }
    
    @Test
    public void eggHolderAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.eggHolder;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(-30.761412199, 0.000001));
    }

    @Test
    public void eggHolderSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.eggHolder;

        Assert.assertThat(f.f(512.0, 404.2319), closeTo(-959.640662711, 0.000001));
    }
    
    @Test
    public void goldsteinPriceAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.goldsteinPrice;
        
        Assert.assertThat(f.f(1.0, 1.0), equalTo(1876.0));
    }

    @Test
    public void goldsteinPriceSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.goldsteinPrice;

        Assert.assertThat(f.f(0.0, -1.0), equalTo(3.0));
    }
    
    @Test
    public void himmelblauAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.himmelblau;
        
        Assert.assertThat(f.f(1.0, 1.0), equalTo(106.0));
    }

    @Test
    public void himmelblauSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.himmelblau;

        Assert.assertThat(f.f(3.0, 2.0), closeTo(0.0, 0.000001));
        Assert.assertThat(f.f(-3.779310253377747, -3.283185991286170), closeTo(0.0, 0.000001));
        Assert.assertThat(f.f(-2.805118086952745,  3.131312518250573), closeTo(0.0, 0.000001));
        Assert.assertThat(f.f(3.584428340330492, -1.848126526964404), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void modifiedSchaffer2AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.modifiedSchaffer2;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.001994016, 0.000001));
    }

    @Test
    public void modifiedSchaffer2Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.modifiedSchaffer2;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void modifiedSchaffer3AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.modifiedSchaffer3;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.707243615, 0.000001));
    }

    @Test
    public void modifiedSchaffer3Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.modifiedSchaffer3;

        Assert.assertThat(f.f(0.0, 1.25313), closeTo(0.00156685, 0.000001));
    }
    
    @Test
    public void modifiedSchaffer4AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.modifiedSchaffer4;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.998005984, 0.000001));
    }

    @Test
    public void modifiedSchaffer4Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.modifiedSchaffer4;

        Assert.assertThat(f.f(0.0, 1.25313), closeTo(0.292579, 0.000001));
    }
    
    @Test
    public void rosenbrockAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.rosenbrock;
        
        Assert.assertThat(f.f(0.0, 0.0), equalTo(1.0));
    }

    @Test
    public void rosenbrockSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.rosenbrock;

        Assert.assertThat(f.f(1.0, 1.0), equalTo(0.0));
    }
    
    @Test
    public void pathologicalAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.pathological;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.3424315, 0.000001));
    }

    @Test
    public void pathologicalSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.pathological;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void schafferF6AtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.schafferF6;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(0.973786, 0.000001));
    }

    @Test
    public void schafferF6Solution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.schafferF6;

        Assert.assertThat(f.f(0.0, 0.0), equalTo(0.0));
    }
    
    @Test
    public void sixHumpCamelBackAtPoint() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.sixHumpCamelBack;
        
        Assert.assertThat(f.f(1.0, 1.0), closeTo(3.233333333, 0.000001));
    }

    @Test
    public void sixHumpCamelBackSolution() {
        F2<Double, Double, Double> f = UnconstrainedFunctions.sixHumpCamelBack;

        Assert.assertThat(f.f(0.08984201368301331, -0.7126564032704135), closeTo(-1.031628453489877, 0.000001));
        Assert.assertThat(f.f(-0.08984201368301331, 0.7126564032704135), closeTo(-1.031628453489877, 0.000001));
    }
    
    /**
     * 4-Dimensional Functions
     */
    @Test
    public void colvilleAtPoint() {
        F4<Double, Double, Double, Double, Double> f = UnconstrainedFunctions.colville;
        
        Assert.assertThat(f.f(0.0, 0.0, 0.0, 0.0), equalTo(42.0));
    }

    @Test
    public void colvilleSolution() {
        F4<Double, Double, Double, Double, Double> f = UnconstrainedFunctions.colville;

        Assert.assertThat(f.f(1.0, 1.0, 1.0, 1.0), equalTo(0.0));
    }

    /**
     * List functions
     */
    @Test
    public void ackleyAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.ackley);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), closeTo(3.625384938, 0.000001));
    }
    
    @Test
    public void ackleySolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.ackley);
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void dejongf4AtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.dejongf4);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(3.0));
    }
    
    @Test
    public void dejongf4Solution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.dejongf4);
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void ellipticAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.elliptic);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(1000001.0));
    }
    
    @Test
    public void ellipticSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.elliptic);
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }

    @Test
    public void griewankAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.griewank);

        Assert.assertThat(e.evaluate(List.list(Math.PI / 2, Math.PI / 2)).some(), closeTo(1.0012337, 0.000001));
    }

    @Test
    public void griewankSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.griewank);
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void hyperEllipsoidAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.hyperEllipsoid);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(3.0));
    }
    
    @Test
    public void hyperEllipsoidSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.hyperEllipsoid);
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void michalewiczAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.michalewicz);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), closeTo(-0.000025574, 0.000001));
    }
    
    /* Everyone knows what the optimum is but not where it is :P
    @Test
    public void michalewiczSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.michalewicz);
        Assert.assertThat(e.evaluate(List.list(Math.PI / 2, Math.PI / 2)).some(), closeTo(-9.66015, 0.000001));
    }*/
    
    @Test
    public void norwegianAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.norwegian);

        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.9801));
    }
    
    @Test
    public void norwegianSolution() {
        //Maximization
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.norwegian);
        
        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(1.0));
    }
    
    @Test
    public void quadricAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.quadric);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), equalTo(5.0));
    }
    
    @Test
    public void quadricSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.quadric);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void salomonAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.salomon);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), closeTo(1.999637542, 0.000001));
    }
    
    @Test
    public void salomonSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.salomon);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), equalTo(0.0));
    }
    
    @Test
    public void schwefel2_21AtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.schwefel2_21);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), closeTo(1.0, 0.000001));
    }
    
    @Test
    public void schwefel2_21Solution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.schwefel2_21);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void schwefel2_22AtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.schwefel2_22);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), closeTo(3.0, 0.000001));
    }
    
    @Test
    public void schwefel2_22Solution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.schwefel2_22);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), closeTo(0.0, 0.000001));
    }
    
    @Test
    public void zakharovAtPoint() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.zakharov);

        Assert.assertThat(e.evaluate(List.list(1.0, 1.0)).some(), closeTo(9.3125, 0.000001));
    }
    
    @Test
    public void zakharovSolution() {
        Evaluatable e = Evaluators.createL(UnconstrainedFunctions.zakharov);
        
        Assert.assertThat(e.evaluate(List.list(0.0, 0.0)).some(), closeTo(0.0, 0.000001));
    }
}
