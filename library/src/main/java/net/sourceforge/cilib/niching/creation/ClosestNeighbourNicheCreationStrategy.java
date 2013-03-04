/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import java.util.Arrays;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.controlparameter.UpdateOnIterationControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.ClosestEntityVisitor;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.velocityprovider.ClampingVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.GCVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;

/**
 * <p>
 * Create a set of niching locations, based on a provided set of identified
 * niching entities.
 * </p>
 * <p>
 * For each newly discovered niching location, a new sub-swarmType is creates that will
 * maintain the niche. For the case of the PSO, the niching particle and the closest
 * particle to the identified particle are grouped into a niche. Sub-swarms will always
 * then have at least two particles.
 * </p>
 * <p>
 * The rational for two particles is that a particle is a social entity and as a result
 * needs to share information. Ensuring that there are at least two particles within
 * a sub-swarmType will enable the velocity update equation associated with the particle
 * to still operate.
 * </p>
 */
public class ClosestNeighbourNicheCreationStrategy extends NicheCreationStrategy {

    /**
     * Default constructor.
     */
    public ClosestNeighbourNicheCreationStrategy() {
        this.swarmType = new PSO();
        ((SynchronousIterationStrategy) ((PSO) this.swarmType).getIterationStrategy()).setBoundaryConstraint(new ClampingBoundaryConstraint());
        this.swarmType.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 500));

        ClampingVelocityProvider delegate = new ClampingVelocityProvider(ConstantControlParameter.of(1.0),
                new StandardVelocityProvider(new UpdateOnIterationControlParameter(new LinearlyVaryingControlParameter(0.7, 0.2)),
                    ConstantControlParameter.of(1.2), ConstantControlParameter.of(1.2)));

        GCVelocityProvider gcVelocityProvider = new GCVelocityProvider();
        gcVelocityProvider.setDelegate(delegate);
        gcVelocityProvider.setRho(ConstantControlParameter.of(0.01));

        this.swarmBehavior = new ParticleBehavior();
        this.swarmBehavior.setVelocityProvider(gcVelocityProvider);
    }

    @Override
    public NichingSwarms f(NichingSwarms a, Entity b) {
        //There should be at least two particles
        if (a.getMainSwarm().getTopology().size() <= 1 || !a.getMainSwarm().getTopology().contains(b)) {
            return a;
        }

        // Get closest particle
        ClosestEntityVisitor closestEntityVisitor = new ClosestEntityVisitor();
        closestEntityVisitor.setTargetEntity(b);
        a.getMainSwarm().accept(closestEntityVisitor);

        // Clone particles
        Particle nicheMainParticle = (Particle) b.getClone();
        Particle nicheClosestParticle = (Particle) closestEntityVisitor.getResult().getClone();

        // Set behavior and nBest
        nicheMainParticle.setNeighbourhoodBest(nicheMainParticle);
        nicheClosestParticle.setNeighbourhoodBest(nicheMainParticle);

        nicheMainParticle.setParticleBehavior(swarmBehavior.getClone());
        nicheClosestParticle.setParticleBehavior(swarmBehavior.getClone());

        // Create new subswarm
        PopulationBasedAlgorithm newSubSwarm = swarmType.getClone();
        newSubSwarm.setOptimisationProblem(a.getMainSwarm().getOptimisationProblem());
        newSubSwarm.getTopology().clear();
        ((Topology<Particle>) newSubSwarm.getTopology()).addAll(Arrays.asList(nicheMainParticle, nicheClosestParticle));

        // Create new mainswarm
        PopulationBasedAlgorithm newMainSwarm = a.getMainSwarm().getClone();
        newMainSwarm.getTopology().clear();
        for(Entity e : a.getMainSwarm().getTopology()) {
            if (!e.equals(b) && !e.equals(closestEntityVisitor.getResult())) {
                ((Topology<Entity>) newMainSwarm.getTopology()).add(e.getClone());
            }
        }

        return NichingSwarms.of(newMainSwarm, a.getSubswarms().cons(newSubSwarm));
    }
}
