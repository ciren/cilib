/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.niching.NichingFunctions.NichingFunction;
import fj.F;
import fj.P;
import fj.P2;
import fj.data.List;

public class NichingSwarms extends P2<SinglePopulationBasedAlgorithm, List<SinglePopulationBasedAlgorithm>> {
    
    final private SinglePopulationBasedAlgorithm mainSwarm;
    final private List<SinglePopulationBasedAlgorithm> subswarms;
    
    public static NichingSwarms of(SinglePopulationBasedAlgorithm ms, List<SinglePopulationBasedAlgorithm> ss) {
        return new NichingSwarms(ms, ss);
    }

    public static NichingSwarms of(SinglePopulationBasedAlgorithm ms, java.util.List<SinglePopulationBasedAlgorithm> ss) {
        return new NichingSwarms(ms, List.iterableList(ss));
    }

    public static NichingSwarms of(P2<SinglePopulationBasedAlgorithm, List<SinglePopulationBasedAlgorithm>> s) {
        return new NichingSwarms(s._1(), s._2());
    }

    public static NichingSwarms of(NichingSwarms s) {
        return new NichingSwarms(s._1(), s._2());
    }
    
    private NichingSwarms(SinglePopulationBasedAlgorithm ms, List<SinglePopulationBasedAlgorithm> ss) {
        this.mainSwarm = ms;
        this.subswarms = ss;
    }

    @Override
    public SinglePopulationBasedAlgorithm _1() {
        return mainSwarm;
    }

    @Override
    public List<SinglePopulationBasedAlgorithm> _2() {
        return subswarms;
    }

    public SinglePopulationBasedAlgorithm getMainSwarm() {
        return mainSwarm;
    }

    public List<SinglePopulationBasedAlgorithm> getSubswarms() {
        return subswarms;
    }

    /**
     * Performs an action only on the main swarm.
     */
    public static NichingFunction onMainSwarm(final F<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm> f) {
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
    public static NichingFunction onFirstSubSwarm(final F<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm> f) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (a._2().isEmpty()) {
                    return a;
                }

                return of(a._1(), List.cons(f.f(a._2().head()), a._2().orTail(P.p(List.<SinglePopulationBasedAlgorithm>nil()))));
            }
        };
    }

    /**
     * Performs an action all of the sub-swarms.
     */
    public static NichingFunction onSubswarms(final F<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm> f) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return of(a._1(), a._2().map(f));
            }
        };
    }
}
