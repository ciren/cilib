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
package net.sourceforge.cilib.niching;

import fj.F;
import fj.P;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import static net.sourceforge.cilib.niching.NichingFunctions.NichingFunction;

public class NichingSwarms extends P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> {
    
    final private PopulationBasedAlgorithm mainSwarm;
    final private List<PopulationBasedAlgorithm> subswarms;
    
    public static NichingSwarms of(PopulationBasedAlgorithm ms, List<PopulationBasedAlgorithm> ss) {
        return new NichingSwarms(ms, ss);
    }

    public static NichingSwarms of(PopulationBasedAlgorithm ms, java.util.List<PopulationBasedAlgorithm> ss) {
        return new NichingSwarms(ms, List.iterableList(ss));
    }

    public static NichingSwarms of(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> s) {
        return new NichingSwarms(s._1(), s._2());
    }

    public static NichingSwarms of(NichingSwarms s) {
        return new NichingSwarms(s._1(), s._2());
    }
    
    private NichingSwarms(PopulationBasedAlgorithm ms, List<PopulationBasedAlgorithm> ss) {
        this.mainSwarm = ms;
        this.subswarms = ss;
    }

    @Override
    public PopulationBasedAlgorithm _1() {
        return mainSwarm;
    }

    @Override
    public List<PopulationBasedAlgorithm> _2() {
        return subswarms;
    }

    /**
     * Performs an action only on the main swarm.
     */
    public static NichingFunction onMainSwarm(final F<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return of(a.map1(f));
            }
        };
    }

    /**
     * Performs an action only on the first sub-swarm.
     */
    public static NichingFunction onFirstSubSwarm(final F<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (a._2().isEmpty()) {
                    return a;
                }

                return of(a._1(), List.cons(f.f(a._2().head()), a._2().orTail(P.p(List.<PopulationBasedAlgorithm>nil()))));
            }
        };
    }

    /**
     * Performs an action all of the sub-swarms.
     */
    public static NichingFunction onSubswarms(final F<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return of(a._1(), a._2().map(f));
            }
        };
    }
}
