/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.Ord;
import fj.Ordering;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.topologies.SpeciationNeighbourhood;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import net.sourceforge.cilib.util.functions.Entities;
import net.sourceforge.cilib.util.functions.Particles;

public class SpeciationIterationStrategy extends AbstractIterationStrategy<PSO> {

    private ControlParameter n = ConstantControlParameter.of(20);
    private ControlParameter radius = ConstantControlParameter.of(0.1);
    private DistanceMeasure distance = new EuclideanDistanceMeasure();

    @Override
    public SpeciationIterationStrategy getClone() {
        return this;
    }

    @Override
    public void performIteration(PSO pso) {
        final fj.data.List<Particle> topology = pso.getTopology();
        pso.setNeighbourhood(new SpeciationNeighbourhood<Particle>(distance, radius, n));

        final F<Particle, Particle> first = new F<Particle, Particle>() {
			@Override
			public Particle f(Particle current) {
				current.getBehaviour().performIteration(current);
	            return current;
			}
        };
        
        final F2<List<Particle>, List<Particle>, List<Particle>> second = new F2<List<Particle>, List<Particle>, List<Particle>>() {
            @Override
            public List<Particle> f(List<Particle> a, List<Particle> b) {
                if (a.isEmpty()) {
                    return b;
                }

                final List<Particle> sorted = a.sort(Ord.<Particle>ord(new F2<Particle, Particle, Ordering>() {
                    @Override
                    public Ordering f(Particle a, Particle b) {
                        return Ordering.values()[-a.getBestFitness().compareTo(b.getBestFitness()) + 1];
                    }
                }.curry()));

                List<Particle> neighbours = sorted.filter(new F<Particle, Boolean>() {
                    @Override
                    public Boolean f(Particle a) {
                        return distance.distance(a.getPosition(), sorted.head().getPosition()) < radius.getParameter();
                    }
                }).take((int) n.getParameter());

                return this.f(sorted.minus(Equal.<Particle>anyEqual(), neighbours), b.append(neighbours.map(Particles.setNeighbourhoodBest().f(sorted.head()))));
            }
        };

        pso.setTopology(second.f(topology.map(first).map(Entities.<Particle>evaluate()), List.<Particle>nil()));
    }

    public void setNeighbourhoodSize(ControlParameter n) {
        this.n = n;
    }

    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }

    public void setDistance(DistanceMeasure distance) {
        this.distance = distance;
    }
    
}
