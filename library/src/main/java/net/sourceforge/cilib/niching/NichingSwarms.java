/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching;

import fj.F;
import fj.P;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.niching.NichingFunctions.NichingFunction;

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

    public PopulationBasedAlgorithm getMainSwarm() {
        return mainSwarm;
    }

    public List<PopulationBasedAlgorithm> getSubswarms() {
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
