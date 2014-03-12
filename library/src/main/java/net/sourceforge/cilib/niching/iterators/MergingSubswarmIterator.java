/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterators;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.niching.NichingFunctions.NichingFunction;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.Equal;
import fj.F2;
import fj.P;
import fj.P2;
import fj.data.List;

public class MergingSubswarmIterator extends SubswarmIterator {

    private ControlParameter granularity;
    private ControlParameter iterations;
    private DistanceMeasure distanceMeasure;

    public MergingSubswarmIterator() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.granularity = ConstantControlParameter.of(0.5);
        this.iterations = ConstantControlParameter.of(50);
        this.iterator = new SingleNicheIteration();
    }

    public MergingSubswarmIterator(MergingSubswarmIterator copy) {
        this.distanceMeasure = copy.distanceMeasure;
        this.granularity = copy.granularity.getClone();
        this.iterations = copy.iterations.getClone();
        this.iterator = copy.iterator;
    }

    @Override
    public SubswarmIterator getClone() {
        return new MergingSubswarmIterator(this);
    }

    private F2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm, P2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm>>
            mergeSingle(final ControlParameter granularity, final DistanceMeasure distanceMeasure) {
        return new F2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm, P2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm>>() {
            @Override
            public P2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm>
                    f(SinglePopulationBasedAlgorithm a, SinglePopulationBasedAlgorithm b) {
                SinglePopulationBasedAlgorithm newA = a.getClone();
                SinglePopulationBasedAlgorithm newB = b.getClone();
                newB.setTopology(List.nil());

		Particle gbest = Topologies.getBestEntity((List<Particle>) b.getTopology(), new SocialBestFitnessComparator<>());
                List<Entity> local = b.getTopology().removeAll(Equal.anyEqual().eq(gbest));
                for (Entity e : local) {
                    double d2 = distanceMeasure.distance((Vector) a.getBestSolution().getPosition(), e.getPosition());

                    if (d2 < granularity.getParameter()) {
                        newA.setTopology(newA.getTopology().snoc(e));

                        if (e instanceof Particle) {
                            e.setBehaviour(((Entity) a.getTopology().head()).getBehaviour());
                        }

                    } else {
                    	newB.setTopology(newB.getTopology().snoc(e));
                    }

		    if (newB.getTopology().isEmpty()) {
			newA.setTopology(newA.getTopology().snoc(gbest));
			gbest.setBehaviour(((Particle) a.getTopology().head()).getBehaviour());
		    } else {
			newB.setTopology(newB.getTopology().snoc(gbest));
		    }
                }

                // we clone to set the nBests
                return P.p(newA.getClone(), newB.getClone());
            }
        };
    }

    private NichingFunction merge(final ControlParameter granularity, final DistanceMeasure distanceMeasure) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (a.getSubswarms().isEmpty()) {
                    return a;
                }

                SinglePopulationBasedAlgorithm headSwarm = a.getSubswarms().head();
                List<SinglePopulationBasedAlgorithm> tailSwarms = List.nil();

                for (SinglePopulationBasedAlgorithm pba : a.getSubswarms().orTail(P.p(List.<SinglePopulationBasedAlgorithm>nil()))) {
                    if (headSwarm.getTopology().isEmpty()) {
                        tailSwarms = tailSwarms.cons(pba);
                        continue;
                    }

                    double d1 = distanceMeasure.distance((Vector) pba.getBestSolution().getPosition(), (Vector) headSwarm.getBestSolution().getPosition());

                    if (d1 < granularity.getParameter()) {
                        P2<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm> merged;

                        if (pba.getBestSolution().compareTo(headSwarm.getBestSolution()) < 0) {
                            //head swarm is better
                            merged = mergeSingle(granularity, distanceMeasure).f(headSwarm, pba);
                        } else {
                            //tail swarm is better
                            merged = mergeSingle(granularity, distanceMeasure).f(pba, headSwarm);
                            merged = merged.swap();
                        }

                        headSwarm = merged._1();

                        if (!merged._2().getTopology().isEmpty()) {
                            tailSwarms = tailSwarms.cons(merged._2());
                        }
                    } else {
                        tailSwarms = tailSwarms.cons(pba);
                    }
                }

                NichingSwarms next = this.f(NichingSwarms.of(a.getMainSwarm(), tailSwarms));

                List<SinglePopulationBasedAlgorithm> newTail = next.getSubswarms();
                if (!headSwarm.getTopology().isEmpty()) {
                    newTail = newTail.cons(headSwarm);
                }

                return NichingSwarms.of(a.getMainSwarm(), newTail);
            }
        };
    }

    @Override
    public NichingSwarms f(NichingSwarms a) {
        NichingSwarms newSwarms = a;

        for (int k = 0; k < iterations.getParameter(); k++) {
            newSwarms = NichingSwarms.onSubswarms(iterator).f(newSwarms);
        }

        newSwarms = merge(granularity, distanceMeasure).f(newSwarms);

        return newSwarms;
    }

    public void setIterations(ControlParameter iterations) {
        this.iterations = iterations;
    }

    public ControlParameter getIterations() {
        return iterations;
    }

    public void setGranularity(ControlParameter granularity) {
        this.granularity = granularity;
    }

    public ControlParameter getGranularity() {
        return granularity;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }
}
