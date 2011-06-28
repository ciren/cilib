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
package net.cilib.algorithm;

import fj.F;
import fj.P2;
import fj.Show;
import fj.data.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class CrossoverProviderTest {

    @Test
    public void binding() {
        List<Double> x = List.list(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        List<Double> y = List.list(10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0,2.0 ,1.0);

        final RandomProvider randomProvider = new MersenneTwister();
        final int j = randomProvider.nextInt(x.length());

        List<Double> crossedOver = x.zip(y).zipIndex()
                .bind(new F<P2<P2<Double, Double>, Integer> , List<Double>>() {
            @Override
            public List<Double> f(P2<P2<Double, Double>, Integer> a) {
                return (randomProvider.nextDouble() < 0.5 || a._2() == j)
                        ? List.list(a._1()._1())
                        : List.list(a._1()._2());
            }
        });
        System.out.println(crossedOver.length());
        Show.listShow(Show.doubleShow).println(crossedOver);
    }
}
