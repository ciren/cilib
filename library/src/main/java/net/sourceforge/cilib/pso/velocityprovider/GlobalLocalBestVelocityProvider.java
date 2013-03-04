/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomControlParameter;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * M. Senthil Arumugam, M.V.C. Rao, "On the improved performances of the particle
 * swarm optimization algorithms with adaptive parameters, cross-over operators
 * and root mean square (RMS) variants for computing optimal control of a class
 * of hybrid systems", Applied Soft Computing, vol 8, pp 324--336, 2008,
 * doi:10.1016/j.asoc.2007.01.010
 * </p>
 */
public class GlobalLocalBestVelocityProvider implements VelocityProvider {

    private ControlParameter acceleration;
    private ControlParameter inertia;
    private ControlParameter random;

    public GlobalLocalBestVelocityProvider() {
        this.inertia = null;
        this.acceleration = null;
        this.random = new RandomControlParameter(new UniformDistribution());
    }

    public GlobalLocalBestVelocityProvider(GlobalLocalBestVelocityProvider copy) {
        this.random = copy.random.getClone();

        if (copy.acceleration != null) {
            this.acceleration = copy.acceleration.getClone();
        }

        if (copy.inertia != null) {
            this.inertia = copy.inertia.getClone();
        }
    }

    @Override
    public VelocityProvider getClone() {
        return new GlobalLocalBestVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.get();
        Topology<Particle> topology = (Topology<Particle>) algorithm.getTopology();
        Particle gBestParticle = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());
        double accValue;
        double inertiaValue;

        if (acceleration == null) {
            accValue = 1.0 + gBestParticle.getBestFitness().getValue() / particle.getBestFitness().getValue();
        } else {
            accValue = acceleration.getParameter();
        }

        if (inertia == null) {
            double average = 0;
            for(Particle p : topology) {
                average += p.getBestFitness().getValue();
            }

            average /= topology.size();

            inertiaValue = 1.1 - gBestParticle.getBestFitness().getValue() / average;
        } else {
            inertiaValue = inertia.getParameter();
        }

        Vector vel = (Vector) particle.getVelocity();
        Vector pBest = (Vector) particle.getBestPosition();
        Vector pos = (Vector) particle.getCandidateSolution();
        Vector gBest = (Vector) gBestParticle.getBestPosition();

        return vel.multiply(inertiaValue).plus(pBest.plus(gBest).plus(pos.multiply(-2.0)).multiply(accValue * random.getParameter()));
    }

    public void setRandom(ControlParameter random) {
        this.random = random;
    }

    public ControlParameter getRandom() {
        return random;
    }

    public void setInertia(ControlParameter inertia) {
        this.inertia = inertia;
    }

    public ControlParameter getInertia() {
        return inertia;
    }

    public void setAcceleration(ControlParameter acceleration) {
        this.acceleration = acceleration;
    }

    public ControlParameter getAcceleration() {
        return acceleration;
    }
}
