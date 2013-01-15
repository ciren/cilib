/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.iterationstrategies;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import fj.P1;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.operations.MultiParentCrossoverOperation;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

public class GBestMutationIterationStrategy extends AbstractIterationStrategy<PSO> {

    private ControlParameter vMax;
    private IterationStrategy<PSO> delegate;
    private ProbabilityDistributionFunction distribution;

    public GBestMutationIterationStrategy() {
        PSOCrossoverIterationStrategy del = new PSOCrossoverIterationStrategy();
        del.setCrossoverOperation(new MultiParentCrossoverOperation());
        this.delegate = del;

        this.vMax = ConstantControlParameter.of(1.0);
        this.distribution = new CauchyDistribution();
    }

    public GBestMutationIterationStrategy(GBestMutationIterationStrategy copy) {
        this.vMax = copy.vMax.getClone();
        this.delegate = copy.delegate.getClone();
        this.distribution = copy.distribution;
    }

    @Override
    public AbstractIterationStrategy<PSO> getClone() {
        return new GBestMutationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        delegate.performIteration(algorithm);
        Topology<Particle> topology = algorithm.getTopology();

        // calculate vAvg
        Vector avgV = Vectors.mean(Lists.transform(topology, new Function<Particle, Vector>() {
            @Override
            public Vector apply(Particle f) {
                return (Vector) f.getVelocity();
            }
        }));

        Vector.Builder builder = Vector.newBuilder();
        for (Numeric n : avgV) {
            if (Math.abs(n.doubleValue()) > vMax.getParameter()) {
                builder.add(vMax.getParameter());
            } else {
                builder.add(n);
            }
        }

        avgV = builder.build();

        // mutation
        Particle gBest = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());
        Particle mutated = gBest.getClone();
        Vector pos = (Vector) gBest.getBestPosition();
        final Bounds bounds = pos.boundsOf(0);

        pos = pos.plus(avgV.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return distribution.getRandomNumber()*bounds.getRange() + bounds.getLowerBound();
            }
        }));

        mutated.setCandidateSolution(pos);
        mutated.calculateFitness();

        if (gBest.getBestFitness().compareTo(mutated.getFitness()) < 0) {
            gBest.getProperties().put(EntityType.Particle.BEST_FITNESS, mutated.getBestFitness());
            gBest.getProperties().put(EntityType.Particle.BEST_POSITION, mutated.getBestPosition());
        }
    }

    public void setVMax(ControlParameter vMax) {
        this.vMax = vMax;
    }

    public ControlParameter getVMax() {
        return vMax;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return distribution;
    }

    public IterationStrategy<PSO> getDelegate() {
        return delegate;
    }

    public void setDelegate(IterationStrategy<PSO> delegate) {
        this.delegate = delegate;
    }
}
